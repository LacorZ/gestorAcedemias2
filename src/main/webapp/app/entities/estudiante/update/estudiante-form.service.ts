import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEstudiante, NewEstudiante } from '../estudiante.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEstudiante for edit and NewEstudianteFormGroupInput for create.
 */
type EstudianteFormGroupInput = IEstudiante | PartialWithRequiredKeyOf<NewEstudiante>;

type EstudianteFormDefaults = Pick<NewEstudiante, 'id' | 'cursos' | 'padres'>;

type EstudianteFormGroupContent = {
  id: FormControl<IEstudiante['id'] | NewEstudiante['id']>;
  nombre: FormControl<IEstudiante['nombre']>;
  email: FormControl<IEstudiante['email']>;
  telefono: FormControl<IEstudiante['telefono']>;
  observaciones: FormControl<IEstudiante['observaciones']>;
  asistencias: FormControl<IEstudiante['asistencias']>;
  facturas: FormControl<IEstudiante['facturas']>;
  cursos: FormControl<IEstudiante['cursos']>;
  asistencia: FormControl<IEstudiante['asistencia']>;
  factura: FormControl<IEstudiante['factura']>;
  padres: FormControl<IEstudiante['padres']>;
};

export type EstudianteFormGroup = FormGroup<EstudianteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EstudianteFormService {
  createEstudianteFormGroup(estudiante: EstudianteFormGroupInput = { id: null }): EstudianteFormGroup {
    const estudianteRawValue = {
      ...this.getFormDefaults(),
      ...estudiante,
    };
    return new FormGroup<EstudianteFormGroupContent>({
      id: new FormControl(
        { value: estudianteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nombre: new FormControl(estudianteRawValue.nombre, {
        validators: [Validators.required],
      }),
      email: new FormControl(estudianteRawValue.email, {
        validators: [Validators.required],
      }),
      telefono: new FormControl(estudianteRawValue.telefono),
      observaciones: new FormControl(estudianteRawValue.observaciones),
      asistencias: new FormControl(estudianteRawValue.asistencias),
      facturas: new FormControl(estudianteRawValue.facturas),
      cursos: new FormControl(estudianteRawValue.cursos ?? []),
      asistencia: new FormControl(estudianteRawValue.asistencia),
      factura: new FormControl(estudianteRawValue.factura),
      padres: new FormControl(estudianteRawValue.padres ?? []),
    });
  }

  getEstudiante(form: EstudianteFormGroup): IEstudiante | NewEstudiante {
    return form.getRawValue() as IEstudiante | NewEstudiante;
  }

  resetForm(form: EstudianteFormGroup, estudiante: EstudianteFormGroupInput): void {
    const estudianteRawValue = { ...this.getFormDefaults(), ...estudiante };
    form.reset(
      {
        ...estudianteRawValue,
        id: { value: estudianteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EstudianteFormDefaults {
    return {
      id: null,
      cursos: [],
      padres: [],
    };
  }
}
