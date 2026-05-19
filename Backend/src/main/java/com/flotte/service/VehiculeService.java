package com.flotte.service;

import com.flotte.converter.VehiculeConverter;
import com.flotte.dto.AlertMaintenanceDTO;
import com.flotte.dto.VehiculeDTO;
import com.flotte.entity.Vehicule;
import com.flotte.repository.TrajetMissionRepository;
import com.flotte.repository.VehiculeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehiculeService {

    @Autowired
    private VehiculeRepository vehiculeRepository;

    @Autowired
    private TrajetMissionRepository trajetRepository;

    @Autowired
    private VehiculeConverter vehiculeConverter;

    // Retourner tous les vehicules
    public List<VehiculeDTO> findAll() {
        return vehiculeConverter.toDtoList(vehiculeRepository.findAll());
    }

    public Optional<VehiculeDTO> findById(Long id) {
        return vehiculeRepository.findById(id)
                .map(vehiculeConverter::toDto);
    }

    public List<VehiculeDTO> findByStatut(String statut) {
        return vehiculeConverter.toDtoList(vehiculeRepository.findByStatut(statut));
    }

    // Créer un vehicule
    public VehiculeDTO save(VehiculeDTO dto) {
        if (vehiculeRepository.existsByImmatriculation(dto.getImmatriculation())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Un véhicule avec l'immatriculation " + dto.getImmatriculation() + " existe déjà");
        }
        Vehicule vehicule = vehiculeConverter.fromDto(dto);
        if (vehicule.getKilometrage() == null) vehicule.setKilometrage(0);
        if (vehicule.getStatut() == null)      vehicule.setStatut("DISPONIBLE");
        return vehiculeConverter.toDto(vehiculeRepository.save(vehicule));
    }

    public VehiculeDTO update(Long id, VehiculeDTO dto) {
        final VehiculeDTO[] result = {null};
        vehiculeRepository.findById(id).ifPresentOrElse(
                vehicule -> {
                    vehicule.setImmatriculation(dto.getImmatriculation());
                    vehicule.setModele(dto.getModele());
                    vehicule.setType(dto.getType());
                    vehicule.setKilometrage(dto.getKilometrage());
                    vehicule.setStatut(dto.getStatut());
                    result[0] = vehiculeConverter.toDto(vehiculeRepository.save(vehicule));
                },
                () -> { throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Véhicule avec l'id " + id + " introuvable"); }
        );
        return result[0];
    }

    // Mettre à jour le statut — ifPresentOrElse
    public VehiculeDTO updateStatut(Long id, String statut) {
        final VehiculeDTO[] result = {null};
        vehiculeRepository.findById(id).ifPresentOrElse(
                vehicule -> {
                    vehicule.setStatut(statut);
                    result[0] = vehiculeConverter.toDto(vehiculeRepository.save(vehicule));
                },
                () -> { throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Véhicule avec l'id " + id + " introuvable"); }
        );
        return result[0];
    }

    // Mettre à jour le kilométrage — ifPresentOrElse
    public VehiculeDTO updateKilometrage(Long id, Integer kilometrage) {
        final VehiculeDTO[] result = {null};
        vehiculeRepository.findById(id).ifPresentOrElse(
                vehicule -> {
                    vehicule.setKilometrage(kilometrage);
                    result[0] = vehiculeConverter.toDto(vehiculeRepository.save(vehicule));
                },
                () -> { throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Véhicule avec l'id " + id + " introuvable"); }
        );
        return result[0];
    }

    // Supprimer un vehicule — ifPresentOrElse
    public void delete(Long id) {
        vehiculeRepository.findById(id).ifPresentOrElse(
                vehicule -> {
                    if (trajetRepository.vehiculeEnMission(id)) {
                        throw new ResponseStatusException(
                                HttpStatus.CONFLICT, "Impossible de supprimer un véhicule en mission");
                    }
                    vehiculeRepository.deleteById(id);
                },
                () -> { throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Véhicule avec l'id " + id + " introuvable"); }
        );
    }

    // Alertes maintenance
    public List<AlertMaintenanceDTO> getAlertesMaintenance(Integer seuil) {
        return vehiculeRepository.findVehiculesPourMaintenance(seuil).stream()
                .map(v -> {
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
                })
                .collect(Collectors.toList());
    }

    // Véhicules les plus actifs
    public List<VehiculeDTO> getVehiculesLesPlasActifs() {
        return vehiculeConverter.toDtoList(vehiculeRepository.findVehiculesLasPlusActifs());
    }
}
