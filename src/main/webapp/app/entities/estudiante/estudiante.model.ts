import { IAsistencia } from 'app/entities/asistencia/asistencia.model';
import { IFactura } from 'app/entities/factura/factura.model';
import { ICurso } from 'app/entities/curso/curso.model';
import { IPadre } from 'app/entities/padre/padre.model';

export interface IEstudiante {
  id: number;
  nombre?: string | null;
  email?: string | null;
  telefono?: string | null;
  observaciones?: string | null;
  asistencias?: Pick<IAsistencia, 'id'> | null;
  facturas?: Pick<IFactura, 'id'> | null;
  cursos?: Pick<ICurso, 'id'>[] | null;
  asistencia?: Pick<IAsistencia, 'id'> | null;
  factura?: Pick<IFactura, 'id'> | null;
  padres?: Pick<IPadre, 'id'>[] | null;
}

export type NewEstudiante = Omit<IEstudiante, 'id'> & { id: null };
