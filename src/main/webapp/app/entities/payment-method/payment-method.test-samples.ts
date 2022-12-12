import { IPaymentMethod, NewPaymentMethod } from './payment-method.model';

export const sampleWithRequiredData: IPaymentMethod = {
  id: 72740,
  name: 'invoice bandwidth',
};

export const sampleWithPartialData: IPaymentMethod = {
  id: 62587,
  name: 'capacitor',
};

export const sampleWithFullData: IPaymentMethod = {
  id: 37471,
  name: 'Pequeño feed mindshare',
};

export const sampleWithNewData: NewPaymentMethod = {
  name: 'blockchains',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
