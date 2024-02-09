import { IClientAccount } from 'app/shared/model/client-account.model';

export interface IAccountTransaction {
  id?: string;
  transactionName?: string | null;
  amount?: number | null;
  clientAccount?: IClientAccount | null;
}

export const defaultValue: Readonly<IAccountTransaction> = {};
