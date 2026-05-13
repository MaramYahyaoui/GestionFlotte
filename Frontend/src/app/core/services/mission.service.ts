import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environements/environement';
import { ApiResponse, TrajetMission } from '../models/models';

@Injectable({ providedIn: 'root' })
export class MissionService {
  private url = `${environment.apiUrl}/missions`;

  constructor(private http: HttpClient) {}

  getAll(filters?: { statut?: string; vehiculeId?: number; chauffeurId?: number; debut?: string; fin?: string }): Observable<TrajetMission[]> {
    let params = new HttpParams();
    if (filters?.statut) params = params.set('statut', filters.statut);
    if (filters?.vehiculeId) params = params.set('vehiculeId', filters.vehiculeId.toString());
    if (filters?.chauffeurId) params = params.set('chauffeurId', filters.chauffeurId.toString());
    if (filters?.debut) params = params.set('debut', filters.debut);
    if (filters?.fin) params = params.set('fin', filters.fin);
    return this.http.get<ApiResponse<TrajetMission[]>>(this.url, { params })
      .pipe(map(r => r.data));
  }

  getById(id: number): Observable<TrajetMission> {
    return this.http.get<ApiResponse<TrajetMission>>(`${this.url}/${id}`)
      .pipe(map(r => r.data));
  }

  getEnCours(): Observable<TrajetMission[]> {
    return this.http.get<ApiResponse<TrajetMission[]>>(`${this.url}/en-cours`)
      .pipe(map(r => r.data));
  }

  create(mission: TrajetMission): Observable<TrajetMission> {
    return this.http.post<ApiResponse<TrajetMission>>(this.url, mission)
      .pipe(map(r => r.data));
  }

  update(id: number, mission: TrajetMission): Observable<TrajetMission> {
    return this.http.put<ApiResponse<TrajetMission>>(`${this.url}/${id}`, mission)
      .pipe(map(r => r.data));
  }

  terminer(id: number, distance?: number): Observable<TrajetMission> {
    return this.http.patch<ApiResponse<TrajetMission>>(`${this.url}/${id}/terminer`,
      distance ? { distance } : {})
      .pipe(map(r => r.data));
  }

  annuler(id: number): Observable<TrajetMission> {
    return this.http.patch<ApiResponse<TrajetMission>>(`${this.url}/${id}/annuler`, {})
      .pipe(map(r => r.data));
  }

  delete(id: number): Observable<void> {
    return this.http.delete<ApiResponse<void>>(`${this.url}/${id}`)
      .pipe(map(() => undefined));
  }
}
