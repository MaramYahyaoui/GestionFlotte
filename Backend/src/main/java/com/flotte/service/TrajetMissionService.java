package com.flotte.service;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TrajetMissionService {

    @Autowired
    private TrajetMissionRepository trajetRepository;

    @Autowired
    private VehiculeRepository vehiculeRepository;

    @Autowired
    private ChauffeurRepository chauffeurRepository;

    public List<TrajetMissionDTO> findAll() {
        return trajetRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public TrajetMissionDTO findById(Long id) {
        return trajetRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Mission avec l'id " + id + " introuvable"));
    }

    public List<TrajetMissionDTO> findByVehicule(Long vehiculeId) {
        if (!vehiculeRepository.existsById(vehiculeId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Véhicule avec l'id " + vehiculeId + " introuvable");
        }
        return trajetRepository.findByVehiculeId(vehiculeId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<TrajetMissionDTO> findByChauffeur(Long chauffeurId) {
        if (!chauffeurRepository.existsById(chauffeurId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Chauffeur avec l'id " + chauffeurId + " introuvable");
        }
        return trajetRepository.findByChauffeurId(chauffeurId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<TrajetMissionDTO> findByStatut(String statut) {
        return trajetRepository.findByStatut(statut).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<TrajetMissionDTO> findEnCours() {
        return trajetRepository.findByStatut("EN_COURS").stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<TrajetMissionDTO> findByPeriode(LocalDate debut, LocalDate fin) {
        return trajetRepository.findByDateMissionBetween(debut, fin).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public TrajetMissionDTO create(TrajetMissionDTO dto) {
        Vehicule vehicule = vehiculeRepository.findById(dto.getVehiculeId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Véhicule avec l'id " + dto.getVehiculeId() + " introuvable"));

        Chauffeur chauffeur = chauffeurRepository.findById(dto.getChauffeurId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Chauffeur avec l'id " + dto.getChauffeurId() + " introuvable"));

        if (trajetRepository.vehiculeEnMission(dto.getVehiculeId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Le véhicule " + vehicule.getImmatriculation() + " est déjà en mission");
        }

        if (trajetRepository.chauffeurAMissionEnCours(dto.getChauffeurId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Le chauffeur " + chauffeur.getNom() + " a déjà une mission en cours");
        }

        if ("EN_PANNE".equals(vehicule.getStatut()) || "EN_MAINTENANCE".equals(vehicule.getStatut())) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "Le véhicule " + vehicule.getImmatriculation()
                            + " n'est pas disponible (statut: " + vehicule.getStatut() + ")");
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

        return toDTO(trajetRepository.save(mission));
    }

    @Transactional
    public TrajetMissionDTO update(Long id, TrajetMissionDTO dto) {
        TrajetMission mission = trajetRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Mission avec l'id " + id + " introuvable"));

        if ("TERMINEE".equals(mission.getStatut())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Impossible de modifier une mission terminée");
        }

        mission.setPointDepart(dto.getPointDepart());
        mission.setDestination(dto.getDestination());
        mission.setDistance(dto.getDistance());
        if (dto.getDateMission() != null) mission.setDateMission(dto.getDateMission());

        return toDTO(trajetRepository.save(mission));
    }

    @Transactional
    public TrajetMissionDTO terminerMission(Long id, Double distanceFinale) {
        TrajetMission mission = trajetRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Mission avec l'id " + id + " introuvable"));

        if (!"EN_COURS".equals(mission.getStatut())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Seules les missions en cours peuvent être terminées");
        }

        if (distanceFinale != null && distanceFinale > 0) {
            mission.setDistance(distanceFinale);
        }
        mission.setStatut("TERMINEE");

        Vehicule vehicule = mission.getVehicule();
        vehicule.setKilometrage(vehicule.getKilometrage() + mission.getDistance().intValue());
        vehicule.setStatut("DISPONIBLE");
        vehiculeRepository.save(vehicule);

        return toDTO(trajetRepository.save(mission));
    }

    @Transactional
    public TrajetMissionDTO annulerMission(Long id) {
        TrajetMission mission = trajetRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Mission avec l'id " + id + " introuvable"));

        if ("TERMINEE".equals(mission.getStatut())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Impossible d'annuler une mission terminée");
        }

        String ancienStatut = mission.getStatut();
        mission.setStatut("ANNULEE");

        if ("EN_COURS".equals(ancienStatut)) {
            Vehicule vehicule = mission.getVehicule();
            vehicule.setStatut("DISPONIBLE");
            vehiculeRepository.save(vehicule);
        }

        return toDTO(trajetRepository.save(mission));
    }

    @Transactional
    public void delete(Long id) {
        TrajetMission mission = trajetRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Mission avec l'id " + id + " introuvable"));

        if ("EN_COURS".equals(mission.getStatut())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Impossible de supprimer une mission en cours");
        }
        trajetRepository.deleteById(id);
    }

    // ========== Mapper ==========

    public TrajetMissionDTO toDTO(TrajetMission t) {
        return TrajetMissionDTO.builder()
                .id(t.getId())
                .vehiculeId(t.getVehicule().getId())
                .vehiculeImmatriculation(t.getVehicule().getImmatriculation())
                .chauffeurId(t.getChauffeur().getId())
                .chauffeurNom(t.getChauffeur().getNom())
                .pointDepart(t.getPointDepart())
                .destination(t.getDestination())
                .distance(t.getDistance())
                .dateMission(t.getDateMission())
                .statut(t.getStatut())
                .build();
    }
}
