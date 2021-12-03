import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpStatusCode } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AdminAuthenticationService } from '../service/admin-authentication.service';


@Injectable()
export class AdminAuthErrorInterceptor implements HttpInterceptor {
  constructor(private authenticationService: AdminAuthenticationService) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(catchError(err => {
      if (err.status === HttpStatusCode.Unauthorized) {
        // auto logout if HttpStatusCode.Unauthorized (401) response returned from api
        this.authenticationService.logout();
      }

      const error = err.error.message || err.statusText;
      return throwError(error);
    }))
  }
}
