import { IEstudiante } from 'app/entities/estudiante/estudiante.model';

export interface IPadre {
  id: number;
  nombre?: string | null;
  email?: string | null;
  telefono?: string | null;
  observaciones?: string | null;
  estudiantes?: Pick<IEstudiante, 'id' | 'nombre'>[] | null;
}

export type NewPadre = Omit<IPadre, 'id'> & { id: null };
