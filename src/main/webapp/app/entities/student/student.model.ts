import { ICourse } from 'app/entities/course/course.model';
import { IAttendance } from 'app/entities/attendance/attendance.model';
import { IInvoice } from 'app/entities/invoice/invoice.model';

export interface IStudent {
  id: number;
  name?: string | null;
  email?: string | null;
  phone?: string | null;
  description?: string | null;
  courses?: Pick<ICourse, 'id'>[] | null;
  attendance?: Pick<IAttendance, 'id'> | null;
  invoice?: Pick<IInvoice, 'id'> | null;
}

export type NewStudent = Omit<IStudent, 'id'> & { id: null };
