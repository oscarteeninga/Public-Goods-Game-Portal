import { Component } from '@angular/core';
import { adminPaths } from '../../../core/paths/admin.paths';
import { AdminAuthenticationService } from '../../service/admin-authentication.service';

@Component({
  selector: 'app-admin-navbar',
  templateUrl: './admin-navbar.component.html',
  styleUrls: ['./admin-navbar.component.css']
})
export class AdminNavbarComponent {

  readonly adminPaths = adminPaths;

  constructor(private authenticationService: AdminAuthenticationService) {
  }

  logout(): void {
    this.authenticationService.logout();
  }

  isLoggedIn(): boolean {
    return this.authenticationService.isBearerPresent;
  }
}
