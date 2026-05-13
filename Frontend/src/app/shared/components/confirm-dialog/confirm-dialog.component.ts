import { Component } from '@angular/core';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-confirm-dialog',
  template: `
    <div class="overlay" *ngIf="visible" (click)="cancel()">
      <div class="dialog" (click)="$event.stopPropagation()">
        <div class="dialog__icon">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M10.29 3.86L1.82 18a2 2 0 001.71 3h16.94a2 2 0 001.71-3L13.71 3.86a2 2 0 00-3.42 0z"/>
            <line x1="12" y1="9" x2="12" y2="13"/>
            <line x1="12" y1="17" x2="12.01" y2="17"/>
          </svg>
        </div>
        <h3 class="dialog__title">{{ title }}</h3>
        <p class="dialog__message">{{ message }}</p>
        <div class="dialog__actions">
          <button class="btn btn--secondary" (click)="cancel()">Annuler</button>
          <button class="btn btn--danger" (click)="confirm()">Confirmer</button>
        </div>
      </div>
    </div>
  `,
  styleUrls: ['./confirm-dialog.component.scss'],
  standalone: false,
})
export class ConfirmDialogComponent {
  visible = false;
  title = 'Confirmation';
  message = 'Êtes-vous sûr de vouloir effectuer cette action ?';
  private result$ = new Subject<boolean>();

  open(title: string, message: string): Promise<boolean> {
    this.title = title;
    this.message = message;
    this.visible = true;
    return new Promise(resolve => {
      this.result$.subscribe(v => { this.visible = false; resolve(v); });
    });
  }

  confirm() { this.result$.next(true); }
  cancel()  { this.result$.next(false); }
}
