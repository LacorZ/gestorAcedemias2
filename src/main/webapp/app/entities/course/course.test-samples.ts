import { ICourse, NewCourse } from './course.model';

export const sampleWithRequiredData: ICourse = {
  id: 59109,
  name: 'multi-byte Aragón',
  price: 77622,
  duration: 61968,
};

export const sampleWithPartialData: ICourse = {
  id: 65901,
  name: 'Refinado',
  description: 'Morado transicional',
  price: 16668,
  duration: 43888,
};

export const sampleWithFullData: ICourse = {
  id: 32141,
  name: 'pixel por e-commerce',
  description: 'Savings synthesize',
  price: 83067,
  duration: 83719,
};

export const sampleWithNewData: NewCourse = {
  name: 'Facilitador real-time',
  price: 77532,
  duration: 71466,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
