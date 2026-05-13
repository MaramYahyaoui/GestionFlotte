import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './modules/dashbord/components/dashboard.component';
import { VehiculesListComponent } from './modules/vehicules/components/vehicules-list/vehicules-list.component';
import { VehiculeFormComponent } from './modules/vehicules/components/vehicule-form/vehicule-form.component';
import { VehiculeDetailComponent } from './modules/vehicules/components/vehicule-detail/vehicule-detail.component';
import { ChauffeursListComponent } from './modules/chauffeur/components/chauffeurs-list/chauffeurs-list.component';
import { ChauffeurFormComponent } from './modules/chauffeur/components/chauffeur-form/chauffeur-form.component';
import { MissionsListComponent } from './modules/missions/components/missions-list/missions-list.component';
import { MissionFormComponent } from './modules/missions/components/mission-form/mission-form.component';
import { ConsommationsListComponent } from './modules/consommations/components/consommations-list/consommations-list.component';
import { ConsommationFormComponent } from './modules/consommations/components/consommation-form/consommation-form.component';
import { ReportingComponent } from './modules/reporting/components/reporting.component';

const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'vehicules', component: VehiculesListComponent },
  { path: 'vehicules/nouveau', component: VehiculeFormComponent },
  { path: 'vehicules/:id', component: VehiculeDetailComponent },
  { path: 'vehicules/:id/modifier', component: VehiculeFormComponent },
  { path: 'chauffeurs', component: ChauffeursListComponent },
  { path: 'chauffeurs/nouveau', component: ChauffeurFormComponent },
  { path: 'chauffeurs/:id/modifier', component: ChauffeurFormComponent },
  { path: 'missions', component: MissionsListComponent },
  { path: 'missions/nouvelle', component: MissionFormComponent },
  { path: 'missions/:id/modifier', component: MissionFormComponent },
  { path: 'consommations', component: ConsommationsListComponent },
  { path: 'consommations/nouvelle', component: ConsommationFormComponent },
  { path: 'consommations/:id/modifier', component: ConsommationFormComponent },
  { path: 'reporting', component: ReportingComponent },
  { path: '**', redirectTo: 'dashboard' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
