import { ICourse } from 'app/entities/course/course.model';

export interface ITutor {
  id: number;
  name?: string | null;
  email?: string | null;
  phone?: string | null;
  description?: string | null;
  photo?: string | null;
  photoContentType?: string | null;
  courses?: Pick<ICourse, 'id'>[] | null;
}

export type NewTutor = Omit<ITutor, 'id'> & { id: null };
