import { IEstudiante, NewEstudiante } from './estudiante.model';

export const sampleWithRequiredData: IEstudiante = {
  id: 8970,
  nombre: 'Adelante Regional maximizada',
  email: 'Luz.Tello@hotmail.com',
};

export const sampleWithPartialData: IEstudiante = {
  id: 43428,
  nombre: 'Plástico',
  email: 'Carla_Alcal@gmail.com',
  telefono: 'uniforme Proactivo',
};

export const sampleWithFullData: IEstudiante = {
  id: 75341,
  nombre: 'mejora hacking',
  email: 'Vernica33@gmail.com',
  telefono: 'Bedfordshire',
  observaciones: 'navigating',
};

export const sampleWithNewData: NewEstudiante = {
  nombre: 'enable Compartible',
  email: 'Micaela87@yahoo.com',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
