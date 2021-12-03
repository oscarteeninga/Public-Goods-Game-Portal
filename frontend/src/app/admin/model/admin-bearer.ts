export interface AdminBearer {
  token: string;
  creationTimestamp: number;
}

export const emptyAdminBearer: AdminBearer = {
  token: '',
  creationTimestamp: 0,
};
