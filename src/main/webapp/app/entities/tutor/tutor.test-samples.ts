import { ITutor, NewTutor } from './tutor.model';

export const sampleWithRequiredData: ITutor = {
  id: 88430,
  nombre: 'pixel',
  email: 'Teresa_Corona@gmail.com',
};

export const sampleWithPartialData: ITutor = {
  id: 75033,
  nombre: 'firewall Guapa',
  email: 'Esperanza_Chacn37@yahoo.com',
  telefono: 'Sector',
  observaciones: 'withdrawal móbil Coordinador',
};

export const sampleWithFullData: ITutor = {
  id: 48369,
  nombre: 'Granito Increible Buckinghamshire',
  email: 'Andrea.Huerta@hotmail.com',
  telefono: 'acceso por sistema',
  observaciones: 'Borders Inteligente transmitting',
};

export const sampleWithNewData: NewTutor = {
  nombre: 'Dollar Australian',
  email: 'Mara53@gmail.com',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
