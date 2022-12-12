import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFactura, NewFactura } from '../factura.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFactura for edit and NewFacturaFormGroupInput for create.
 */
type FacturaFormGroupInput = IFactura | PartialWithRequiredKeyOf<NewFactura>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFactura | NewFactura> = Omit<T, 'fechaFactura'> & {
  fechaFactura?: string | null;
};

type FacturaFormRawValue = FormValueOf<IFactura>;

type NewFacturaFormRawValue = FormValueOf<NewFactura>;

type FacturaFormDefaults = Pick<NewFactura, 'id' | 'fechaFactura'>;

type FacturaFormGroupContent = {
  id: FormControl<FacturaFormRawValue['id'] | NewFactura['id']>;
  facturado: FormControl<FacturaFormRawValue['facturado']>;
  fechaFactura: FormControl<FacturaFormRawValue['fechaFactura']>;
  observaciones: FormControl<FacturaFormRawValue['observaciones']>;
  pagos: FormControl<FacturaFormRawValue['pagos']>;
  pago: FormControl<FacturaFormRawValue['pago']>;
};

export type FacturaFormGroup = FormGroup<FacturaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FacturaFormService {
  createFacturaFormGroup(factura: FacturaFormGroupInput = { id: null }): FacturaFormGroup {
    const facturaRawValue = this.convertFacturaToFacturaRawValue({
      ...this.getFormDefaults(),
      ...factura,
    });
    return new FormGroup<FacturaFormGroupContent>({
      id: new FormControl(
        { value: facturaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      facturado: new FormControl(facturaRawValue.facturado, {
        validators: [Validators.required],
      }),
      fechaFactura: new FormControl(facturaRawValue.fechaFactura, {
        validators: [Validators.required],
      }),
      observaciones: new FormControl(facturaRawValue.observaciones),
      pagos: new FormControl(facturaRawValue.pagos),
      pago: new FormControl(facturaRawValue.pago),
    });
  }

  getFactura(form: FacturaFormGroup): IFactura | NewFactura {
    return this.convertFacturaRawValueToFactura(form.getRawValue() as FacturaFormRawValue | NewFacturaFormRawValue);
  }

  resetForm(form: FacturaFormGroup, factura: FacturaFormGroupInput): void {
    const facturaRawValue = this.convertFacturaToFacturaRawValue({ ...this.getFormDefaults(), ...factura });
    form.reset(
      {
        ...facturaRawValue,
        id: { value: facturaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FacturaFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fechaFactura: currentTime,
    };
  }

  private convertFacturaRawValueToFactura(rawFactura: FacturaFormRawValue | NewFacturaFormRawValue): IFactura | NewFactura {
    return {
      ...rawFactura,
      fechaFactura: dayjs(rawFactura.fechaFactura, DATE_TIME_FORMAT),
    };
  }

  private convertFacturaToFacturaRawValue(
    factura: IFactura | (Partial<NewFactura> & FacturaFormDefaults)
  ): FacturaFormRawValue | PartialWithRequiredKeyOf<NewFacturaFormRawValue> {
    return {
      ...factura,
      fechaFactura: factura.fechaFactura ? factura.fechaFactura.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
