import { Component } from '@angular/core';

@Component({
  selector: 'app-loading-spinner',
  template: `
    <div class="spinner-wrap">
      <div class="spinner"></div>
      <span>Chargement...</span>
    </div>
  `,
  styles: [`
    .spinner-wrap {
      display: flex; flex-direction: column;
      align-items: center; justify-content: center;
      padding: 60px; gap: 14px;
      color: var(--text-muted); font-size: 0.85rem;
    }
    .spinner {
      width: 32px; height: 32px;
      border: 2px solid var(--border-light);
      border-top-color: var(--accent);
      border-radius: 50%;
      animation: spin 0.7s linear infinite;
    }
    @keyframes spin { to { transform: rotate(360deg); } }
  `],
  standalone: false,
})
export class LoadingSpinnerComponent {}
