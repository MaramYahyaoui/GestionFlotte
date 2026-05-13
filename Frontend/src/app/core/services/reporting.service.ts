import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map, catchError, of } from 'rxjs';
import { environment } from '../../../environements/environement';
import { ApiResponse, DashboardData, VehiculeActif } from '../models/models';

@Injectable({ providedIn: 'root' })
export class ReportingService {
  private url = `${environment.apiUrl}/reporting`;

  constructor(private http: HttpClient) {}

  getDashboard(): Observable<DashboardData> {
    return this.http.get<ApiResponse<DashboardData>>(`${this.url}/dashboard`)
      .pipe(
        map(r => r.data),
        catchError(e => {
          console.error('getDashboard error:', e);
          throw e;  // ← re-propager pour que le composant l'attrape
        })
      );
  }

  getTop5(): Observable<VehiculeActif[]> {
    return this.http.get<ApiResponse<VehiculeActif[]>>(`${this.url}/top5-vehicules`)
      .pipe(
        map(r => r.data),
        catchError(() => of([]))  // ← retourner tableau vide si erreur
      );
  }

  getFLotteActive(): Observable<VehiculeActif[]> {
    return this.http.get<ApiResponse<VehiculeActif[]>>(`${this.url}/flotte-active`)
      .pipe(
        map(r => r.data),
        catchError(() => of([]))
      );
  }

  getStatuts(): Observable<Record<string, number>> {
    return this.http.get<ApiResponse<Record<string, number>>>(`${this.url}/statuts`)
      .pipe(
        map(r => r.data),
        catchError(() => of({}))
      );
  }

  getMissionsParMois(annee?: number): Observable<Record<string, number>> {
    let params = new HttpParams();
    if (annee) params = params.set('annee', annee.toString());
    return this.http.get<ApiResponse<Record<string, number>>>(`${this.url}/missions-par-mois`, { params })
      .pipe(
        map(r => r.data),
        catchError(() => of({}))
      );
  }
}