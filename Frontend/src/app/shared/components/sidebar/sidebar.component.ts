import { Component } from '@angular/core';
import { Router } from '@angular/router';

interface NavItem {
  label: string;
  path: string;
  icon: string;
}

@Component({
  selector: 'app-sidebar',
  template: `
    <aside class="sidebar">
      <div class="sidebar__brand">
        <div class="brand-icon">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <rect x="1" y="3" width="15" height="13" rx="2"/>
            <path d="M16 8h4l3 5v3h-7V8z"/>
            <circle cx="5.5" cy="18.5" r="2.5"/>
            <circle cx="18.5" cy="18.5" r="2.5"/>
          </svg>
        </div>
        <div class="brand-text">
          <span class="brand-name">FleetOS</span>
          <span class="brand-sub">Gestion de flotte</span>
        </div>
      </div>

      <nav class="sidebar__nav">
        <div class="nav-section">
          <span class="nav-section__label">Principal</span>
          <a *ngFor="let item of mainItems"
             [routerLink]="item.path"
             routerLinkActive="active"
             class="nav-item">
            <span class="nav-item__icon" [innerHTML]="item.icon"></span>
            <span class="nav-item__label">{{ item.label }}</span>
          </a>
        </div>

        <div class="nav-section">
          <span class="nav-section__label">Gestion</span>
          <a *ngFor="let item of managementItems"
             [routerLink]="item.path"
             routerLinkActive="active"
             class="nav-item">
            <span class="nav-item__icon" [innerHTML]="item.icon"></span>
            <span class="nav-item__label">{{ item.label }}</span>
          </a>
        </div>

        <div class="nav-section">
          <span class="nav-section__label">Analyse</span>
          <a *ngFor="let item of analysisItems"
             [routerLink]="item.path"
             routerLinkActive="active"
             class="nav-item">
            <span class="nav-item__icon" [innerHTML]="item.icon"></span>
            <span class="nav-item__label">{{ item.label }}</span>
          </a>
        </div>
      </nav>

      <div class="sidebar__footer">
        <div class="version-tag">v1.0.0</div>
      </div>
    </aside>
  `,
  styleUrls: ['./sidebar.component.scss'],
  standalone: false,
})
export class SidebarComponent {
  mainItems: NavItem[] = [
    { label: 'Dashboard', path: '/dashboard', icon: svgGrid },
  ];

  managementItems: NavItem[] = [
    { label: 'Véhicules', path: '/vehicules', icon: svgTruck },
    { label: 'Chauffeurs', path: '/chauffeurs', icon: svgUser },
    { label: 'Missions', path: '/missions', icon: svgMap },
    { label: 'Consommations', path: '/consommations', icon: svgFuel },
  ];

  analysisItems: NavItem[] = [
    { label: 'Reporting', path: '/reporting', icon: svgChart },
  ];
}

const svgGrid = `<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="3" width="7" height="7"/><rect x="14" y="3" width="7" height="7"/><rect x="3" y="14" width="7" height="7"/><rect x="14" y="14" width="7" height="7"/></svg>`;
const svgTruck = `<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="1" y="3" width="15" height="13" rx="2"/><path d="M16 8h4l3 5v3h-7V8z"/><circle cx="5.5" cy="18.5" r="2.5"/><circle cx="18.5" cy="18.5" r="2.5"/></svg>`;
const svgUser = `<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>`;
const svgMap = `<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polygon points="3 6 9 3 15 6 21 3 21 18 15 21 9 18 3 21"/><line x1="9" y1="3" x2="9" y2="18"/><line x1="15" y1="6" x2="15" y2="21"/></svg>`;
const svgFuel = `<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M3 22V8l7-5 7 5v14"/><line x1="3" y1="14" x2="17" y2="14"/><path d="M17 5h2a2 2 0 0 1 2 2v3a2 2 0 0 1-2 2h-2"/></svg>`;
const svgChart = `<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="18" y1="20" x2="18" y2="10"/><line x1="12" y1="20" x2="12" y2="4"/><line x1="6" y1="20" x2="6" y2="14"/></svg>`;
