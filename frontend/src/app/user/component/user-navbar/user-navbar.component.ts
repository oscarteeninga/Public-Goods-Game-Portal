import { Component } from '@angular/core';
import { UserStateService } from "../../service/user-state.service";

@Component({
  selector: 'app-user-navbar',
  templateUrl: './user-navbar.component.html',
  styleUrls: ['./user-navbar.component.css']
})
export class UserNavbarComponent {

  constructor(private userManagementService: UserStateService) {
  }

  isUserRegistered(): boolean {
    return this.userManagementService.isUserRegistered();
  }

  getUsername(): string {
    return this.userManagementService.getUser().username;
  }

}
