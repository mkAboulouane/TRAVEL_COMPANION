import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';

export interface IUserProfile {
  id?: number;
  phoneNumber?: string | null;
  birthDate?: dayjs.Dayjs | null;
  nationality?: string | null;
  preferredLanguage?: string | null;
  user?: IUser;
}

export const defaultValue: Readonly<IUserProfile> = {};
