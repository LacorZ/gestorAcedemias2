import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAsistencia, NewAsistencia } from '../asistencia.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAsistencia for edit and NewAsistenciaFormGroupInput for create.
 */
type AsistenciaFormGroupInput = IAsistencia | PartialWithRequiredKeyOf<NewAsistencia>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAsistencia | NewAsistencia> = Omit<T, 'horaEntrada' | 'horaSalida'> & {
  horaEntrada?: string | null;
  horaSalida?: string | null;
};

type AsistenciaFormRawValue = FormValueOf<IAsistencia>;

type NewAsistenciaFormRawValue = FormValueOf<NewAsistencia>;

type AsistenciaFormDefaults = Pick<NewAsistencia, 'id' | 'horaEntrada' | 'horaSalida'>;

type AsistenciaFormGroupContent = {
  id: FormControl<AsistenciaFormRawValue['id'] | NewAsistencia['id']>;
  fecha: FormControl<AsistenciaFormRawValue['fecha']>;
  estado: FormControl<AsistenciaFormRawValue['estado']>;
  horaEntrada: FormControl<AsistenciaFormRawValue['horaEntrada']>;
  horaSalida: FormControl<AsistenciaFormRawValue['horaSalida']>;
  observaciones: FormControl<AsistenciaFormRawValue['observaciones']>;
};

export type AsistenciaFormGroup = FormGroup<AsistenciaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AsistenciaFormService {
  createAsistenciaFormGroup(asistencia: AsistenciaFormGroupInput = { id: null }): AsistenciaFormGroup {
    const asistenciaRawValue = this.convertAsistenciaToAsistenciaRawValue({
      ...this.getFormDefaults(),
      ...asistencia,
    });
    return new FormGroup<AsistenciaFormGroupContent>({
      id: new FormControl(
        { value: asistenciaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      fecha: new FormControl(asistenciaRawValue.fecha, {
        validators: [Validators.required],
      }),
      estado: new FormControl(asistenciaRawValue.estado, {
        validators: [Validators.required],
      }),
      horaEntrada: new FormControl(asistenciaRawValue.horaEntrada),
      horaSalida: new FormControl(asistenciaRawValue.horaSalida),
      observaciones: new FormControl(asistenciaRawValue.observaciones),
    });
  }

  getAsistencia(form: AsistenciaFormGroup): IAsistencia | NewAsistencia {
    return this.convertAsistenciaRawValueToAsistencia(form.getRawValue() as AsistenciaFormRawValue | NewAsistenciaFormRawValue);
  }

  resetForm(form: AsistenciaFormGroup, asistencia: AsistenciaFormGroupInput): void {
    const asistenciaRawValue = this.convertAsistenciaToAsistenciaRawValue({ ...this.getFormDefaults(), ...asistencia });
    form.reset(
      {
        ...asistenciaRawValue,
        id: { value: asistenciaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AsistenciaFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      horaEntrada: currentTime,
      horaSalida: currentTime,
    };
  }

  private convertAsistenciaRawValueToAsistencia(
    rawAsistencia: AsistenciaFormRawValue | NewAsistenciaFormRawValue
  ): IAsistencia | NewAsistencia {
    return {
      ...rawAsistencia,
      horaEntrada: dayjs(rawAsistencia.horaEntrada, DATE_TIME_FORMAT),
      horaSalida: dayjs(rawAsistencia.horaSalida, DATE_TIME_FORMAT),
    };
  }

  private convertAsistenciaToAsistenciaRawValue(
    asistencia: IAsistencia | (Partial<NewAsistencia> & AsistenciaFormDefaults)
  ): AsistenciaFormRawValue | PartialWithRequiredKeyOf<NewAsistenciaFormRawValue> {
    return {
      ...asistencia,
      horaEntrada: asistencia.horaEntrada ? asistencia.horaEntrada.format(DATE_TIME_FORMAT) : undefined,
      horaSalida: asistencia.horaSalida ? asistencia.horaSalida.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
