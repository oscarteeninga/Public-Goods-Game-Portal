import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { AdminAuthenticationService } from '../service/admin-authentication.service';
import { adminPaths } from '../../core/paths/admin.paths';


@Injectable({providedIn: 'root'})
export class AdminAuthGuard implements CanActivate {
  constructor(
    private router: Router,
    private authenticationService: AdminAuthenticationService
  ) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    if (this.authenticationService.isBearerPresent) {
      return true;
    }

    //  redirect to login page with the return url
    this.router.navigate([adminPaths.adminLogin.absolute], {queryParams: {returnUrl: state.url}});
    return false;
  }
}
