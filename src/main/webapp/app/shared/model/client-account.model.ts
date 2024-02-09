import dayjs from 'dayjs';
import { IAccountTransaction } from 'app/shared/model/account-transaction.model';

export interface IClientAccount {
  id?: string;
  accountNumber?: string | null;
  owner?: string | null;
  balance?: number | null;
  creationDate?: dayjs.Dayjs | null;
  clientAccounts?: IAccountTransaction[] | null;
}

export const defaultValue: Readonly<IClientAccount> = {};
