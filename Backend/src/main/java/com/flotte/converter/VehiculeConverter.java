package com.flotte.converter;

import com.flotte.dto.VehiculeDTO;
import com.flotte.entity.Vehicule;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper Vehicule ↔ VehiculeDTO
 * Structure identique au EtudiantConverter du cours (slides 22-24)
 */
@Component
public class VehiculeConverter {

    @Autowired
    private ModelMapper modelMapper;

    // ===== toDto : de l'entité vers le DTO (slide 23) =====
    public VehiculeDTO toDto(Vehicule vehicule) {
        // Mapping automatique des attributs de même nom
        VehiculeDTO dto = modelMapper.map(vehicule, VehiculeDTO.class);
        // Mapping des attributs supplémentaires non présents dans l'entité
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

    // ===== fromDto : du DTO vers l'entité (slide 23) =====
    public Vehicule fromDto(VehiculeDTO dto) {
        return modelMapper.map(dto, Vehicule.class);
    }

    // ===== conversion de liste (slide 24) =====
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
