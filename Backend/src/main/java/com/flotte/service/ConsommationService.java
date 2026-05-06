package com.flotte.service;

import com.flotte.dto.ConsommationDTO;
import com.flotte.dto.ConsommationMensuelleDTO;
import com.flotte.dto.CoutCarburantDTO;
import com.flotte.entity.Consommation;
import com.flotte.entity.Vehicule;
import com.flotte.repository.ConsommationRepository;
import com.flotte.repository.VehiculeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ConsommationService {

    @Autowired
    private ConsommationRepository consommationRepository;

    @Autowired
    private VehiculeRepository vehiculeRepository;

    public List<ConsommationDTO> findAll() {
        return consommationRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ConsommationDTO findById(Long id) {
        return consommationRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Consommation avec l'id " + id + " introuvable"));
    }

    public List<ConsommationDTO> findByVehicule(Long vehiculeId) {
        if (!vehiculeRepository.existsById(vehiculeId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Véhicule avec l'id " + vehiculeId + " introuvable");
        }
        return consommationRepository.findByVehiculeId(vehiculeId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ConsommationDTO> findByPeriode(LocalDate debut, LocalDate fin) {
        return consommationRepository.findByDateBetween(debut, fin).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ConsommationDTO> findByVehiculeEtPeriode(Long vehiculeId, LocalDate debut, LocalDate fin) {
        if (!vehiculeRepository.existsById(vehiculeId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Véhicule avec l'id " + vehiculeId + " introuvable");
        }
        return consommationRepository.findByVehiculeIdAndDateBetween(vehiculeId, debut, fin).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ConsommationDTO create(ConsommationDTO dto) {
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

    @Transactional
    public ConsommationDTO update(Long id, ConsommationDTO dto) {
        Consommation consommation = consommationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Consommation avec l'id " + id + " introuvable"));

        consommation.setDate(dto.getDate());
        consommation.setQuantiteCarburant(dto.getQuantiteCarburant());
        consommation.setCoutTotal(dto.getCoutTotal());

        if (dto.getVehiculeId() != null
                && !dto.getVehiculeId().equals(consommation.getVehicule().getId())) {
            Vehicule vehicule = vehiculeRepository.findById(dto.getVehiculeId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Véhicule avec l'id " + dto.getVehiculeId() + " introuvable"));
            consommation.setVehicule(vehicule);
        }

        return toDTO(consommationRepository.save(consommation));
    }

    @Transactional
    public void delete(Long id) {
        if (!consommationRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Consommation avec l'id " + id + " introuvable");
        }
        consommationRepository.deleteById(id);
    }

    // ========== Dashboard carburant ==========

    public List<CoutCarburantDTO> getDashboardCouts() {
        List<Object[]> results = consommationRepository.sumCoutParVehicule();
        List<CoutCarburantDTO> dtos = new ArrayList<>();

        for (Object[] row : results) {
            Long vehiculeId = (Long) row[0];
            Double coutTotal = row[1] != null ? ((Number) row[1]).doubleValue() : 0.0;

            vehiculeRepository.findById(vehiculeId).ifPresent(v -> {
                List<Consommation> pleins = consommationRepository.findByVehiculeId(vehiculeId);
                double quantiteTotale = pleins.stream()
                        .mapToDouble(c -> c.getQuantiteCarburant() != null ? c.getQuantiteCarburant() : 0)
                        .sum();

                dtos.add(CoutCarburantDTO.builder()
                        .vehiculeId(vehiculeId)
                        .immatriculation(v.getImmatriculation())
                        .modele(v.getModele())
                        .coutTotal(coutTotal)
                        .quantiteTotale(quantiteTotale)
                        .nombrePleins((long) pleins.size())
                        .build());
            });
        }
        return dtos;
    }

    public List<ConsommationMensuelleDTO> getConsommationMensuelle(Long vehiculeId) {
        if (!vehiculeRepository.existsById(vehiculeId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Véhicule avec l'id " + vehiculeId + " introuvable");
        }
        List<Object[]> results = consommationRepository.consommationMensuelleParVehicule(vehiculeId);
        return results.stream().map(row -> {
            int annee = ((Number) row[0]).intValue();
            int mois  = ((Number) row[1]).intValue();
            String libelle = Month.of(mois).getDisplayName(TextStyle.FULL, Locale.FRENCH);
            return ConsommationMensuelleDTO.builder()
                    .annee(annee)
                    .mois(mois)
                    .moisLibelle(libelle + " " + annee)
                    .coutTotal(row[2] != null ? ((Number) row[2]).doubleValue() : 0.0)
                    .quantiteTotale(row[3] != null ? ((Number) row[3]).doubleValue() : 0.0)
                    .build();
        }).collect(Collectors.toList());
    }

    public Double getCoutTotalGlobal() {
        Double total = consommationRepository.sumCoutTotal();
        return total != null ? total : 0.0;
    }

    // ========== Mapper ==========

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
