import { IStudent, NewStudent } from './student.model';

export const sampleWithRequiredData: IStudent = {
  id: 23105,
  name: 'Franc División Nacional',
  email: 'Eduardo_Tejeda@yahoo.com',
};

export const sampleWithPartialData: IStudent = {
  id: 33985,
  name: 'transmit',
  email: 'Ins62@yahoo.com',
  phone: '927 334 554',
};

export const sampleWithFullData: IStudent = {
  id: 59186,
  name: 'auxiliary Marroquinería Savings',
  email: 'Anita30@gmail.com',
  phone: '981-298-166',
  description: 'Joyería',
};

export const sampleWithNewData: NewStudent = {
  name: 'connect Buckinghamshire',
  email: 'Reina.Baeza70@yahoo.com',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
