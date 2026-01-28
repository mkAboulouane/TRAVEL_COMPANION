export interface IPermission {
  id?: number;
  name?: string;
  description?: string | null;
  code?: string;
}

export const defaultValue: Readonly<IPermission> = {};
