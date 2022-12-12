import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPadre, NewPadre } from '../padre.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPadre for edit and NewPadreFormGroupInput for create.
 */
type PadreFormGroupInput = IPadre | PartialWithRequiredKeyOf<NewPadre>;

type PadreFormDefaults = Pick<NewPadre, 'id' | 'estudiantes'>;

type PadreFormGroupContent = {
  id: FormControl<IPadre['id'] | NewPadre['id']>;
  nombre: FormControl<IPadre['nombre']>;
  email: FormControl<IPadre['email']>;
  telefono: FormControl<IPadre['telefono']>;
  observaciones: FormControl<IPadre['observaciones']>;
  estudiantes: FormControl<IPadre['estudiantes']>;
};

export type PadreFormGroup = FormGroup<PadreFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PadreFormService {
  createPadreFormGroup(padre: PadreFormGroupInput = { id: null }): PadreFormGroup {
    const padreRawValue = {
      ...this.getFormDefaults(),
      ...padre,
    };
    return new FormGroup<PadreFormGroupContent>({
      id: new FormControl(
        { value: padreRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nombre: new FormControl(padreRawValue.nombre, {
        validators: [Validators.required],
      }),
      email: new FormControl(padreRawValue.email, {
        validators: [Validators.required],
      }),
      telefono: new FormControl(padreRawValue.telefono),
      observaciones: new FormControl(padreRawValue.observaciones),
      estudiantes: new FormControl(padreRawValue.estudiantes ?? []),
    });
  }

  getPadre(form: PadreFormGroup): IPadre | NewPadre {
    return form.getRawValue() as IPadre | NewPadre;
  }

  resetForm(form: PadreFormGroup, padre: PadreFormGroupInput): void {
    const padreRawValue = { ...this.getFormDefaults(), ...padre };
    form.reset(
      {
        ...padreRawValue,
        id: { value: padreRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PadreFormDefaults {
    return {
      id: null,
      estudiantes: [],
    };
  }
}
