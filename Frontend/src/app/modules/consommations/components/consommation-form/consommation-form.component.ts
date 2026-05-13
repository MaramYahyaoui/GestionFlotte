import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ConsommationService } from '../../../../core/services/consommation.service';
import { VehiculeService } from '../../../../core/services/vehicule.service';
import { NotificationService } from '../../../../core/services/notification.service';
import { Vehicule } from '../../../../core/models/models';

@Component({
  selector: 'app-consommation-form',
  templateUrl: './consommation-form.component.html',
  standalone: false,
})
export class ConsommationFormComponent implements OnInit {
  form!: FormGroup;
  isEdit = false;
  consoId: number | null = null;
  loading = false;
  saving = false;
  vehicules: Vehicule[] = [];

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private service: ConsommationService,
    private vehiculeService: VehiculeService,
    private notif: NotificationService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.form = this.fb.group({
      vehiculeId:        [null, Validators.required],
      date:              [new Date().toISOString().split('T')[0], Validators.required],
      quantiteCarburant: [null, [Validators.required, Validators.min(0.1)]],
      coutTotal:         [null, [Validators.required, Validators.min(0.1)]],
    });

    // Charger la liste des véhicules
    this.vehiculeService.getAll().subscribe({
      next: v => {
        this.vehicules = v;
        this.cdr.detectChanges();
      }
    });

    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEdit = true;
      this.consoId = +id;
      this.loading = true;
      this.cdr.detectChanges();
      this.service.getById(+id).subscribe({
        next: c => {
          this.form.patchValue(c);
          this.loading = false;
          this.cdr.detectChanges();
        },
        error: () => {
          this.notif.error('Introuvable');
          this.router.navigate(['/consommations']);
        }
      });
    }
  }

  submit() {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.saving = true;
    this.cdr.detectChanges();
    const req$ = this.isEdit
      ? this.service.update(this.consoId!, this.form.value)
      : this.service.create(this.form.value);
    req$.subscribe({
      next: () => {
        this.notif.success(this.isEdit ? 'Mis à jour' : 'Consommation enregistrée');
        this.router.navigate(['/consommations']);
      },
      error: (e: any) => {
        this.notif.error(e.message);
        this.saving = false;
        this.cdr.detectChanges();
      }
    });
  }

  get f() { return this.form.controls; }
  cancel() { this.router.navigate(['/consommations']); }
}
