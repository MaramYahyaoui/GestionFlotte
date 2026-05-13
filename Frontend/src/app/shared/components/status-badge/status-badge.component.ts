import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-status-badge',
  template: `<span class="badge badge--{{ cssClass }}">{{ label }}</span>`,
  standalone: false,
})
export class StatusBadgeComponent {
  @Input() set status(val: string) {
    const map: Record<string, { label: string; css: string }> = {
      'DISPONIBLE':    { label: 'Disponible',     css: 'disponible' },
      'EN_MISSION':    { label: 'En mission',      css: 'en-mission' },
      'EN_MAINTENANCE':{ label: 'Maintenance',     css: 'maintenance' },
      'EN_PANNE':      { label: 'En panne',        css: 'en-panne' },
      'EN_COURS':      { label: 'En cours',        css: 'en-cours' },
      'TERMINEE':      { label: 'Terminée',        css: 'terminee' },
      'ANNULEE':       { label: 'Annulée',         css: 'annulee' },
      'CRITIQUE':      { label: 'Critique',        css: 'critique' },
      'ATTENTION':     { label: 'Attention',       css: 'attention' },
      'INFO':          { label: 'Info',            css: 'info' },
    };
    const entry = map[val] ?? { label: val, css: 'default' };
    this.label = entry.label;
    this.cssClass = entry.css;
  }
  label = '';
  cssClass = 'default';
}
