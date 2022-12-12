import dayjs from 'dayjs/esm';
import { IPayment } from 'app/entities/payment/payment.model';

export interface IInvoice {
  id: number;
  amount?: number | null;
  issuedAt?: dayjs.Dayjs | null;
  dueAt?: dayjs.Dayjs | null;
  payment?: Pick<IPayment, 'id'> | null;
}

export type NewInvoice = Omit<IInvoice, 'id'> & { id: null };
