import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  template: `
    <div class="app-shell">
      <app-sidebar></app-sidebar>
      <div class="main-area">
        <app-topbar></app-topbar>
        <main class="content">
          <router-outlet></router-outlet>
        </main>
      </div>
    </div>
    <app-notification-toast></app-notification-toast>
    <app-confirm-dialog></app-confirm-dialog>
  `,
  styleUrls: ['./app.component.scss'],
  standalone: false,
})
export class AppComponent {
  title = 'Fleet Management';
}
