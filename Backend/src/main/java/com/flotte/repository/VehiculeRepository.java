package com.flotte.repository;

import com.flotte.entity.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculeRepository extends JpaRepository<Vehicule, Long> {

    Optional<Vehicule> findByImmatriculation(String immatriculation);
    List<Vehicule> findByStatut(String statut);
    boolean existsByImmatriculation(String immatriculation);

    @Query("SELECT v FROM Vehicule v WHERE v.statut IN ('DISPONIBLE', 'EN_MISSION')")
    List<Vehicule> findVehiculesActifs();

    @Query("SELECT v FROM Vehicule v WHERE v.kilometrage >= :seuil")
    List<Vehicule> findVehiculesPourMaintenance(Integer seuil);

    @Query("SELECT v FROM Vehicule v LEFT JOIN v.trajets t GROUP BY v ORDER BY COUNT(t) DESC")
    List<Vehicule> findVehiculesLasPlusActifs();
}
