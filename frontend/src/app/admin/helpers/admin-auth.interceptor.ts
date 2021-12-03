import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AdminAuthenticationService } from '../service/admin-authentication.service';
import { environment } from '../../../environments/environment';


@Injectable()
export class AdminAuthInterceptor implements HttpInterceptor {
  constructor(private authenticationService: AdminAuthenticationService) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const admin = this.authenticationService.getAdminBearerValue;
    const isLoggedIn = this.authenticationService.isBearerPresent;
    const isAdminApiUrl = request.url.startsWith(environment.singApiPrefix + '/admin');

    if (isAdminApiUrl && isLoggedIn) {
      request = request.clone({
        setHeaders: {
          'Custom-Auth': `${admin.token}`
        }
      });
    }

    return next.handle(request);
  }
}
