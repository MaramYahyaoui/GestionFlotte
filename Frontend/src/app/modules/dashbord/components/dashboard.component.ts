import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ReportingService } from '../../../core/services/reporting.service';
import { DashboardData, VehiculeActif } from '../../../core/models/models';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
  standalone: false,
})
export class DashboardComponent implements OnInit {
  data: DashboardData | null = null;
  top5: VehiculeActif[] = [];
  loading = true;

  icons = {
    truck: `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="1" y="3" width="15" height="13" rx="2"/><path d="M16 8h4l3 5v3h-7V8z"/><circle cx="5.5" cy="18.5" r="2.5"/><circle cx="18.5" cy="18.5" r="2.5"/></svg>`,
    check: `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="20 6 9 17 4 12"/></svg>`,
    route: `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polygon points="3 6 9 3 15 6 21 3 21 18 15 21 9 18 3 21"/><line x1="9" y1="3" x2="9" y2="18"/><line x1="15" y1="6" x2="15" y2="21"/></svg>`,
    fuel:  `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M3 22V8l7-5 7 5v14"/><line x1="3" y1="14" x2="17" y2="14"/></svg>`,
    users: `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/></svg>`,
    alert: `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M10.29 3.86L1.82 18a2 2 0 001.71 3h16.94a2 2 0 001.71-3L13.71 3.86a2 2 0 00-3.42 0z"/><line x1="12" y1="9" x2="12" y2="13"/><line x1="12" y1="17" x2="12.01" y2="17"/></svg>`,
  };

  constructor(
    private reportingService: ReportingService,
    private cdr: ChangeDetectorRef  // ← AJOUT
  ) {}

  ngOnInit() { this.load(); }

  load() {
    this.loading = true;
    this.data = null;
    this.cdr.detectChanges(); // ← forcer affichage spinner

    this.reportingService.getDashboard().subscribe({
      next: d => {
        this.data = d;
        this.loading = false;
        this.cdr.detectChanges(); // ← forcer mise à jour du template
      },
      error: (e) => {
        console.error('Erreur dashboard:', e);
        this.loading = false;
        this.cdr.detectChanges(); // ← forcer mise à jour même en erreur
      }
    });

    this.reportingService.getTop5().subscribe({
      next: v => {
        this.top5 = v;
        this.cdr.detectChanges();
      },
      error: () => {}
    });
  }

  formatCurrency(val: number): string {
    return new Intl.NumberFormat('fr-TN', {
      style: 'currency', currency: 'TND', maximumFractionDigits: 0
    }).format(val);
  }

  formatDistance(val: number): string {
    return new Intl.NumberFormat('fr-FR').format(Math.round(val)) + ' km';
  }
}