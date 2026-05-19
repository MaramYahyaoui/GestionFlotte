package com.flotte.service;

import com.flotte.converter.TrajetMissionConverter;
import com.flotte.dto.TrajetMissionDTO;
import com.flotte.entity.Chauffeur;
import com.flotte.entity.TrajetMission;
import com.flotte.entity.Vehicule;
import com.flotte.repository.ChauffeurRepository;
import com.flotte.repository.TrajetMissionRepository;
import com.flotte.repository.VehiculeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TrajetMissionService {

    @Autowired
    private TrajetMissionRepository trajetRepository;

    @Autowired
    private VehiculeRepository vehiculeRepository;

    @Autowired
    private ChauffeurRepository chauffeurRepository;

    @Autowired
    private TrajetMissionConverter trajetConverter;

    // Retourner toutes les missions
    public List<TrajetMissionDTO> findAll() {
        return trajetConverter.toDtoList(trajetRepository.findAll());
    }

    public Optional<TrajetMissionDTO> findById(Long id) {
        return trajetRepository.findById(id).map(trajetConverter::toDto);
    }

    // Retourner les missions par statut
    public List<TrajetMissionDTO> findByStatut(String statut) {
        return trajetConverter.toDtoList(trajetRepository.findByStatut(statut));
    }

    // Créer une mission
    public TrajetMissionDTO save(TrajetMissionDTO dto) {
        Vehicule vehicule = vehiculeRepository.findById(dto.getVehiculeId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Véhicule avec l'id " + dto.getVehiculeId() + " introuvable"));

        Chauffeur chauffeur = chauffeurRepository.findById(dto.getChauffeurId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Chauffeur avec l'id " + dto.getChauffeurId() + " introuvable"));

        if (trajetRepository.vehiculeEnMission(dto.getVehiculeId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Le véhicule " + vehicule.getImmatriculation() + " est déjà en mission");
        }

        if (trajetRepository.chauffeurAMissionEnCours(dto.getChauffeurId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Le chauffeur " + chauffeur.getNom() + " a déjà une mission en cours");
        }

        TrajetMission mission = TrajetMission.builder()
                .vehicule(vehicule)
                .chauffeur(chauffeur)
                .pointDepart(dto.getPointDepart())
                .destination(dto.getDestination())
                .distance(dto.getDistance())
                .dateMission(dto.getDateMission() != null ? dto.getDateMission() : LocalDate.now())
                .statut("EN_COURS")
                .build();

        vehicule.setStatut("EN_MISSION");
        vehiculeRepository.save(vehicule);

        return trajetConverter.toDto(trajetRepository.save(mission));
    }

    public TrajetMissionDTO update(Long id, TrajetMissionDTO dto) {
        final TrajetMissionDTO[] result = {null};
        trajetRepository.findById(id).ifPresentOrElse(
                mission -> {
                    mission.setPointDepart(dto.getPointDepart());
                    mission.setDestination(dto.getDestination());
                    mission.setDistance(dto.getDistance());
                    if (dto.getDateMission() != null) mission.setDateMission(dto.getDateMission());
                    result[0] = trajetConverter.toDto(trajetRepository.save(mission));
                },
                () -> { throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Mission avec l'id " + id + " introuvable"); }
        );
        return result[0];
    }

    public TrajetMissionDTO terminerMission(Long id) {
        final TrajetMissionDTO[] result = {null};
        trajetRepository.findById(id).ifPresentOrElse(
                mission -> {
                    mission.setStatut("TERMINEE");
                    Vehicule vehicule = mission.getVehicule();
                    vehicule.setKilometrage(vehicule.getKilometrage() + mission.getDistance().intValue());
                    vehicule.setStatut("DISPONIBLE");
                    vehiculeRepository.save(vehicule);
                    result[0] = trajetConverter.toDto(trajetRepository.save(mission));
                },
                () -> { throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Mission avec l'id " + id + " introuvable"); }
        );
        return result[0];
    }

    public void delete(Long id) {
        trajetRepository.findById(id).ifPresentOrElse(
                mission -> trajetRepository.deleteById(id),
                () -> { throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Mission avec l'id " + id + " introuvable"); }
        );
    }
}
