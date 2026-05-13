import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { filter, map } from 'rxjs/operators';

@Component({
  selector: 'app-topbar',
  template: `
    <header class="topbar">
      <div class="topbar__breadcrumb">
        <span class="breadcrumb-home">FleetOS</span>
        <span class="breadcrumb-sep">/</span>
        <span class="breadcrumb-current">{{ currentPage }}</span>
      </div>
      <div class="topbar__actions">
        <div class="live-indicator">
          <span class="dot"></span>
          Système actif
        </div>
        <div class="clock">{{ currentTime }}</div>
      </div>
    </header>
  `,
  styleUrls: ['./topbar.component.scss'],
  standalone: false,
})
export class TopbarComponent implements OnInit {
  currentPage = 'Dashboard';
  currentTime = '';

  private pageNames: Record<string, string> = {
    'dashboard': 'Dashboard',
    'vehicules': 'Véhicules',
    'chauffeurs': 'Chauffeurs',
    'missions': 'Missions',
    'consommations': 'Consommations',
    'reporting': 'Reporting',
  };

  constructor(private router: Router) {}

  ngOnInit() {
    this.router.events.pipe(
      filter(e => e instanceof NavigationEnd),
      map((e: any) => e.urlAfterRedirects.split('/')[1])
    ).subscribe(seg => {
      this.currentPage = this.pageNames[seg] ?? 'Dashboard';
    });

    this.updateClock();
    setInterval(() => this.updateClock(), 1000);
  }

  private updateClock() {
    this.currentTime = new Date().toLocaleTimeString('fr-FR', {
      hour: '2-digit', minute: '2-digit', second: '2-digit'
    });
  }
}
