import dayjs from 'dayjs/esm';

import { ICurso, NewCurso } from './curso.model';

export const sampleWithRequiredData: ICurso = {
  id: 11617,
  nombre: 'Metal FTP extend',
  price: 47055,
};

export const sampleWithPartialData: ICurso = {
  id: 21123,
  nombre: 'mano',
  price: 74462,
  fechaFin: dayjs('2022-12-12'),
  observaciones: 'productividad',
};

export const sampleWithFullData: ICurso = {
  id: 54708,
  nombre: 'Avon',
  descripcion: 'Granito Librería',
  price: 22257,
  fechaInicio: dayjs('2022-12-12'),
  fechaFin: dayjs('2022-12-12'),
  observaciones: 'sistema funcionalidad Cine',
};

export const sampleWithNewData: NewCurso = {
  nombre: 'Metal Hormigon parsing',
  price: 75141,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
