export function combinePaths(pref: string, suffix: string): string {
  return pref + '/' + suffix;
}

export function getAbsolutePath(path: string): string {
  return '/' + path;
}


export const queryParam = {
  id: '/:id',
}
