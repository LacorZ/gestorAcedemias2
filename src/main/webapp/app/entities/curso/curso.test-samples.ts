import dayjs from 'dayjs/esm';

import { ICurso, NewCurso } from './curso.model';

export const sampleWithRequiredData: ICurso = {
  id: 11617,
  nombre: 'Metal FTP extend',
};

export const sampleWithPartialData: ICurso = {
  id: 51737,
  nombre: 'asíncrona',
  observaciones: 'input productividad Eritrea',
};

export const sampleWithFullData: ICurso = {
  id: 6826,
  nombre: 'deposit',
  descripcion: 'Librería Equilibrado',
  fechaInicio: dayjs('2022-12-11'),
  fechaFin: dayjs('2022-12-12'),
  observaciones: 'Bedfordshire web-readiness capacitor',
};

export const sampleWithNewData: NewCurso = {
  nombre: 'Taka Hormigon',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
