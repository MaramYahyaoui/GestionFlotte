import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { VehiculeService } from '../../../../core/services/vehicule.service';
import { MissionService } from '../../../../core/services/mission.service';
import { ConsommationService } from '../../../../core/services/consommation.service';
import { NotificationService } from '../../../../core/services/notification.service';
import { Vehicule, TrajetMission, Consommation } from '../../../../core/models/models';

@Component({
  selector: 'app-vehicule-detail',
  templateUrl: './vehicule-detail.component.html',
  styleUrls: ['./vehicule-detail.component.scss'],
  standalone: false,
})
export class VehiculeDetailComponent implements OnInit {
  vehicule: Vehicule | null = null;
  missions: TrajetMission[] = [];
  consommations: Consommation[] = [];
  loading = true;
  activeTab: 'missions' | 'consommations' = 'missions';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private vehiculeService: VehiculeService,
    private missionService: MissionService,
    private consoService: ConsommationService,
    private notif: NotificationService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    const id = +this.route.snapshot.paramMap.get('id')!;
    this.loading = true;
    this.cdr.detectChanges();

    forkJoin({
      vehicule:      this.vehiculeService.getById(id).pipe(catchError(() => of(null))),
      missions:      this.missionService.getAll({ vehiculeId: id }).pipe(catchError(() => of([]))),
      consommations: this.consoService.getAll(id).pipe(catchError(() => of([]))),
    }).subscribe({
      next: (results: any) => {
        if (!results.vehicule) {
          this.notif.error('Véhicule introuvable');
          this.router.navigate(['/vehicules']);
          return;
        }
        this.vehicule      = results.vehicule;
        this.missions      = results.missions;
        this.consommations = results.consommations;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.notif.error('Erreur de chargement');
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  get totalCout(): number {
    return this.consommations.reduce((s, c) => s + (c.coutTotal ?? 0), 0);
  }
  get totalLitres(): number {
    return this.consommations.reduce((s, c) => s + (c.quantiteCarburant ?? 0), 0);
  }
  get totalDistance(): number {
    return this.missions
      .filter(m => m.statut === 'TERMINEE')
      .reduce((s, m) => s + (m.distance ?? 0), 0);
  }
}
