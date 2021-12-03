import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserManagementRest } from '../rest/user-management.rest';
import { User } from '../model/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserManagementService {

  constructor(private userManagementRest: UserManagementRest) {
  }

  register(gender: string, username: string): Observable<User> {
    return this.userManagementRest.register(gender, username);
  }

  registerNew(): Observable<User> {
    return this.userManagementRest.registerNew();
  }

  deregister(username: string): Observable<any> {
    return this.userManagementRest.deregister(username);
  }


  getAllUsers(): Observable<User[]> {
    return this.userManagementRest.getAllUsers();
  }

  isRegistered(username: string): Observable<any> {
    return this.userManagementRest.isRegistered(username);
  }
}
