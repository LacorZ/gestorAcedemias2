import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPago, NewPago } from '../pago.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPago for edit and NewPagoFormGroupInput for create.
 */
type PagoFormGroupInput = IPago | PartialWithRequiredKeyOf<NewPago>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPago | NewPago> = Omit<T, 'fechaPago'> & {
  fechaPago?: string | null;
};

type PagoFormRawValue = FormValueOf<IPago>;

type NewPagoFormRawValue = FormValueOf<NewPago>;

type PagoFormDefaults = Pick<NewPago, 'id' | 'fechaPago'>;

type PagoFormGroupContent = {
  id: FormControl<PagoFormRawValue['id'] | NewPago['id']>;
  cantidad: FormControl<PagoFormRawValue['cantidad']>;
  fechaPago: FormControl<PagoFormRawValue['fechaPago']>;
  metodoPago: FormControl<PagoFormRawValue['metodoPago']>;
  observaciones: FormControl<PagoFormRawValue['observaciones']>;
};

export type PagoFormGroup = FormGroup<PagoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PagoFormService {
  createPagoFormGroup(pago: PagoFormGroupInput = { id: null }): PagoFormGroup {
    const pagoRawValue = this.convertPagoToPagoRawValue({
      ...this.getFormDefaults(),
      ...pago,
    });
    return new FormGroup<PagoFormGroupContent>({
      id: new FormControl(
        { value: pagoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      cantidad: new FormControl(pagoRawValue.cantidad, {
        validators: [Validators.required],
      }),
      fechaPago: new FormControl(pagoRawValue.fechaPago, {
        validators: [Validators.required],
      }),
      metodoPago: new FormControl(pagoRawValue.metodoPago, {
        validators: [Validators.required],
      }),
      observaciones: new FormControl(pagoRawValue.observaciones),
    });
  }

  getPago(form: PagoFormGroup): IPago | NewPago {
    return this.convertPagoRawValueToPago(form.getRawValue() as PagoFormRawValue | NewPagoFormRawValue);
  }

  resetForm(form: PagoFormGroup, pago: PagoFormGroupInput): void {
    const pagoRawValue = this.convertPagoToPagoRawValue({ ...this.getFormDefaults(), ...pago });
    form.reset(
      {
        ...pagoRawValue,
        id: { value: pagoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PagoFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fechaPago: currentTime,
    };
  }

  private convertPagoRawValueToPago(rawPago: PagoFormRawValue | NewPagoFormRawValue): IPago | NewPago {
    return {
      ...rawPago,
      fechaPago: dayjs(rawPago.fechaPago, DATE_TIME_FORMAT),
    };
  }

  private convertPagoToPagoRawValue(
    pago: IPago | (Partial<NewPago> & PagoFormDefaults)
  ): PagoFormRawValue | PartialWithRequiredKeyOf<NewPagoFormRawValue> {
    return {
      ...pago,
      fechaPago: pago.fechaPago ? pago.fechaPago.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
