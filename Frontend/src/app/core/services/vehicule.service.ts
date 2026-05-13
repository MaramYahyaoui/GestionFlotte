import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environements/environement';
import { ApiResponse, Vehicule, AlerteMaintenance, VehiculeActif } from '../models/models';

@Injectable({ providedIn: 'root' })
export class VehiculeService {
  private url = `${environment.apiUrl}/vehicules`;

  constructor(private http: HttpClient) {}

  getAll(statut?: string): Observable<Vehicule[]> {
    let params = new HttpParams();
    if (statut) params = params.set('statut', statut);
    return this.http.get<ApiResponse<Vehicule[]>>(this.url, { params })
      .pipe(map(r => r.data));
  }

  getById(id: number): Observable<Vehicule> {
    return this.http.get<ApiResponse<Vehicule>>(`${this.url}/${id}`)
      .pipe(map(r => r.data));
  }

  getActifs(): Observable<Vehicule[]> {
    return this.http.get<ApiResponse<Vehicule[]>>(`${this.url}/actifs`)
      .pipe(map(r => r.data));
  }

  getAlertesMaintenance(seuil?: number): Observable<AlerteMaintenance[]> {
    let params = new HttpParams();
    if (seuil) params = params.set('seuil', seuil.toString());
    return this.http.get<ApiResponse<AlerteMaintenance[]>>(`${this.url}/maintenance/alertes`, { params })
      .pipe(map(r => r.data));
  }

  getLesPlasActifs(): Observable<Vehicule[]> {
    return this.http.get<ApiResponse<Vehicule[]>>(`${this.url}/les-plus-actifs`)
      .pipe(map(r => r.data));
  }

  create(vehicule: Vehicule): Observable<Vehicule> {
    return this.http.post<ApiResponse<Vehicule>>(this.url, vehicule)
      .pipe(map(r => r.data));
  }

  update(id: number, vehicule: Vehicule): Observable<Vehicule> {
    return this.http.put<ApiResponse<Vehicule>>(`${this.url}/${id}`, vehicule)
      .pipe(map(r => r.data));
  }

  updateStatut(id: number, statut: string): Observable<Vehicule> {
    return this.http.patch<ApiResponse<Vehicule>>(`${this.url}/${id}/statut`, { statut })
      .pipe(map(r => r.data));
  }

  updateKilometrage(id: number, kilometrage: number): Observable<Vehicule> {
    return this.http.patch<ApiResponse<Vehicule>>(`${this.url}/${id}/kilometrage`, { kilometrage })
      .pipe(map(r => r.data));
  }

  delete(id: number): Observable<void> {
    return this.http.delete<ApiResponse<void>>(`${this.url}/${id}`)
      .pipe(map(() => undefined));
  }
}
