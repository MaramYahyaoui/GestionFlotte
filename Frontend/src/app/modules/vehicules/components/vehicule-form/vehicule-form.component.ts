import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { VehiculeService } from '../../../../core/services/vehicule.service';
import { NotificationService } from '../../../../core/services/notification.service';

@Component({
  selector: 'app-vehicule-form',
  templateUrl: './vehicule-form.component.html',
  styleUrls: ['./vehicule-form.component.scss'],
  standalone: false,
})
export class VehiculeFormComponent implements OnInit {
  form!: FormGroup;
  isEdit = false;
  vehiculeId: number | null = null;
  loading = false;
  saving = false;

  types = ['Utilitaire', 'Camionnette', 'Pick-up', 'Berline', 'SUV', 'Bus', 'Camion'];

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private vehiculeService: VehiculeService,
    private notif: NotificationService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.form = this.fb.group({
      immatriculation: ['', [Validators.required, Validators.maxLength(20)]],
      modele:          ['', [Validators.required, Validators.maxLength(100)]],
      type:            ['', Validators.required],
      kilometrage:     [0,  [Validators.required, Validators.min(0)]],
      statut:          ['DISPONIBLE', Validators.required],
    });

    const id = this.route.snapshot.paramMap.get('id');
    if (id && id !== 'nouveau') {
      this.isEdit = true;
      this.vehiculeId = +id;
      this.loading = true;
      this.cdr.detectChanges();
      this.vehiculeService.getById(this.vehiculeId).subscribe({
        next: v => {
          this.form.patchValue(v);
          this.loading = false;
          this.cdr.detectChanges();
        },
        error: () => {
          this.notif.error('Véhicule introuvable');
          this.router.navigate(['/vehicules']);
        }
      });
    }
  }

  submit() {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.saving = true;
    this.cdr.detectChanges();
    const req$ = this.isEdit
      ? this.vehiculeService.update(this.vehiculeId!, this.form.value)
      : this.vehiculeService.create(this.form.value);

    req$.subscribe({
      next: () => {
        this.notif.success(this.isEdit ? 'Véhicule mis à jour' : 'Véhicule créé');
        this.router.navigate(['/vehicules']);
      },
      error: (e: any) => {
        this.notif.error(e.message);
        this.saving = false;
        this.cdr.detectChanges();
      }
    });
  }

  get f() { return this.form.controls; }
  cancel() { this.router.navigate(['/vehicules']); }
}
