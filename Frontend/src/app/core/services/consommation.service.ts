import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environements/environement';
import { ApiResponse, Consommation, CoutCarburant, ConsommationMensuelle } from '../models/models';

@Injectable({ providedIn: 'root' })
export class ConsommationService {
  private url = `${environment.apiUrl}/consommations`;

  constructor(private http: HttpClient) {}

  getAll(vehiculeId?: number): Observable<Consommation[]> {
    let params = new HttpParams();
    if (vehiculeId) params = params.set('vehiculeId', vehiculeId.toString());
    return this.http.get<ApiResponse<Consommation[]>>(this.url, { params })
      .pipe(map(r => r.data));
  }

  getById(id: number): Observable<Consommation> {
    return this.http.get<ApiResponse<Consommation>>(`${this.url}/${id}`)
      .pipe(map(r => r.data));
  }

  getDashboard(): Observable<CoutCarburant[]> {
    return this.http.get<ApiResponse<CoutCarburant[]>>(`${this.url}/dashboard`)
      .pipe(map(r => r.data));
  }

  getMensuelle(vehiculeId: number): Observable<ConsommationMensuelle[]> {
    return this.http.get<ApiResponse<ConsommationMensuelle[]>>(
      `${this.url}/mensuelle/vehicule/${vehiculeId}`)
      .pipe(map(r => r.data));
  }

  getCoutTotal(): Observable<number> {
    return this.http.get<ApiResponse<number>>(`${this.url}/cout-total`)
      .pipe(map(r => r.data));
  }

  create(conso: Consommation): Observable<Consommation> {
    return this.http.post<ApiResponse<Consommation>>(this.url, conso)
      .pipe(map(r => r.data));
  }

  update(id: number, conso: Consommation): Observable<Consommation> {
    return this.http.put<ApiResponse<Consommation>>(`${this.url}/${id}`, conso)
      .pipe(map(r => r.data));
  }

  delete(id: number): Observable<void> {
    return this.http.delete<ApiResponse<void>>(`${this.url}/${id}`)
      .pipe(map(() => undefined));
  }
}
