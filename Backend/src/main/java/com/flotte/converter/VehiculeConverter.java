package com.flotte.converter;

import com.flotte.dto.VehiculeDTO;
import com.flotte.entity.Vehicule;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class VehiculeConverter {

    @Autowired
    private ModelMapper modelMapper;

    public VehiculeDTO toDto(Vehicule vehicule) {
        VehiculeDTO dto = modelMapper.map(vehicule, VehiculeDTO.class);
        if (vehicule.getTrajets() != null) {
            dto.setNombreMissions((long) vehicule.getTrajets().size());
            dto.setDistanceTotale(
                vehicule.getTrajets().stream()
                    .mapToDouble(t -> t.getDistance() != null ? t.getDistance() : 0)
                    .sum()
            );
        }
        if (vehicule.getConsommations() != null) {
            dto.setCoutCarburantTotal(
                vehicule.getConsommations().stream()
                    .mapToDouble(c -> c.getCoutTotal() != null ? c.getCoutTotal() : 0)
                    .sum()
            );
        }
        return dto;
    }

    public Vehicule fromDto(VehiculeDTO dto) {
        return modelMapper.map(dto, Vehicule.class);
    }

    public List<VehiculeDTO> toDtoList(List<Vehicule> vehicules) {
        return vehicules.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<Vehicule> fromDtoList(List<VehiculeDTO> dtos) {
        return dtos.stream()
                .map(this::fromDto)
                .collect(Collectors.toList());
    }
}
