package com.flotte.entity;

import java.util.List;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chauffeur")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chauffeur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(name = "numero_permis", unique = true, nullable = false, length = 50)
    private String permis;

    @Column(nullable = false)
    private Integer experience;

    @OneToMany(mappedBy = "chauffeur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TrajetMission> trajets;
}
