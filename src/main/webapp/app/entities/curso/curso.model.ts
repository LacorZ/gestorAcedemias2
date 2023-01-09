import dayjs from 'dayjs/esm';
import { IAsistencia } from 'app/entities/asistencia/asistencia.model';
import { ITutor } from 'app/entities/tutor/tutor.model';
import { IEstudiante } from 'app/entities/estudiante/estudiante.model';

export interface ICurso {
  id: number;
  nombre?: string | null;
  descripcion?: string | null;
  fechaInicio?: dayjs.Dayjs | null;
  fechaFin?: dayjs.Dayjs | null;
  observaciones?: string | null;
  asistencias?: Pick<IAsistencia, 'id'> | null;
  asistencia?: Pick<IAsistencia, 'id'> | null;
  tutores?: Pick<ITutor, 'id' | 'nombre'>[] | null;
  estudiantes?: Pick<IEstudiante, 'id' | 'nombre'>[] | null;
}

export type NewCurso = Omit<ICurso, 'id'> & { id: null };
