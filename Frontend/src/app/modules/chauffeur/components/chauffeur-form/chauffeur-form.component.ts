import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ChauffeurService } from '../../../../core/services/chauffeur.service';
import { NotificationService } from '../../../../core/services/notification.service';

@Component({
  selector: 'app-chauffeur-form',
  templateUrl: './chauffeur-form.component.html',
  standalone: false,
})
export class ChauffeurFormComponent implements OnInit {
  form!: FormGroup;
  isEdit = false;
  chauffeurId: number | null = null;
  loading = false;
  saving = false;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private service: ChauffeurService,
    private notif: NotificationService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.form = this.fb.group({
      nom:        ['', [Validators.required, Validators.maxLength(100)]],
      permis:     ['', [Validators.required, Validators.maxLength(50)]],
      experience: [0,  [Validators.required, Validators.min(0), Validators.max(60)]],
    });

    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEdit = true;
      this.chauffeurId = +id;
      this.loading = true;
      this.cdr.detectChanges();
      this.service.getById(+id).subscribe({
        next: c => {
          this.form.patchValue(c);
          this.loading = false;
          this.cdr.detectChanges();
        },
        error: () => {
          this.notif.error('Chauffeur introuvable');
          this.router.navigate(['/chauffeurs']);
        }
      });
    }
  }

  submit() {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.saving = true;
    this.cdr.detectChanges();
    const req$ = this.isEdit
      ? this.service.update(this.chauffeurId!, this.form.value)
      : this.service.create(this.form.value);
    req$.subscribe({
      next: () => {
        this.notif.success(this.isEdit ? 'Chauffeur mis à jour' : 'Chauffeur créé');
        this.router.navigate(['/chauffeurs']);
      },
      error: (e: any) => {
        this.notif.error(e.message);
        this.saving = false;
        this.cdr.detectChanges();
      }
    });
  }

  get f() { return this.form.controls; }
  cancel() { this.router.navigate(['/chauffeurs']); }
}
