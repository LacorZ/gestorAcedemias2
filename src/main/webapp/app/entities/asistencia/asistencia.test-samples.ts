import dayjs from 'dayjs/esm';

import { AsistenciaEstado } from 'app/entities/enumerations/asistencia-estado.model';

import { IAsistencia, NewAsistencia } from './asistencia.model';

export const sampleWithRequiredData: IAsistencia = {
  id: 66021,
  fecha: dayjs('2022-12-12'),
  estado: AsistenciaEstado['PRESENTE'],
};

export const sampleWithPartialData: IAsistencia = {
  id: 37390,
  fecha: dayjs('2022-12-12'),
  estado: AsistenciaEstado['OTRO'],
  horaEntrada: dayjs('2022-12-12T12:13'),
  observaciones: 'USB',
};

export const sampleWithFullData: IAsistencia = {
  id: 51749,
  fecha: dayjs('2022-12-12'),
  estado: AsistenciaEstado['OTRO'],
  horaEntrada: dayjs('2022-12-12T11:53'),
  horaSalida: dayjs('2022-12-12T19:19'),
  observaciones: 'virtual e-business',
};

export const sampleWithNewData: NewAsistencia = {
  fecha: dayjs('2022-12-12'),
  estado: AsistenciaEstado['OTRO'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
