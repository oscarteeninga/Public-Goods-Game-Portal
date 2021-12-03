import { combinePaths, getAbsolutePath } from './paths.util';

const userPrefix = 'user';

const userDefinitions = {
  game: 'game',
  connect: 'connect',
}

export const userPaths = {
  game: {
    relative: combinePaths(userPrefix, userDefinitions.game),
    absolute: getAbsolutePath(combinePaths(userPrefix, userDefinitions.game)),
  },

  connect: {
    relative: combinePaths(userPrefix, userDefinitions.connect),
    absolute: getAbsolutePath(combinePaths(userPrefix, userDefinitions.connect)),
  },
};
