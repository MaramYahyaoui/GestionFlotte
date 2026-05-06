package com.flotte.service;

import com.flotte.dto.DashboardDTO;
import com.flotte.dto.VehiculeActifDTO;
import com.flotte.repository.ChauffeurRepository;
import com.flotte.repository.ConsommationRepository;
import com.flotte.repository.TrajetMissionRepository;
import com.flotte.repository.VehiculeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReportingService {

    @Autowired
    private VehiculeRepository vehiculeRepository;

    @Autowired
    private ChauffeurRepository chauffeurRepository;

    @Autowired
    private TrajetMissionRepository trajetRepository;

    @Autowired
    private ConsommationRepository consommationRepository;

    @Autowired
    private VehiculeService vehiculeService;

    @Value("${fleet.maintenance.seuil-kilometrage:10000}")
    private Integer seuilMaintenance;

    public DashboardDTO getDashboardGlobal() {
        long totalVehicules    = vehiculeRepository.count();
        long disponibles       = vehiculeRepository.findByStatut("DISPONIBLE").size();
        long enMission         = vehiculeRepository.findByStatut("EN_MISSION").size();
        long enMaintenance     = vehiculeRepository.findByStatut("EN_MAINTENANCE").size();
        long totalChauffeurs   = chauffeurRepository.count();
        long missionsEnCours   = trajetRepository.findByStatut("EN_COURS").size();
        long totalMissions     = trajetRepository.count();
        Double coutTotal       = consommationRepository.sumCoutTotal();

        List<Object[]> distancesData = trajetRepository.sumDistanceParVehicule();
        double distanceTotale = distancesData.stream()
                .mapToDouble(row -> row[1] != null ? ((Number) row[1]).doubleValue() : 0)
                .sum();

        return DashboardDTO.builder()
                .totalVehicules(totalVehicules)
                .vehiculesDisponibles(disponibles)
                .vehiculesEnMission(enMission)
                .vehiculesEnMaintenance(enMaintenance)
                .totalChauffeurs(totalChauffeurs)
                .missionsEnCours(missionsEnCours)
                .totalMissions(totalMissions)
                .coutCarburantTotal(coutTotal != null ? coutTotal : 0.0)
                .distanceTotale(distanceTotale)
                .alertesMaintenance(vehiculeService.getAlertesMaintenance())
                .build();
    }

    public List<VehiculeActifDTO> getFLotteActive() {
        Map<Long, Long> missionsParVehicule = new HashMap<>();
        trajetRepository.countMissionsParVehicule()
                .forEach(row -> missionsParVehicule.put((Long) row[0], (Long) row[1]));

        Map<Long, Double> distanceParVehicule = new HashMap<>();
        trajetRepository.sumDistanceParVehicule()
                .forEach(row -> distanceParVehicule.put(
                        (Long) row[0],
                        row[1] != null ? ((Number) row[1]).doubleValue() : 0.0));

        Map<Long, Double> coutParVehicule = new HashMap<>();
        consommationRepository.sumCoutParVehicule()
                .forEach(row -> coutParVehicule.put(
                        (Long) row[0],
                        row[1] != null ? ((Number) row[1]).doubleValue() : 0.0));

        return vehiculeRepository.findAll().stream()
                .map(v -> VehiculeActifDTO.builder()
                        .vehiculeId(v.getId())
                        .immatriculation(v.getImmatriculation())
                        .modele(v.getModele())
                        .type(v.getType())
                        .kilometrage(v.getKilometrage())
                        .nombreMissions(missionsParVehicule.getOrDefault(v.getId(), 0L))
                        .distanceTotale(distanceParVehicule.getOrDefault(v.getId(), 0.0))
                        .coutCarburant(coutParVehicule.getOrDefault(v.getId(), 0.0))
                        .build())
                .sorted(Comparator.comparingLong(VehiculeActifDTO::getNombreMissions).reversed())
                .collect(Collectors.toList());
    }

    public List<VehiculeActifDTO> getTop5VehiculesActifs() {
        return getFLotteActive().stream().limit(5).collect(Collectors.toList());
    }

    public Map<String, Long> getStatutRepartition() {
        Map<String, Long> repartition = new LinkedHashMap<>();
        repartition.put("DISPONIBLE",    (long) vehiculeRepository.findByStatut("DISPONIBLE").size());
        repartition.put("EN_MISSION",    (long) vehiculeRepository.findByStatut("EN_MISSION").size());
        repartition.put("EN_MAINTENANCE",(long) vehiculeRepository.findByStatut("EN_MAINTENANCE").size());
        repartition.put("EN_PANNE",      (long) vehiculeRepository.findByStatut("EN_PANNE").size());
        return repartition;
    }

    public Map<String, Long> getMissionsParMois(int annee) {
        Map<String, Long> result = new LinkedHashMap<>();
        String[] mois = {"Janvier","Février","Mars","Avril","Mai","Juin",
                          "Juillet","Août","Septembre","Octobre","Novembre","Décembre"};
        for (int m = 1; m <= 12; m++) {
            long count = trajetRepository.findByDateMissionBetween(
                    java.time.LocalDate.of(annee, m, 1),
                    java.time.LocalDate.of(annee, m, 1).withDayOfMonth(
                            java.time.LocalDate.of(annee, m, 1).lengthOfMonth())
            ).size();
            result.put(mois[m - 1], count);
        }
        return result;
    }
}
