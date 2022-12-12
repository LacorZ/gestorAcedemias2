import dayjs from 'dayjs/esm';

export interface IAttendance {
  id: number;
  date?: dayjs.Dayjs | null;
}

export type NewAttendance = Omit<IAttendance, 'id'> & { id: null };
