import { IAttendance } from 'app/entities/attendance/attendance.model';
import { ITutor } from 'app/entities/tutor/tutor.model';
import { IStudent } from 'app/entities/student/student.model';

export interface ICourse {
  id: number;
  name?: string | null;
  description?: string | null;
  price?: number | null;
  duration?: number | null;
  attendance?: Pick<IAttendance, 'id'> | null;
  tutors?: Pick<ITutor, 'id'>[] | null;
  students?: Pick<IStudent, 'id'>[] | null;
}

export type NewCourse = Omit<ICourse, 'id'> & { id: null };
