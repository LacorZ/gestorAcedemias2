import dayjs from 'dayjs/esm';

export interface IPayment {
  id: number;
  amount?: number | null;
  paidAt?: dayjs.Dayjs | null;
}

export type NewPayment = Omit<IPayment, 'id'> & { id: null };
