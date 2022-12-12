import dayjs from 'dayjs/esm';
import { AsistenciaEstado } from 'app/entities/enumerations/asistencia-estado.model';

export interface IAsistencia {
  id: number;
  fecha?: dayjs.Dayjs | null;
  estado?: AsistenciaEstado | null;
  horaEntrada?: dayjs.Dayjs | null;
  horaSalida?: dayjs.Dayjs | null;
  observaciones?: string | null;
}

export type NewAsistencia = Omit<IAsistencia, 'id'> & { id: null };
