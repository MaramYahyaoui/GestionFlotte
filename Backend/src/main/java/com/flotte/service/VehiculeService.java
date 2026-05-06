package com.flotte.service;

import com.flotte.converter.VehiculeConverter;
import com.flotte.dto.AlertMaintenanceDTO;
import com.flotte.dto.VehiculeDTO;
import com.flotte.entity.Vehicule;
import com.flotte.repository.ConsommationRepository;
import com.flotte.repository.TrajetMissionRepository;
import com.flotte.repository.VehiculeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class VehiculeService {

    @Autowired
    private VehiculeRepository vehiculeRepository;

    @Autowired
    private TrajetMissionRepository trajetRepository;

    @Autowired
    private ConsommationRepository consommationRepository;

    @Autowired
    private VehiculeConverter vehiculeConverter;  // injection du mapper (slide 25)

    @Value("${fleet.maintenance.seuil-kilometrage:10000}")
    private Integer seuilMaintenance;

    public List<VehiculeDTO> findAll() {
        return vehiculeConverter.toDtoList(vehiculeRepository.findAll());
    }

    public VehiculeDTO findById(Long id) {
        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Véhicule avec l'id " + id + " introuvable"));
        return vehiculeConverter.toDto(vehicule);
    }

    public VehiculeDTO findByImmatriculation(String immatriculation) {
        Vehicule vehicule = vehiculeRepository.findByImmatriculation(immatriculation)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Véhicule avec immatriculation " + immatriculation + " introuvable"));
        return vehiculeConverter.toDto(vehicule);
    }

    public List<VehiculeDTO> findByStatut(String statut) {
        return vehiculeConverter.toDtoList(vehiculeRepository.findByStatut(statut));
    }

    public List<VehiculeDTO> findActifs() {
        return vehiculeConverter.toDtoList(vehiculeRepository.findVehiculesActifs());
    }

    @Transactional
    public VehiculeDTO create(VehiculeDTO dto) {
        if (vehiculeRepository.existsByImmatriculation(dto.getImmatriculation())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Un véhicule avec l'immatriculation " + dto.getImmatriculation() + " existe déjà");
        }
        Vehicule vehicule = vehiculeConverter.fromDto(dto);
        if (vehicule.getKilometrage() == null) vehicule.setKilometrage(0);
        if (vehicule.getStatut()      == null) vehicule.setStatut("DISPONIBLE");
        return vehiculeConverter.toDto(vehiculeRepository.save(vehicule));
    }

    @Transactional
    public VehiculeDTO update(Long id, VehiculeDTO dto) {
        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Véhicule avec l'id " + id + " introuvable"));

        if (!vehicule.getImmatriculation().equals(dto.getImmatriculation())
                && vehiculeRepository.existsByImmatriculation(dto.getImmatriculation())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "L'immatriculation " + dto.getImmatriculation() + " est déjà utilisée");
        }
        vehicule.setImmatriculation(dto.getImmatriculation());
        vehicule.setModele(dto.getModele());
        vehicule.setType(dto.getType());
        vehicule.setKilometrage(dto.getKilometrage());
        vehicule.setStatut(dto.getStatut());
        return vehiculeConverter.toDto(vehiculeRepository.save(vehicule));
    }

    @Transactional
    public VehiculeDTO updateStatut(Long id, String statut) {
        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Véhicule avec l'id " + id + " introuvable"));
        vehicule.setStatut(statut);
        return vehiculeConverter.toDto(vehiculeRepository.save(vehicule));
    }

    @Transactional
    public VehiculeDTO updateKilometrage(Long id, Integer kilometrage) {
        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Véhicule avec l'id " + id + " introuvable"));
        if (kilometrage < vehicule.getKilometrage()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le nouveau kilométrage (" + kilometrage
                            + ") ne peut pas être inférieur à l'actuel (" + vehicule.getKilometrage() + ")");
        }
        vehicule.setKilometrage(kilometrage);
        return vehiculeConverter.toDto(vehiculeRepository.save(vehicule));
    }

    @Transactional
    public void delete(Long id) {
        if (!vehiculeRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Véhicule avec l'id " + id + " introuvable");
        }
        if (trajetRepository.vehiculeEnMission(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Impossible de supprimer un véhicule en mission");
        }
        vehiculeRepository.deleteById(id);
    }

    public List<AlertMaintenanceDTO> getAlertesMaintenance() {
        return vehiculeRepository.findVehiculesPourMaintenance(seuilMaintenance).stream()
                .map(v -> buildAlerte(v, seuilMaintenance))
                .collect(Collectors.toList());
    }

    public List<AlertMaintenanceDTO> getAlertesMaintenancePersonnalise(Integer seuil) {
        return vehiculeRepository.findVehiculesPourMaintenance(seuil).stream()
                .map(v -> buildAlerte(v, seuil))
                .collect(Collectors.toList());
    }

    public List<VehiculeDTO> getVehiculesLesPlasActifs() {
        return vehiculeConverter.toDtoList(vehiculeRepository.findVehiculesLasPlusActifs());
    }

    private AlertMaintenanceDTO buildAlerte(Vehicule v, Integer seuil) {
        int depassement = v.getKilometrage() - seuil;
        String niveau = depassement > 20000 ? "CRITIQUE"
                : depassement > 5000 ? "ATTENTION" : "INFO";
        return AlertMaintenanceDTO.builder()
                .vehiculeId(v.getId())
                .immatriculation(v.getImmatriculation())
                .modele(v.getModele())
                .kilometrageActuel(v.getKilometrage())
                .seuilMaintenance(seuil)
                .depassement(depassement)
                .niveauAlerte(niveau)
                .build();
    }
}
