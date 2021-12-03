import { Injectable } from '@angular/core';
import { defaultUser, User } from '../model/user.model';
import { GameSessionToken } from "../../core/model/game-session-token.model";
import { UserManagementService } from "./user-management.service";
import { UserGameConnectionService } from "./user-game-connection.service";
import { Observable, of } from "rxjs";
import { map, mergeMap } from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class UserStateService {

  private readonly userKey = "userKey";
  private readonly registeredKey = "registeredKey";
  private readonly connectedGamesKey = "connectedGamesKey";

  private user: User;
  private isRegistered: boolean;
  private connectedGames: Set<string>;

  constructor(private userManagementService: UserManagementService,
              private userGameConnectionService: UserGameConnectionService) {
    this.user = this.loadUser();
    this.isRegistered = this.loadRegistered();
    this.connectedGames = this.loadConnections();
  }

  getUser(): User {
    return this.user;
  }

  isUserRegistered(): boolean {
    return this.isRegistered;
  }

  isUserConnected(gameSessionToken: GameSessionToken): boolean {
    return this.connectedGames.has(gameSessionToken.token);
  }

  ensureConnected(gameSessionToken: GameSessionToken): Observable<boolean> {
    // TODO maybe should remove games cache here - might grow over time.
    //  Some timestamps, some structure that can remove oldest values?

    return of(this.user === defaultUser)
      .pipe(mergeMap(userRegistered => this.ensureUserIsRegistered(userRegistered)))
      .pipe(mergeMap(_ => this.ensureUserIsConnected(gameSessionToken)));
  }

  private ensureUserIsRegistered(isUserAbsent: boolean): Observable<boolean> {
    // check user registration status on server side
    if (isUserAbsent) {
      // user absent - register new
      return this.userManagementService.registerNew().pipe(map(user => {
        this.updateUser(user);
        this.updateRegistered(true);
        return true;
      }));
    } else {
      // user present - check if registered and register if not
      return this.userManagementService.isRegistered(this.user.username).pipe(mergeMap(registered => {
        if (registered) {
          // user registered - continue
          this.updateRegistered(true);
          return of(true);
        } else {
          // user not registered - register
          return this.userManagementService.register(this.user.gender, this.user.username)
            .pipe(map(user => {
              this.updateUser(user);
              this.updateRegistered(true);
              return true;
            }));
        }
      }));
    }
  }

  private ensureUserIsConnected(gameSessionToken: GameSessionToken): Observable<boolean> {
    // check user connection status on server side
    return this.userGameConnectionService.isConnected(this.user.username, gameSessionToken)
      .pipe(mergeMap(connected => {
          if (connected) {
            // already connected to game (should not happen, otherwise why redirect here...)
            this.updateConnections(gameSessionToken);
            return of(true);
          } else {
            // not connected to game - connect
            return this.userGameConnectionService.connect(this.user.username, gameSessionToken)
              .pipe(map(connected => {
                if (connected) {
                  this.updateConnections(gameSessionToken);
                  return true;
                } else {
                  return false;
                }
              }));
          }
        }
      ));
  }

  clearCache(): void {
    localStorage.removeItem(this.userKey);
    localStorage.removeItem(this.registeredKey);
    localStorage.removeItem(this.connectedGamesKey);
  }

  private loadUser(): User {
    let raw = localStorage.getItem(this.userKey);
    return raw ? JSON.parse(raw) : defaultUser;
  }

  private updateUser(user: User): void {
    this.user = user;
    localStorage.setItem(this.userKey, JSON.stringify(this.user));
  }

  private loadRegistered(): boolean {
    let raw = localStorage.getItem(this.registeredKey);
    return raw ? JSON.parse(raw) : false;
  }

  private updateRegistered(isRegistered: boolean): void {
    this.isRegistered = isRegistered;
    localStorage.setItem(this.registeredKey, JSON.stringify(this.isRegistered));
  }

  private loadConnections(): Set<string> {
    let raw = localStorage.getItem(this.connectedGamesKey);
    return raw ? new Set(JSON.parse(raw)) : new Set();
  }

  private updateConnections(game: GameSessionToken): void {
    this.connectedGames.add(game.token);
    localStorage.setItem(this.connectedGamesKey, JSON.stringify(Array.from(this.connectedGames)));
  }
}
