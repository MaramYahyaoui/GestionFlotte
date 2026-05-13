import { Component, OnInit, ViewChild, ChangeDetectorRef } from '@angular/core';
import { MissionService } from '../../../../core/services/mission.service';
import { NotificationService } from '../../../../core/services/notification.service';
import { ConfirmDialogComponent } from '../../../../shared/components/confirm-dialog/confirm-dialog.component';
import { TrajetMission } from '../../../../core/models/models';

@Component({
  selector: 'app-missions-list',
  templateUrl: './missions-list.component.html',
  styleUrls: ['./missions-list.component.scss'],
  standalone: false,
})
export class MissionsListComponent implements OnInit {
  @ViewChild(ConfirmDialogComponent) confirmDialog!: ConfirmDialogComponent;
  missions: TrajetMission[] = [];
  filtered: TrajetMission[] = [];
  loading = true;
  search = '';
  selectedStatut = '';

  constructor(
    private service: MissionService,
    private notif: NotificationService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() { this.load(); }

  load() {
    this.loading = true;
    this.cdr.detectChanges();
    this.service.getAll().subscribe({
      next: m => {
        this.missions = m;
        this.applyFilters();
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.notif.error('Erreur de chargement');
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  applyFilters() {
    this.filtered = this.missions.filter(m => {
      const s = this.search.toLowerCase();
      const matchSearch = !s ||
        m.vehiculeImmatriculation?.toLowerCase().includes(s) ||
        m.chauffeurNom?.toLowerCase().includes(s) ||
        m.pointDepart?.toLowerCase().includes(s) ||
        m.destination?.toLowerCase().includes(s);
      const matchStatut = !this.selectedStatut || m.statut === this.selectedStatut;
      return matchSearch && matchStatut;
    });
    this.cdr.detectChanges();
  }

  async terminer(m: TrajetMission) {
    const ok = await this.confirmDialog.open(
      'Terminer la mission',
      `Terminer la mission ${m.vehiculeImmatriculation} → ${m.destination} ?`
    );
    if (!ok) return;
    this.service.terminer(m.id!).subscribe({
      next: () => { this.notif.success('Mission terminée'); this.load(); },
      error: (e: any) => this.notif.error(e.message)
    });
  }

  async annuler(m: TrajetMission) {
    const ok = await this.confirmDialog.open(
      'Annuler la mission',
      `Annuler la mission vers ${m.destination} ?`
    );
    if (!ok) return;
    this.service.annuler(m.id!).subscribe({
      next: () => { this.notif.success('Mission annulée'); this.load(); },
      error: (e: any) => this.notif.error(e.message)
    });
  }

  async delete(m: TrajetMission) {
    const ok = await this.confirmDialog.open('Supprimer', `Supprimer la mission #${m.id} ?`);
    if (!ok) return;
    this.service.delete(m.id!).subscribe({
      next: () => { this.notif.success('Mission supprimée'); this.load(); },
      error: (e: any) => this.notif.error(e.message)
    });
  }
}
