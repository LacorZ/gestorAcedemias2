import dayjs from 'dayjs/esm';

import { IPayment, NewPayment } from './payment.model';

export const sampleWithRequiredData: IPayment = {
  id: 47537,
  amount: 72336,
  paidAt: dayjs('2022-12-12T02:20'),
};

export const sampleWithPartialData: IPayment = {
  id: 47604,
  amount: 34993,
  paidAt: dayjs('2022-12-11T22:55'),
};

export const sampleWithFullData: IPayment = {
  id: 46452,
  amount: 39655,
  paidAt: dayjs('2022-12-12T13:18'),
};

export const sampleWithNewData: NewPayment = {
  amount: 32065,
  paidAt: dayjs('2022-12-12T07:28'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
