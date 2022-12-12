import dayjs from 'dayjs/esm';

import { IFactura, NewFactura } from './factura.model';

export const sampleWithRequiredData: IFactura = {
  id: 85429,
  facturado: 56706,
  fechaFactura: dayjs('2022-12-11T23:43'),
};

export const sampleWithPartialData: IFactura = {
  id: 43410,
  facturado: 71211,
  fechaFactura: dayjs('2022-12-12T08:58'),
};

export const sampleWithFullData: IFactura = {
  id: 73284,
  facturado: 39299,
  fechaFactura: dayjs('2022-12-11T22:53'),
  observaciones: 'transmit',
};

export const sampleWithNewData: NewFactura = {
  facturado: 44385,
  fechaFactura: dayjs('2022-12-12T04:01'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
