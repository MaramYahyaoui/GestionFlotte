package com.flotte.repository;

import com.flotte.entity.Chauffeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChauffeurRepository extends JpaRepository<Chauffeur, Long> {


    List<Chauffeur> findByNomContainingIgnoreCase(String nom);
    boolean existsByPermis(String permis);

    @Query("SELECT c FROM Chauffeur c WHERE c.id NOT IN " +
           "(SELECT t.chauffeur.id FROM TrajetMission t WHERE t.statut = 'EN_COURS')")
    List<Chauffeur> findChauffeursdisponibles();

    @Query("SELECT c FROM Chauffeur c LEFT JOIN c.trajets t GROUP BY c ORDER BY COUNT(t) DESC")
    List<Chauffeur> findChauffeursLesPlasActifs();
}
