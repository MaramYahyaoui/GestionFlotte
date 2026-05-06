package com.flotte.repository;

import com.flotte.entity.Consommation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ConsommationRepository extends JpaRepository<Consommation, Long> {

    // Méthodes dérivées
    List<Consommation> findByVehiculeId(Long vehiculeId);
    List<Consommation> findByDateBetween(LocalDate debut, LocalDate fin);
    List<Consommation> findByVehiculeIdAndDateBetween(Long vehiculeId, LocalDate debut, LocalDate fin);

    // Requêtes JPQL
    @Query("SELECT c.vehicule.id, SUM(c.coutTotal) FROM Consommation c GROUP BY c.vehicule.id ORDER BY SUM(c.coutTotal) DESC")
    List<Object[]> sumCoutParVehicule();

    @Query("SELECT YEAR(c.date), MONTH(c.date), SUM(c.coutTotal), SUM(c.quantiteCarburant) " +
           "FROM Consommation c WHERE c.vehicule.id = :vehiculeId " +
           "GROUP BY YEAR(c.date), MONTH(c.date) ORDER BY YEAR(c.date), MONTH(c.date)")
    List<Object[]> consommationMensuelleParVehicule(Long vehiculeId);

    @Query("SELECT SUM(c.coutTotal) FROM Consommation c")
    Double sumCoutTotal();
}
