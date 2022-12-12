import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../padre.test-samples';

import { PadreFormService } from './padre-form.service';

describe('Padre Form Service', () => {
  let service: PadreFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PadreFormService);
  });

  describe('Service methods', () => {
    describe('createPadreFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPadreFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            email: expect.any(Object),
            telefono: expect.any(Object),
            observaciones: expect.any(Object),
            estudiantes: expect.any(Object),
          })
        );
      });

      it('passing IPadre should create a new form with FormGroup', () => {
        const formGroup = service.createPadreFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            email: expect.any(Object),
            telefono: expect.any(Object),
            observaciones: expect.any(Object),
            estudiantes: expect.any(Object),
          })
        );
      });
    });

    describe('getPadre', () => {
      it('should return NewPadre for default Padre initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPadreFormGroup(sampleWithNewData);

        const padre = service.getPadre(formGroup) as any;

        expect(padre).toMatchObject(sampleWithNewData);
      });

      it('should return NewPadre for empty Padre initial value', () => {
        const formGroup = service.createPadreFormGroup();

        const padre = service.getPadre(formGroup) as any;

        expect(padre).toMatchObject({});
      });

      it('should return IPadre', () => {
        const formGroup = service.createPadreFormGroup(sampleWithRequiredData);

        const padre = service.getPadre(formGroup) as any;

        expect(padre).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPadre should not enable id FormControl', () => {
        const formGroup = service.createPadreFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPadre should disable id FormControl', () => {
        const formGroup = service.createPadreFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
