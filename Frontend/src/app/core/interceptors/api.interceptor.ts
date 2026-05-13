import { Injectable } from '@angular/core';
import {
  HttpRequest, HttpHandler, HttpEvent,
  HttpInterceptor, HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable()
export class ApiInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {

    // ✅ Ne pas ajouter Content-Type sur les GET (cause des problèmes CORS)
    const apiReq = req.method === 'GET' ? req : req.clone({
      setHeaders: { 'Content-Type': 'application/json' }
    });

    return next.handle(apiReq).pipe(
      catchError((error: HttpErrorResponse) => {
        let message = 'Une erreur est survenue';
        if (error.error?.message) message = error.error.message;
        else if (error.status === 0) message = 'Impossible de contacter le serveur';
        else if (error.status === 404) message = 'Ressource introuvable';
        console.error('API Error:', error.status, message);
        // ✅ Propager une vraie Error pour que le bloc error: () => {} soit appelé
        return throwError(() => new Error(message));
      })
    );
  }
}