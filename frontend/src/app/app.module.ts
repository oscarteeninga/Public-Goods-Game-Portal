import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AddGameComponent } from './admin/component/add-game/add-game.component';
import { GameConfigurationComponent } from './admin/component/game-configuration/game-configuration.component';
import { GamesListComponent } from './admin/component/games-list/games-list.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatSliderModule } from '@angular/material/slider';
import { ClipboardModule } from '@angular/cdk/clipboard';
import { MatIconModule } from '@angular/material/icon';
import { GameComponent } from './user/component/game/game.component';
import { AdminNavbarComponent } from './admin/component/admin-navbar/admin-navbar.component';
import { UserNavbarComponent } from './user/component/user-navbar/user-navbar.component';
import { MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { AdminLoginComponent } from './admin/component/admin-login/admin-login.component';
import { AdminAuthInterceptor } from './admin/helpers/admin-auth.interceptor';
import { AdminAuthErrorInterceptor } from './admin/helpers/admin-auth-error.interceptor';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { GameConnectionComponent } from './user/component/game-connection/game-connection.component';
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";

@NgModule({
  declarations: [
    AppComponent,
    AddGameComponent,
    GameConfigurationComponent,
    GamesListComponent,
    GameComponent,
    AdminNavbarComponent,
    UserNavbarComponent,
    AdminLoginComponent,
    GameConnectionComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatSliderModule,
    ClipboardModule,
    MatIconModule,
    ReactiveFormsModule,
    MatTableModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatProgressSpinnerModule,
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: AdminAuthInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: AdminAuthErrorInterceptor, multi: true},
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
