import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-stat-card',
  template: `
    <div class="stat-card" [class]="'stat-card--' + color">
      <div class="stat-card__icon" [innerHTML]="icon"></div>
      <div class="stat-card__body">
        <div class="stat-card__value">{{ value }}</div>
        <div class="stat-card__label">{{ label }}</div>
      </div>
    </div>
  `,
  styleUrls: ['./stat-card.component.scss'],
  standalone: false,
})
export class StatCardComponent {
  @Input() label = '';
  @Input() value: string | number = 0;
  @Input() icon = '';
  @Input() color: 'blue' | 'green' | 'yellow' | 'red' | 'default' = 'default';
}
