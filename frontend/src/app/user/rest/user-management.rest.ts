import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { User } from '../model/user.model';


@Injectable({providedIn: 'root'})
export class UserManagementRest {

  private readonly baseUrl = `${environment.singApiPrefix}/users`;

  constructor(private http: HttpClient) {
  }


  register(gender: string, username: string): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/register?gender=${gender}&username=${username}`, {responseType: 'json'});
  }

  registerNew(): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/register`, {responseType: 'json'});
  }

  deregister(username: string): Observable<boolean> {
    return this.http.delete<boolean>(`${this.baseUrl}/deregister?username=${username}`, {responseType: 'json'});
  }


  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.baseUrl}`, {responseType: 'json'});
  }

  isRegistered(username: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.baseUrl}/isregistered?username=${username}`, {responseType: 'json'});
  }

}
