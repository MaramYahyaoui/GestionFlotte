package com.flotte.service;

import com.flotte.converter.ChauffeurConverter;
import com.flotte.dto.ChauffeurDTO;
import com.flotte.entity.Chauffeur;
import com.flotte.repository.ChauffeurRepository;
import com.flotte.repository.TrajetMissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ChauffeurService {

    @Autowired
    private ChauffeurRepository chauffeurRepository;

    @Autowired
    private TrajetMissionRepository trajetRepository;

    @Autowired
    private ChauffeurConverter chauffeurConverter;

    public List<ChauffeurDTO> findAll() {
        return chauffeurConverter.toDtoList(chauffeurRepository.findAll());
    }

    public Optional<ChauffeurDTO> findById(Long id) {
        return chauffeurRepository.findById(id)
                .map(chauffeurConverter::toDto);
    }

    public List<ChauffeurDTO> findByNom(String nom) {
        return chauffeurConverter.toDtoList(chauffeurRepository.findByNomContainingIgnoreCase(nom));
    }

    public List<ChauffeurDTO> findDisponibles() {
        return chauffeurConverter.toDtoList(chauffeurRepository.findChauffeursdisponibles());
    }

    public List<ChauffeurDTO> findLesPlasActifs() {
        return chauffeurConverter.toDtoList(chauffeurRepository.findChauffeursLesPlasActifs());
    }

    public ChauffeurDTO save(ChauffeurDTO dto) {
        if (chauffeurRepository.existsByPermis(dto.getPermis())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Un chauffeur avec le permis " + dto.getPermis() + " existe déjà");
        }
        return chauffeurConverter.toDto(chauffeurRepository.save(chauffeurConverter.fromDto(dto)));
    }

    public ChauffeurDTO update(Long id, ChauffeurDTO dto) {
        final ChauffeurDTO[] result = {null};
        chauffeurRepository.findById(id).ifPresentOrElse(
                chauffeur -> {
                    chauffeur.setNom(dto.getNom());
                    chauffeur.setPermis(dto.getPermis());
                    chauffeur.setExperience(dto.getExperience());
                    result[0] = chauffeurConverter.toDto(chauffeurRepository.save(chauffeur));
                },
                () -> { throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Chauffeur avec l'id " + id + " introuvable"); }
        );
        return result[0];
    }

    public void delete(Long id) {
        chauffeurRepository.findById(id).ifPresentOrElse(
                chauffeur -> {
                    if (trajetRepository.chauffeurAMissionEnCours(id)) {
                        throw new ResponseStatusException(
                                HttpStatus.CONFLICT,
                                "Impossible de supprimer un chauffeur ayant une mission en cours");
                    }
                    chauffeurRepository.deleteById(id);
                },
                () -> { throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Chauffeur avec l'id " + id + " introuvable"); }
        );
    }
}
