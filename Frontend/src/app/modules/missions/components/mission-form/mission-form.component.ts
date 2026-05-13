import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { MissionService } from '../../../../core/services/mission.service';
import { VehiculeService } from '../../../../core/services/vehicule.service';
import { ChauffeurService } from '../../../../core/services/chauffeur.service';
import { NotificationService } from '../../../../core/services/notification.service';
import { Vehicule, Chauffeur } from '../../../../core/models/models';

@Component({
  selector: 'app-mission-form',
  templateUrl: './mission-form.component.html',
  standalone: false,
})
export class MissionFormComponent implements OnInit {
  form!: FormGroup;
  isEdit = false;
  missionId: number | null = null;
  loading = false;
  saving = false;
  vehicules: Vehicule[] = [];
  chauffeurs: Chauffeur[] = [];

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private service: MissionService,
    private vehiculeService: VehiculeService,
    private chauffeurService: ChauffeurService,
    private notif: NotificationService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.form = this.fb.group({
      vehiculeId:  [null, Validators.required],
      chauffeurId: [null, Validators.required],
      pointDepart: ['',   Validators.required],
      destination: ['',   Validators.required],
      distance:    [null, [Validators.required, Validators.min(0.1)]],
      dateMission: [new Date().toISOString().split('T')[0]],
    });

    // Charger véhicules et chauffeurs disponibles avec detectChanges
    forkJoin({
      vehicules:  this.vehiculeService.getAll('DISPONIBLE').pipe(catchError(() => of([]))),
      chauffeurs: this.chauffeurService.getDisponibles().pipe(catchError(() => of([]))),
    }).subscribe({
      next: (results: any) => {
        this.vehicules  = results.vehicules;
        this.chauffeurs = results.chauffeurs;
        this.cdr.detectChanges();
      }
    });

    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEdit = true;
      this.missionId = +id;
      this.loading = true;
      this.cdr.detectChanges();
      this.service.getById(+id).subscribe({
        next: m => {
          this.form.patchValue(m);
          this.loading = false;
          this.cdr.detectChanges();
        },
        error: () => {
          this.notif.error('Mission introuvable');
          this.router.navigate(['/missions']);
        }
      });
    }
  }

  submit() {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.saving = true;
    this.cdr.detectChanges();
    const req$ = this.isEdit
      ? this.service.update(this.missionId!, this.form.value)
      : this.service.create(this.form.value);
    req$.subscribe({
      next: () => {
        this.notif.success(this.isEdit ? 'Mission mise à jour' : 'Mission créée');
        this.router.navigate(['/missions']);
      },
      error: (e: any) => {
        this.notif.error(e.message);
        this.saving = false;
        this.cdr.detectChanges();
      }
    });
  }

  get f() { return this.form.controls; }
  cancel() { this.router.navigate(['/missions']); }
}
