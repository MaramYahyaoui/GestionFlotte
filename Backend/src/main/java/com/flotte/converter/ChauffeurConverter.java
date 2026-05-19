package com.flotte.converter;

import com.flotte.dto.ChauffeurDTO;
import com.flotte.entity.Chauffeur;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChauffeurConverter {

    @Autowired
    private ModelMapper modelMapper;

    public ChauffeurDTO toDto(Chauffeur chauffeur) {
        ChauffeurDTO dto = modelMapper.map(chauffeur, ChauffeurDTO.class);
        if (chauffeur.getTrajets() != null) {
            dto.setNombreMissions((long) chauffeur.getTrajets().size());
            dto.setDistanceTotale(
                chauffeur.getTrajets().stream()
                    .mapToDouble(t -> t.getDistance() != null ? t.getDistance() : 0)
                    .sum()
            );
        }
        return dto;
    }

    public Chauffeur fromDto(ChauffeurDTO dto) {
        return modelMapper.map(dto, Chauffeur.class);
    }

    public List<ChauffeurDTO> toDtoList(List<Chauffeur> chauffeurs) {
        return chauffeurs.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
