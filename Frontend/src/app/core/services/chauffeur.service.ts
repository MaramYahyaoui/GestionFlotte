import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environements/environement';
import { ApiResponse, Chauffeur } from '../models/models';

@Injectable({ providedIn: 'root' })
export class ChauffeurService {
  private url = `${environment.apiUrl}/chauffeurs`;

  constructor(private http: HttpClient) {}

  getAll(nom?: string): Observable<Chauffeur[]> {
    let params = new HttpParams();
    if (nom) params = params.set('nom', nom);
    return this.http.get<ApiResponse<Chauffeur[]>>(this.url, { params })
      .pipe(map(r => r.data));
  }

  getById(id: number): Observable<Chauffeur> {
    return this.http.get<ApiResponse<Chauffeur>>(`${this.url}/${id}`)
      .pipe(map(r => r.data));
  }

  getDisponibles(): Observable<Chauffeur[]> {
    return this.http.get<ApiResponse<Chauffeur[]>>(`${this.url}/disponibles`)
      .pipe(map(r => r.data));
  }

  getLesPlasActifs(): Observable<Chauffeur[]> {
    return this.http.get<ApiResponse<Chauffeur[]>>(`${this.url}/les-plus-actifs`)
      .pipe(map(r => r.data));
  }

  create(chauffeur: Chauffeur): Observable<Chauffeur> {
    return this.http.post<ApiResponse<Chauffeur>>(this.url, chauffeur)
      .pipe(map(r => r.data));
  }

  update(id: number, chauffeur: Chauffeur): Observable<Chauffeur> {
    return this.http.put<ApiResponse<Chauffeur>>(`${this.url}/${id}`, chauffeur)
      .pipe(map(r => r.data));
  }

  delete(id: number): Observable<void> {
    return this.http.delete<ApiResponse<void>>(`${this.url}/${id}`)
      .pipe(map(() => undefined));
  }
}
