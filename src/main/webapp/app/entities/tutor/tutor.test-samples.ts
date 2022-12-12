import { ITutor, NewTutor } from './tutor.model';

export const sampleWithRequiredData: ITutor = {
  id: 88430,
  name: 'pixel',
  email: 'Teresa_Corona@gmail.com',
};

export const sampleWithPartialData: ITutor = {
  id: 54748,
  name: 'Avon FTP deposit',
  email: 'Pedro_Abrego@hotmail.com',
  phone: '996.492.619',
  description: 'Bebes Granito',
  photo: '../fake-data/blob/hipster.png',
  photoContentType: 'unknown',
};

export const sampleWithFullData: ITutor = {
  id: 8580,
  name: 'connect',
  email: 'Pilar_Luna@yahoo.com',
  phone: '993.002.133',
  description: 'Guapo withdrawal Rojo',
  photo: '../fake-data/blob/hipster.png',
  photoContentType: 'unknown',
};

export const sampleWithNewData: NewTutor = {
  name: 'transmitting',
  email: 'Eva_Salas4@yahoo.com',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
