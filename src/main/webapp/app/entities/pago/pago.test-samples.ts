import dayjs from 'dayjs/esm';

import { MetodoPago } from 'app/entities/enumerations/metodo-pago.model';

import { IPago, NewPago } from './pago.model';

export const sampleWithRequiredData: IPago = {
  id: 23778,
  cantidad: 7685,
  fechaPago: dayjs('2022-12-12T04:21'),
  metodoPago: MetodoPago['TARJETA'],
};

export const sampleWithPartialData: IPago = {
  id: 53511,
  cantidad: 58737,
  fechaPago: dayjs('2022-12-12T00:18'),
  metodoPago: MetodoPago['OTRO'],
};

export const sampleWithFullData: IPago = {
  id: 1805,
  cantidad: 64525,
  fechaPago: dayjs('2022-12-12T08:42'),
  metodoPago: MetodoPago['TARJETA'],
  observaciones: 'bypass',
};

export const sampleWithNewData: NewPago = {
  cantidad: 2628,
  fechaPago: dayjs('2022-12-12T07:53'),
  metodoPago: MetodoPago['TARJETA'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
