import { Component, OnInit, ViewChild, ChangeDetectorRef } from '@angular/core';
import { ConsommationService } from '../../../../core/services/consommation.service';
import { NotificationService } from '../../../../core/services/notification.service';
import { ConfirmDialogComponent } from '../../../../shared/components/confirm-dialog/confirm-dialog.component';
import { Consommation } from '../../../../core/models/models';

@Component({
  selector: 'app-consommations-list',
  templateUrl: './consommations-list.component.html',
  standalone: false,
})
export class ConsommationsListComponent implements OnInit {
  @ViewChild(ConfirmDialogComponent) confirmDialog!: ConfirmDialogComponent;
  consommations: Consommation[] = [];
  filtered: Consommation[] = [];
  loading = true;
  search = '';

  get totalCout(): number {
    return this.filtered.reduce((s, c) => s + c.coutTotal, 0);
  }
  get totalLitres(): number {
    return this.filtered.reduce((s, c) => s + c.quantiteCarburant, 0);
  }

  constructor(
    private service: ConsommationService,
    private notif: NotificationService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() { this.load(); }

  load() {
    this.loading = true;
    this.cdr.detectChanges();
    this.service.getAll().subscribe({
      next: c => {
        this.consommations = c;
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
    const s = this.search.toLowerCase();
    this.filtered = this.consommations.filter(c =>
      !s || c.vehiculeImmatriculation?.toLowerCase().includes(s)
    );
    this.cdr.detectChanges();
  }

  async delete(c: Consommation) {
    const ok = await this.confirmDialog.open('Supprimer', `Supprimer cette consommation ?`);
    if (!ok) return;
    this.service.delete(c.id!).subscribe({
      next: () => { this.notif.success('Consommation supprimée'); this.load(); },
      error: (e: any) => this.notif.error(e.message)
    });
  }
}
