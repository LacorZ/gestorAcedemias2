import { IPadre, NewPadre } from './padre.model';

export const sampleWithRequiredData: IPadre = {
  id: 3555,
  nombre: 'Interno Intercambiable engineer',
  email: 'Silvia.Miramontes@yahoo.com',
};

export const sampleWithPartialData: IPadre = {
  id: 91604,
  nombre: 'Gráfica data-warehouse Analista',
  email: 'Esteban79@hotmail.com',
  telefono: 'Inverso RSS Colombia',
};

export const sampleWithFullData: IPadre = {
  id: 50185,
  nombre: 'Heredado Ensalada',
  email: 'Ramiro87@hotmail.com',
  telefono: 'Account Account',
  observaciones: 'invoice maximizada Aragón',
};

export const sampleWithNewData: NewPadre = {
  nombre: 'Rioja Buckinghamshire Madera',
  email: 'Isabela26@hotmail.com',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
