package com.flotte.service;

import com.flotte.dto.ConsommationDTO;
import com.flotte.entity.Consommation;
import com.flotte.entity.Vehicule;
import com.flotte.repository.ConsommationRepository;
import com.flotte.repository.VehiculeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConsommationService {

    @Autowired
    private ConsommationRepository consommationRepository;

    @Autowired
    private VehiculeRepository vehiculeRepository;

    // Retourner toutes les consommations
    public List<ConsommationDTO> findAll() {
        return consommationRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Retourner une consommation par id — Optional comme dans le cours
    public Optional<ConsommationDTO> findById(Long id) {
        return consommationRepository.findById(id).map(this::toDTO);
    }

    // Retourner les consommations d'un vehicule
    public List<ConsommationDTO> findByVehicule(Long vehiculeId) {
        return consommationRepository.findByVehiculeId(vehiculeId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Créer une consommation
    public ConsommationDTO save(ConsommationDTO dto) {
        Vehicule vehicule = vehiculeRepository.findById(dto.getVehiculeId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Véhicule avec l'id " + dto.getVehiculeId() + " introuvable"));

        Consommation consommation = Consommation.builder()
                .vehicule(vehicule)
                .date(dto.getDate() != null ? dto.getDate() : LocalDate.now())
                .quantiteCarburant(dto.getQuantiteCarburant())
                .coutTotal(dto.getCoutTotal())
                .build();

        return toDTO(consommationRepository.save(consommation));
    }

    // Mettre à jour une consommation — ifPresentOrElse
    public ConsommationDTO update(Long id, ConsommationDTO dto) {
        final ConsommationDTO[] result = {null};
        consommationRepository.findById(id).ifPresentOrElse(
                consommation -> {
                    consommation.setDate(dto.getDate());
                    consommation.setQuantiteCarburant(dto.getQuantiteCarburant());
                    consommation.setCoutTotal(dto.getCoutTotal());
                    result[0] = toDTO(consommationRepository.save(consommation));
                },
                () -> { throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Consommation avec l'id " + id + " introuvable"); }
        );
        return result[0];
    }

    // Supprimer une consommation — ifPresentOrElse
    public void delete(Long id) {
        consommationRepository.findById(id).ifPresentOrElse(
                consommation -> consommationRepository.deleteById(id),
                () -> { throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Consommation avec l'id " + id + " introuvable"); }
        );
    }

    // Coût total global
    public Double getCoutTotalGlobal() {
        Double total = consommationRepository.sumCoutTotal();
        return total != null ? total : 0.0;
    }

    // Mapper
    public ConsommationDTO toDTO(Consommation c) {
        return ConsommationDTO.builder()
                .id(c.getId())
                .vehiculeId(c.getVehicule().getId())
                .vehiculeImmatriculation(c.getVehicule().getImmatriculation())
                .date(c.getDate())
                .quantiteCarburant(c.getQuantiteCarburant())
                .coutTotal(c.getCoutTotal())
                .build();
    }
}
