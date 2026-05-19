package com.flotte.converter;

import com.flotte.dto.ConsommationDTO;
import com.flotte.entity.Consommation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConsommationConverter {

    @Autowired
    private ModelMapper modelMapper;

    public ConsommationDTO toDto(Consommation consommation) {
        ConsommationDTO dto = modelMapper.map(consommation, ConsommationDTO.class);
        dto.setVehiculeId(consommation.getVehicule().getId());
        dto.setVehiculeImmatriculation(consommation.getVehicule().getImmatriculation());
        return dto;
    }

    public Consommation fromDto(ConsommationDTO dto) {
        return modelMapper.map(dto, Consommation.class);
    }

    public List<ConsommationDTO> toDtoList(List<Consommation> consommations) {
        return consommations.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
