import { IPayment } from 'app/entities/payment/payment.model';

export interface IPaymentMethod {
  id: number;
  name?: string | null;
  payment?: Pick<IPayment, 'id'> | null;
}

export type NewPaymentMethod = Omit<IPaymentMethod, 'id'> & { id: null };
