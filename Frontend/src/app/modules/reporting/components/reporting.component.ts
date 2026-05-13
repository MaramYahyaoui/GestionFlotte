import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { forkJoin } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';
import { ReportingService } from '../../../core/services/reporting.service';
import { ConsommationService } from '../../../core/services/consommation.service';
import { VehiculeActif, CoutCarburant } from '../../../core/models/models';

@Component({
  selector: 'app-reporting',
  templateUrl: './reporting.component.html',
  styleUrls: ['./reporting.component.scss'],
  standalone: false,
})
export class ReportingComponent implements OnInit {
  flotteActive: VehiculeActif[] = [];
  couts: CoutCarburant[] = [];
  missionsParMois: Record<string, number> = {};
  statuts: Record<string, number> = {};
  loading = true;
  currentAnnee = new Date().getFullYear();
  annees = [2024, 2025, 2026];

  constructor(
    private reportingService: ReportingService,
    private consoService: ConsommationService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() { this.load(); }

  load() {
    this.loading = true;
    this.cdr.detectChanges();

    forkJoin({
      flotte:   this.reportingService.getFLotteActive().pipe(catchError(() => of([]))),
      couts:    this.consoService.getDashboard().pipe(catchError(() => of([]))),
      missions: this.reportingService.getMissionsParMois(this.currentAnnee).pipe(catchError(() => of({}))),
      statuts:  this.reportingService.getStatuts().pipe(catchError(() => of({}))),
    }).subscribe({
      next: results => {
        this.flotteActive    = results.flotte;
        this.couts           = results.couts;
        this.missionsParMois = results.missions;
        this.statuts         = results.statuts;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  changeAnnee(a: number) {
    this.currentAnnee = a;
    this.reportingService.getMissionsParMois(a).subscribe(m => {
      this.missionsParMois = m;
      this.cdr.detectChanges();
    });
  }

  get missionsEntries() { return Object.entries(this.missionsParMois); }

  getTotalCout(): number {
    return this.couts.reduce((acc, curr) => acc + (curr.coutTotal || 0), 0);
  }

  getPercentage(val: number): number {
    const total = this.getTotalCout();
    return total > 0 ? (val / total) * 100 : 0;
  }

  get maxMissions() {
    const values = Object.values(this.missionsParMois);
    return values.length > 0 ? Math.max(...values, 1) : 1;
  }

  get totalStatuts() {
    return Object.values(this.statuts).reduce((a, b) => a + b, 0);
  }

  getBarHeight(val: number): number {
    return Math.round((val / this.maxMissions) * 100);
  }

  pct(val: number): number {
    return this.totalStatuts > 0 ? Math.round((val / this.totalStatuts) * 100) : 0;
  }

  formatCurrency(val: number): string {
    return new Intl.NumberFormat('fr-TN', {
      minimumFractionDigits: 0,
      maximumFractionDigits: 0
    }).format(val || 0) + ' TND';
  }
}
