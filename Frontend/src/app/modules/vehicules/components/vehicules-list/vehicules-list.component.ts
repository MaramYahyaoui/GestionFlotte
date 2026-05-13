import { Component, OnInit, ViewChild, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { VehiculeService } from '../../../../core/services/vehicule.service';
import { NotificationService } from '../../../../core/services/notification.service';
import { ConfirmDialogComponent } from '../../../../shared/components/confirm-dialog/confirm-dialog.component';
import { Vehicule } from '../../../../core/models/models';

@Component({
  selector: 'app-vehicules-list',
  templateUrl: './vehicules-list.component.html',
  styleUrls: ['./vehicules-list.component.scss'],
  standalone: false,
})
export class VehiculesListComponent implements OnInit {
  @ViewChild(ConfirmDialogComponent) confirmDialog!: ConfirmDialogComponent;

  vehicules: Vehicule[] = [];
  filtered: Vehicule[] = [];
  loading = true;
  search = '';
  selectedStatut = '';

  constructor(
    private vehiculeService: VehiculeService,
    private notif: NotificationService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() { this.load(); }

  load() {
    this.loading = true;
    this.cdr.detectChanges();
    this.vehiculeService.getAll().subscribe({
      next: v => {
        this.vehicules = v;
        this.applyFilters();
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.notif.error('Erreur lors du chargement des véhicules');
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  applyFilters() {
    this.filtered = this.vehicules.filter(v => {
      const matchSearch = !this.search ||
        v.immatriculation.toLowerCase().includes(this.search.toLowerCase()) ||
        v.modele.toLowerCase().includes(this.search.toLowerCase());
      const matchStatut = !this.selectedStatut || v.statut === this.selectedStatut;
      return matchSearch && matchStatut;
    });
    this.cdr.detectChanges();
  }

  async delete(v: Vehicule) {
    const ok = await this.confirmDialog.open(
      'Supprimer le véhicule',
      `Supprimer le véhicule ${v.immatriculation} (${v.modele}) ?`
    );
    if (!ok) return;
    this.vehiculeService.delete(v.id!).subscribe({
      next: () => { this.notif.success('Véhicule supprimé'); this.load(); },
      error: (e: any) => this.notif.error(e.message)
    });
  }

  goTo(path: string) { this.router.navigate([path]); }
}
