import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { AdminCredential } from '../model/admin-credential';
import { AdminBearer } from '../model/admin-bearer';


@Injectable({providedIn: 'root'})
export class AdminAuthRest {

  private readonly baseUrl = `${environment.singApiPrefix}/auth`;

  constructor(private http: HttpClient) {
  }

  authenticate(adminCredential: AdminCredential) {
    return this.http.post<AdminBearer>(`${this.baseUrl}`, adminCredential, {responseType: 'json'});
  }
}
