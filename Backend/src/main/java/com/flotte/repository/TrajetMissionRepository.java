package com.flotte.repository;

import com.flotte.entity.TrajetMission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TrajetMissionRepository extends JpaRepository<TrajetMission, Long> {

    // Méthodes dérivées
    List<TrajetMission> findByStatut(String statut);
    List<TrajetMission> findByVehiculeId(Long vehiculeId);
    List<TrajetMission> findByChauffeurId(Long chauffeurId);
    List<TrajetMission> findByDateMissionBetween(LocalDate debut, LocalDate fin);

    // Requêtes JPQL
    @Query("SELECT COUNT(t) > 0 FROM TrajetMission t WHERE t.vehicule.id = :vehiculeId AND t.statut = 'EN_COURS'")
    boolean vehiculeEnMission(Long vehiculeId);

    @Query("SELECT COUNT(t) > 0 FROM TrajetMission t WHERE t.chauffeur.id = :chauffeurId AND t.statut = 'EN_COURS'")
    boolean chauffeurAMissionEnCours(Long chauffeurId);

    @Query("SELECT t.vehicule.id, COUNT(t) FROM TrajetMission t GROUP BY t.vehicule.id")
    List<Object[]> countMissionsParVehicule();

    @Query("SELECT t.vehicule.id, SUM(t.distance) FROM TrajetMission t GROUP BY t.vehicule.id")
    List<Object[]> sumDistanceParVehicule();
}
