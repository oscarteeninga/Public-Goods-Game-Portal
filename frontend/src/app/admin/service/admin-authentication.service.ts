import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { AdminAuthRest } from '../rest/admin-auth.rest';
import { AdminCredential } from '../model/admin-credential';
import { AdminBearer, emptyAdminBearer } from '../model/admin-bearer';
import { adminPaths } from '../../core/paths/admin.paths';

@Injectable({providedIn: 'root'})
export class AdminAuthenticationService {

  private adminSubjectBearer: BehaviorSubject<AdminBearer>;
  private adminBearer: Observable<AdminBearer>;

  constructor(
    private router: Router,
    private http: HttpClient,
    private adminAuthRest: AdminAuthRest
  ) {
    let adminBearer = localStorage.getItem('adminBearer');
    this.adminSubjectBearer = new BehaviorSubject<AdminBearer>(adminBearer ? JSON.parse(adminBearer) : null);
    this.adminBearer = this.adminSubjectBearer.asObservable();
  }


  public get getAdminBearerValue(): AdminBearer {
    return this.adminSubjectBearer.value;
  }

  public get isBearerPresent(): boolean {
    if (this.adminSubjectBearer.value == undefined || this.adminSubjectBearer.value.token == undefined)
      return false;

    return this.adminSubjectBearer.value.token.startsWith('BEARER-');
  }


  login(username: string, password: string): Observable<AdminBearer> {
    const adminCredential: AdminCredential = {
      username: username,
      password: password
    };

    return this.adminAuthRest.authenticate(adminCredential)
      .pipe(map(bearer => {
        localStorage.setItem('adminBearer', JSON.stringify(bearer));
        this.adminSubjectBearer.next(bearer);
        return bearer;
      }));
  }

  logout(): void {
    // remove user from local storage to log user out
    localStorage.removeItem('adminBearer');
    this.adminSubjectBearer.next(emptyAdminBearer);
    this.router.navigate([adminPaths.adminLogin.absolute]);
  }
}
