import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IPermission } from 'app/shared/model/permission.model';

export interface IUserPermission {
  id?: number;
  grantedAt?: dayjs.Dayjs | null;
  isActive?: boolean | null;
  user?: IUser;
  permission?: IPermission;
}

export const defaultValue: Readonly<IUserPermission> = {
  isActive: false,
};
