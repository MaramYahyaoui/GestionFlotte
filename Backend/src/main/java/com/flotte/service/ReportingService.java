package com.flotte.service;

import com.flotte.dto.AlertMaintenanceDTO;
import com.flotte.dto.VehiculeDTO;
import com.flotte.repository.ChauffeurRepository;
import com.flotte.repository.ConsommationRepository;
import com.flotte.repository.TrajetMissionRepository;
import com.flotte.repository.VehiculeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
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

    // Dashboard global
    public Map<String, Object> getDashboard() {
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("totalVehicules",         vehiculeRepository.count());
        dashboard.put("totalChauffeurs",        chauffeurRepository.count());
        dashboard.put("totalMissions",          trajetRepository.count());
        dashboard.put("missionsEnCours",        trajetRepository.findByStatut("EN_COURS").size());
        dashboard.put("vehiculesDisponibles",   vehiculeRepository.findByStatut("DISPONIBLE").size());
        dashboard.put("vehiculesEnMission",     vehiculeRepository.findByStatut("EN_MISSION").size());
        dashboard.put("vehiculesEnMaintenance", vehiculeRepository.findByStatut("EN_MAINTENANCE").size());
        Double coutTotal = consommationRepository.sumCoutTotal();
        dashboard.put("coutCarburantTotal", coutTotal != null ? coutTotal : 0.0);
        // distance totale
        double distanceTotale = trajetRepository.sumDistanceParVehicule().stream()
                .mapToDouble(r -> r[1] != null ? ((Number) r[1]).doubleValue() : 0)
                .sum();
        dashboard.put("distanceTotale", distanceTotale);
        // alertes maintenance
        dashboard.put("alertesMaintenance", vehiculeService.getAlertesMaintenance(10000));
        return dashboard;
    }

    // Top 5 véhicules les plus actifs
    public List<VehiculeDTO> getTop5VehiculesActifs() {
        return vehiculeService.getVehiculesLesPlasActifs()
                .stream().limit(5)
                .collect(Collectors.toList());
    }

    // Flotte active — tous les véhicules avec stats
    public List<Map<String, Object>> getFLotteActive() {
        return vehiculeRepository.findAll().stream().map(v -> {
            Map<String, Object> item = new HashMap<>();
            item.put("vehiculeId",     v.getId());
            item.put("immatriculation",v.getImmatriculation());
            item.put("modele",         v.getModele());
            item.put("type",           v.getType());
            item.put("kilometrage",    v.getKilometrage());
            item.put("statut",         v.getStatut());
            long nbMissions = v.getTrajets() != null ? v.getTrajets().size() : 0;
            double distance = v.getTrajets() != null ? v.getTrajets().stream()
                    .mapToDouble(t -> t.getDistance() != null ? t.getDistance() : 0).sum() : 0;
            double coutCarb = v.getConsommations() != null ? v.getConsommations().stream()
                    .mapToDouble(c -> c.getCoutTotal() != null ? c.getCoutTotal() : 0).sum() : 0;
            item.put("nombreMissions", nbMissions);
            item.put("distanceTotale", distance);
            item.put("coutCarburant",  coutCarb);
            return item;
        }).collect(Collectors.toList());
    }

    // Répartition des véhicules par statut
    public Map<String, Long> getStatuts() {
        Map<String, Long> statuts = new LinkedHashMap<>();
        statuts.put("DISPONIBLE",     (long) vehiculeRepository.findByStatut("DISPONIBLE").size());
        statuts.put("EN_MISSION",     (long) vehiculeRepository.findByStatut("EN_MISSION").size());
        statuts.put("EN_MAINTENANCE", (long) vehiculeRepository.findByStatut("EN_MAINTENANCE").size());
        statuts.put("EN_PANNE",       (long) vehiculeRepository.findByStatut("EN_PANNE").size());
        return statuts;
    }

    // Missions par mois pour une année
    public Map<String, Long> getMissionsParMois(int annee) {
        Map<String, Long> result = new LinkedHashMap<>();
        for (int m = 1; m <= 12; m++) {
            LocalDate debut = LocalDate.of(annee, m, 1);
            LocalDate fin   = debut.withDayOfMonth(debut.lengthOfMonth());
            long count = trajetRepository.findByDateMissionBetween(debut, fin).size();
            String moisLibelle = Month.of(m).getDisplayName(TextStyle.FULL, Locale.FRENCH);
            result.put(moisLibelle, count);
        }
        return result;
    }
}
