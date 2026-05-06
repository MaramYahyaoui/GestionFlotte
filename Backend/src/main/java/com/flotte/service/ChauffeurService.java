package com.flotte.service;

import com.flotte.converter.ChauffeurConverter;
import com.flotte.dto.ChauffeurDTO;
import com.flotte.entity.Chauffeur;
import com.flotte.repository.ChauffeurRepository;
import com.flotte.repository.TrajetMissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ChauffeurService {

    @Autowired
    private ChauffeurRepository chauffeurRepository;

    @Autowired
    private TrajetMissionRepository trajetRepository;

    @Autowired
    private ChauffeurConverter chauffeurConverter;  // injection du mapper (slide 25)

    public List<ChauffeurDTO> findAll() {
        return chauffeurConverter.toDtoList(chauffeurRepository.findAll());
    }

    public ChauffeurDTO findById(Long id) {
        Chauffeur chauffeur = chauffeurRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Chauffeur avec l'id " + id + " introuvable"));
        return chauffeurConverter.toDto(chauffeur);
    }

    public List<ChauffeurDTO> findDisponibles() {
        return chauffeurConverter.toDtoList(chauffeurRepository.findChauffeursdisponibles());
    }

    public List<ChauffeurDTO> findLesPlasActifs() {
        return chauffeurConverter.toDtoList(chauffeurRepository.findChauffeursLesPlasActifs());
    }

    public List<ChauffeurDTO> findByNom(String nom) {
        return chauffeurConverter.toDtoList(chauffeurRepository.findByNomContainingIgnoreCase(nom));
    }

    @Transactional
    public ChauffeurDTO create(ChauffeurDTO dto) {
        if (chauffeurRepository.existsByPermis(dto.getPermis())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Un chauffeur avec le permis " + dto.getPermis() + " existe déjà");
        }
        return chauffeurConverter.toDto(chauffeurRepository.save(chauffeurConverter.fromDto(dto)));
    }

    @Transactional
    public ChauffeurDTO update(Long id, ChauffeurDTO dto) {
        Chauffeur chauffeur = chauffeurRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Chauffeur avec l'id " + id + " introuvable"));

        if (!chauffeur.getPermis().equals(dto.getPermis())
                && chauffeurRepository.existsByPermis(dto.getPermis())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Le numéro de permis " + dto.getPermis() + " est déjà utilisé");
        }
        chauffeur.setNom(dto.getNom());
        chauffeur.setPermis(dto.getPermis());
        chauffeur.setExperience(dto.getExperience());
        return chauffeurConverter.toDto(chauffeurRepository.save(chauffeur));
    }

    @Transactional
    public void delete(Long id) {
        if (!chauffeurRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Chauffeur avec l'id " + id + " introuvable");
        }
        if (trajetRepository.chauffeurAMissionEnCours(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Impossible de supprimer un chauffeur ayant une mission en cours");
        }
        chauffeurRepository.deleteById(id);
    }
}
