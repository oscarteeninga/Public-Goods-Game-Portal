import { combinePaths, getAbsolutePath } from './paths.util';


const adminPrefix = 'admin';

const adminDefinitions = {
  root: '',
  games: {
    root: 'games',
    list: 'games/list',
    configureGame: 'games/configure',
  },
  addGame: 'addGame',
  login: 'login',
}

export const adminPaths = {

  root: adminDefinitions.root,

  games: {
    root: {
      relative: combinePaths(adminPrefix, adminDefinitions.games.root),
      absolute: getAbsolutePath(combinePaths(adminPrefix, adminDefinitions.games.root)),
    },
    list: {
      relative: combinePaths(adminPrefix, adminDefinitions.games.list),
      absolute: getAbsolutePath(combinePaths(adminPrefix, adminDefinitions.games.list)),
    },
    gameConfiguration: {
      relative: combinePaths(adminPrefix, adminDefinitions.games.configureGame),
      absolute: getAbsolutePath(combinePaths(adminPrefix, adminDefinitions.games.configureGame)),
    },
  },

  addGame: {
    relative: combinePaths(adminPrefix, adminDefinitions.addGame),
    absolute: getAbsolutePath(combinePaths(adminPrefix, adminDefinitions.addGame)),
  },

  adminLogin: {
    relative: combinePaths(adminPrefix, adminDefinitions.login),
    absolute: getAbsolutePath(combinePaths(adminPrefix, adminDefinitions.login)),
  },

};
