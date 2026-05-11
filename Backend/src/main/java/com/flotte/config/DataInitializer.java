package com.flotte.config;

import com.flotte.entity.Chauffeur;
import com.flotte.entity.Consommation;
import com.flotte.entity.TrajetMission;
import com.flotte.entity.Vehicule;
import com.flotte.repository.ChauffeurRepository;
import com.flotte.repository.ConsommationRepository;
import com.flotte.repository.TrajetMissionRepository;
import com.flotte.repository.VehiculeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    @Bean
    @Profile("dev")
    public CommandLineRunner initData(
            VehiculeRepository vehiculeRepo,
            ChauffeurRepository chauffeurRepo,
            TrajetMissionRepository trajetRepo,
            ConsommationRepository consoRepo) {

        return args -> {
            if (vehiculeRepo.count() > 0) {
                log.info("Données déjà présentes, skip initialisation.");
                return;
            }

            log.info("Initialisation des données de démonstration...");

            // ===== Véhicules =====
            Vehicule v1 = vehiculeRepo.save(Vehicule.builder()
                    .immatriculation("TUN-001-AA").modele("Renault Kangoo")
                    .type("Utilitaire").kilometrage(45200).statut("DISPONIBLE").build());
            Vehicule v2 = vehiculeRepo.save(Vehicule.builder()
                    .immatriculation("TUN-002-BB").modele("Peugeot Expert")
                    .type("Utilitaire").kilometrage(78300).statut("DISPONIBLE").build());
            Vehicule v3 = vehiculeRepo.save(Vehicule.builder()
                    .immatriculation("TUN-003-CC").modele("Ford Transit")
                    .type("Camionnette").kilometrage(120000).statut("EN_MAINTENANCE").build());
            Vehicule v4 = vehiculeRepo.save(Vehicule.builder()
                    .immatriculation("TUN-004-DD").modele("Toyota Hilux")
                    .type("Pick-up").kilometrage(32500).statut("DISPONIBLE").build());

            // ===== Chauffeurs =====
            Chauffeur c1 = chauffeurRepo.save(Chauffeur.builder()
                    .nom("Mohamed Ben Ali").permis("B-12345").experience(8).build());
            Chauffeur c2 = chauffeurRepo.save(Chauffeur.builder()
                    .nom("Ahmed Trabelsi").permis("B-67890").experience(12).build());
            Chauffeur c3 = chauffeurRepo.save(Chauffeur.builder()
                    .nom("Sami Gharbi").permis("C-11223").experience(5).build());

            // ===== Missions =====
            trajetRepo.save(TrajetMission.builder()
                    .vehicule(v1).chauffeur(c1)
                    .pointDepart("Tunis").destination("Sfax")
                    .distance(270.0).dateMission(LocalDate.now().minusDays(5))
                    .statut("TERMINEE").build());
            trajetRepo.save(TrajetMission.builder()
                    .vehicule(v2).chauffeur(c2)
                    .pointDepart("Tunis").destination("Sousse")
                    .distance(142.0).dateMission(LocalDate.now().minusDays(3))
                    .statut("TERMINEE").build());
            trajetRepo.save(TrajetMission.builder()
                    .vehicule(v4).chauffeur(c3)
                    .pointDepart("Tunis").destination("Bizerte")
                    .distance(65.0).dateMission(LocalDate.now())
                    .statut("EN_COURS").build());
            v4.setStatut("EN_MISSION");
            vehiculeRepo.save(v4);

            // ===== Consommations =====
            consoRepo.save(Consommation.builder().vehicule(v1)
                    .date(LocalDate.now().minusDays(10)).quantiteCarburant(50.0).coutTotal(95.0).build());
            consoRepo.save(Consommation.builder().vehicule(v1)
                    .date(LocalDate.now().minusDays(5)).quantiteCarburant(45.0).coutTotal(85.5).build());
            consoRepo.save(Consommation.builder().vehicule(v2)
                    .date(LocalDate.now().minusDays(8)).quantiteCarburant(60.0).coutTotal(114.0).build());
            consoRepo.save(Consommation.builder().vehicule(v2)
                    .date(LocalDate.now().minusDays(3)).quantiteCarburant(55.0).coutTotal(104.5).build());
            consoRepo.save(Consommation.builder().vehicule(v4)
                    .date(LocalDate.now().minusDays(2)).quantiteCarburant(70.0).coutTotal(133.0).build());

            log.info("Données de démonstration initialisées avec succès !");
        };
    }
}
