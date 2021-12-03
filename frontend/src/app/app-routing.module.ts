import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { GamesListComponent } from './admin/component/games-list/games-list.component';
import { GameConfigurationComponent } from './admin/component/game-configuration/game-configuration.component';
import { AddGameComponent } from './admin/component/add-game/add-game.component';

import { GameComponent } from './user/component/game/game.component';
import { adminPaths } from './core/paths/admin.paths';
import { queryParam } from './core/paths/paths.util';
import { userPaths } from './core/paths/user.paths';
import { AdminLoginComponent } from './admin/component/admin-login/admin-login.component';
import { AdminAuthGuard } from './admin/helpers/admin-auth.guard';
import { GameConnectionComponent } from "./user/component/game-connection/game-connection.component";

const routes: Routes = [
  {
    path: '',
    redirectTo: adminPaths.games.list.relative,
    pathMatch: 'full',
  },
  {
    path: adminPaths.games.root.relative,
    redirectTo: adminPaths.games.list.relative,
    pathMatch: 'full',
  },

  {
    path: adminPaths.games.list.relative,
    component: GamesListComponent,
    canActivate: [AdminAuthGuard],
  },

  {
    path: adminPaths.games.gameConfiguration.relative + queryParam.id,
    component: GameConfigurationComponent,
    canActivate: [AdminAuthGuard],
  },

  {
    path: adminPaths.addGame.relative,
    component: AddGameComponent,
    canActivate: [AdminAuthGuard],
  },

  {
    path: adminPaths.adminLogin.relative,
    component: AdminLoginComponent,
  },

  {
    path: userPaths.game.relative + queryParam.id,
    component: GameComponent,
  },

  {
    path: userPaths.connect.relative + queryParam.id,
    component: GameConnectionComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
