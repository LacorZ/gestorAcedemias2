import { ICurso } from 'app/entities/curso/curso.model';

export interface ITutor {
  id: number;
  nombre?: string | null;
  email?: string | null;
  telefono?: string | null;
  observaciones?: string | null;
  cursos?: Pick<ICurso, 'id' | 'nombre'>[] | null;
}

export type NewTutor = Omit<ITutor, 'id'> & { id: null };
