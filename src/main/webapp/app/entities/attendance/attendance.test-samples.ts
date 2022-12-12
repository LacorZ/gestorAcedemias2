import dayjs from 'dayjs/esm';

import { IAttendance, NewAttendance } from './attendance.model';

export const sampleWithRequiredData: IAttendance = {
  id: 67192,
  date: dayjs('2022-12-12'),
};

export const sampleWithPartialData: IAttendance = {
  id: 80058,
  date: dayjs('2022-12-12'),
};

export const sampleWithFullData: IAttendance = {
  id: 79366,
  date: dayjs('2022-12-12'),
};

export const sampleWithNewData: NewAttendance = {
  date: dayjs('2022-12-12'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
