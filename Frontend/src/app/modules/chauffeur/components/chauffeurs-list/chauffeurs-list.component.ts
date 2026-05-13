import { Component, OnInit, ViewChild, ChangeDetectorRef } from '@angular/core';
import { ChauffeurService } from '../../../../core/services/chauffeur.service';
import { NotificationService } from '../../../../core/services/notification.service';
import { ConfirmDialogComponent } from '../../../../shared/components/confirm-dialog/confirm-dialog.component';
import { Chauffeur } from '../../../../core/models/models';

@Component({
  selector: 'app-chauffeurs-list',
  templateUrl: './chauffeurs-list.component.html',
  standalone: false,
})
export class ChauffeursListComponent implements OnInit {
  @ViewChild(ConfirmDialogComponent) confirmDialog!: ConfirmDialogComponent;
  chauffeurs: Chauffeur[] = [];
  filtered: Chauffeur[] = [];
  loading = true;
  search = '';

  constructor(
    private service: ChauffeurService,
    private notif: NotificationService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() { this.load(); }

  load() {
    this.loading = true;
    this.cdr.detectChanges();
    this.service.getAll().subscribe({
      next: c => {
        this.chauffeurs = c;
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
    this.filtered = this.chauffeurs.filter(c =>
      !this.search || c.nom.toLowerCase().includes(this.search.toLowerCase()) ||
      c.permis.toLowerCase().includes(this.search.toLowerCase())
    );
    this.cdr.detectChanges();
  }

  async delete(c: Chauffeur) {
    const ok = await this.confirmDialog.open('Supprimer le chauffeur', `Supprimer ${c.nom} ?`);
    if (!ok) return;
    this.service.delete(c.id!).subscribe({
      next: () => { this.notif.success('Chauffeur supprimé'); this.load(); },
      error: (e: any) => this.notif.error(e.message)
    });
  }
}
