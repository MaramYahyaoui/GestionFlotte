import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { provideHttpClient, withInterceptorsFromDi, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule, DecimalPipe, DatePipe } from '@angular/common';
import { RouterModule } from '@angular/router';

import { AppRoutingModule } from './app_routing.module';
import { AppComponent } from './app.component';
import { ApiInterceptor } from './core/interceptors/api.interceptor';

// Shared Components
import { SidebarComponent } from './shared/components/sidebar/sidebar.component';
import { TopbarComponent } from './shared/components/topbar/topbar.component';
import { NotificationToastComponent } from './shared/components/notification-toast/notification-toast.component';
import { StatCardComponent } from './shared/components/stat-card/stat-card.component';
import { ConfirmDialogComponent } from './shared/components/confirm-dialog/confirm-dialog.component';
import { StatusBadgeComponent } from './shared/components/status-badge/status-badge.component';
import { LoadingSpinnerComponent } from './shared/components/loading-spinner/loading-spinner.component';

// Dashboard
import { DashboardComponent } from './modules/dashbord/components/dashboard.component';

// Véhicules
import { VehiculesListComponent } from './modules/vehicules/components/vehicules-list/vehicules-list.component';
import { VehiculeFormComponent } from './modules/vehicules/components/vehicule-form/vehicule-form.component';
import { VehiculeDetailComponent } from './modules/vehicules/components/vehicule-detail/vehicule-detail.component';

// Chauffeurs
import { ChauffeursListComponent } from './modules/chauffeur/components/chauffeurs-list/chauffeurs-list.component';
import { ChauffeurFormComponent } from './modules/chauffeur/components/chauffeur-form/chauffeur-form.component';

// Missions
import { MissionsListComponent } from './modules/missions/components/missions-list/missions-list.component';
import { MissionFormComponent } from './modules/missions/components/mission-form/mission-form.component';

// Consommations
import { ConsommationsListComponent } from './modules/consommations/components/consommations-list/consommations-list.component';
import { ConsommationFormComponent } from './modules/consommations/components/consommation-form/consommation-form.component';

// Reporting
import { ReportingComponent } from './modules/reporting/components/reporting.component';

@NgModule({
  declarations: [
    AppComponent,
    SidebarComponent,
    TopbarComponent,
    NotificationToastComponent,
    StatCardComponent,
    ConfirmDialogComponent,
    StatusBadgeComponent,
    LoadingSpinnerComponent,
    DashboardComponent,
    VehiculesListComponent,
    VehiculeFormComponent,
    VehiculeDetailComponent,
    ChauffeursListComponent,
    ChauffeurFormComponent,
    MissionsListComponent,
    MissionFormComponent,
    ConsommationsListComponent,
    ConsommationFormComponent,
    ReportingComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    CommonModule,
    RouterModule,
    AppRoutingModule,
  ],
  providers: [
    // ✅ Remplace HttpClientModule — compatible Angular 18+
    provideHttpClient(withInterceptorsFromDi()),
    { provide: HTTP_INTERCEPTORS, useClass: ApiInterceptor, multi: true },
    DecimalPipe,
    DatePipe,
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}