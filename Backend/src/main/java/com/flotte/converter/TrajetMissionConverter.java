package com.flotte.converter;

import com.flotte.dto.TrajetMissionDTO;
import com.flotte.entity.TrajetMission;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TrajetMissionConverter {

    @Autowired
    private ModelMapper modelMapper;

    public TrajetMissionDTO toDto(TrajetMission trajet) {
        TrajetMissionDTO dto = modelMapper.map(trajet, TrajetMissionDTO.class);
        // Attributs supplémentaires issus des relations
        dto.setVehiculeId(trajet.getVehicule().getId());
        dto.setVehiculeImmatriculation(trajet.getVehicule().getImmatriculation());
        dto.setChauffeurId(trajet.getChauffeur().getId());
        dto.setChauffeurNom(trajet.getChauffeur().getNom());
        return dto;
    }

    public TrajetMission fromDto(TrajetMissionDTO dto) {
        return modelMapper.map(dto, TrajetMission.class);
    }

    public List<TrajetMissionDTO> toDtoList(List<TrajetMission> trajets) {
        return trajets.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
