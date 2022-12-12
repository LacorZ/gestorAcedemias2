import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../tutor.test-samples';

import { TutorFormService } from './tutor-form.service';

describe('Tutor Form Service', () => {
  let service: TutorFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TutorFormService);
  });

  describe('Service methods', () => {
    describe('createTutorFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTutorFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            email: expect.any(Object),
            telefono: expect.any(Object),
            observaciones: expect.any(Object),
            cursos: expect.any(Object),
          })
        );
      });

      it('passing ITutor should create a new form with FormGroup', () => {
        const formGroup = service.createTutorFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            email: expect.any(Object),
            telefono: expect.any(Object),
            observaciones: expect.any(Object),
            cursos: expect.any(Object),
          })
        );
      });
    });

    describe('getTutor', () => {
      it('should return NewTutor for default Tutor initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createTutorFormGroup(sampleWithNewData);

        const tutor = service.getTutor(formGroup) as any;

        expect(tutor).toMatchObject(sampleWithNewData);
      });

      it('should return NewTutor for empty Tutor initial value', () => {
        const formGroup = service.createTutorFormGroup();

        const tutor = service.getTutor(formGroup) as any;

        expect(tutor).toMatchObject({});
      });

      it('should return ITutor', () => {
        const formGroup = service.createTutorFormGroup(sampleWithRequiredData);

        const tutor = service.getTutor(formGroup) as any;

        expect(tutor).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITutor should not enable id FormControl', () => {
        const formGroup = service.createTutorFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTutor should disable id FormControl', () => {
        const formGroup = service.createTutorFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
