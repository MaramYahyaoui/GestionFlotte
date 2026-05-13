import { Component, OnInit } from '@angular/core';
import { NotificationService, Notification } from '../../../core/services/notification.service';

@Component({
  selector: 'app-notification-toast',
  template: `
    <div class="toast-container">
      <div *ngFor="let n of notifications"
           class="toast toast--{{ n.type }}"
           (click)="notifService.remove(n.id)">
        <span class="toast__icon">{{ icons[n.type] }}</span>
        <span class="toast__msg">{{ n.message }}</span>
        <button class="toast__close">✕</button>
      </div>
    </div>
  `,
  styleUrls: ['./notification-toast.component.scss'],
  standalone: false,
})
export class NotificationToastComponent implements OnInit {
  notifications: Notification[] = [];
  icons: Record<string, string> = {
    success: '✓', error: '✕', warning: '⚠', info: 'ℹ'
  };

  constructor(public notifService: NotificationService) {}

  ngOnInit() {
    this.notifService.notifications$.subscribe(n => this.notifications = n);
  }
}
