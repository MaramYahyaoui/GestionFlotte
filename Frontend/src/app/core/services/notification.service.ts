import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface Notification {
  id: string;
  type: 'success' | 'error' | 'warning' | 'info';
  message: string;
}

@Injectable({ providedIn: 'root' })
export class NotificationService {
  private notifs$ = new BehaviorSubject<Notification[]>([]);
  notifications$ = this.notifs$.asObservable();

  success(message: string) { this.add('success', message); }
  error(message: string) { this.add('error', message); }
  warning(message: string) { this.add('warning', message); }
  info(message: string) { this.add('info', message); }

  private add(type: Notification['type'], message: string) {
    const id = Math.random().toString(36).slice(2);
    const current = this.notifs$.getValue();
    this.notifs$.next([...current, { id, type, message }]);
    setTimeout(() => this.remove(id), 4000);
  }

  remove(id: string) {
    this.notifs$.next(this.notifs$.getValue().filter(n => n.id !== id));
  }
}
