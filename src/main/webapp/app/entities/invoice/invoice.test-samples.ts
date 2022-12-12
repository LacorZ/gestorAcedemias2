import dayjs from 'dayjs/esm';

import { IInvoice, NewInvoice } from './invoice.model';

export const sampleWithRequiredData: IInvoice = {
  id: 91509,
  amount: 62071,
  issuedAt: dayjs('2022-12-12T08:02'),
  dueAt: dayjs('2022-12-12T18:07'),
};

export const sampleWithPartialData: IInvoice = {
  id: 12294,
  amount: 24487,
  issuedAt: dayjs('2022-12-12T09:50'),
  dueAt: dayjs('2022-12-12T09:18'),
};

export const sampleWithFullData: IInvoice = {
  id: 18841,
  amount: 48480,
  issuedAt: dayjs('2022-12-12T08:20'),
  dueAt: dayjs('2022-12-12T09:19'),
};

export const sampleWithNewData: NewInvoice = {
  amount: 13016,
  issuedAt: dayjs('2022-12-12T06:54'),
  dueAt: dayjs('2022-12-12T12:26'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
