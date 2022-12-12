import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICurso, NewCurso } from '../curso.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICurso for edit and NewCursoFormGroupInput for create.
 */
type CursoFormGroupInput = ICurso | PartialWithRequiredKeyOf<NewCurso>;

type CursoFormDefaults = Pick<NewCurso, 'id' | 'tutors' | 'estudiantes'>;

type CursoFormGroupContent = {
  id: FormControl<ICurso['id'] | NewCurso['id']>;
  nombre: FormControl<ICurso['nombre']>;
  descripcion: FormControl<ICurso['descripcion']>;
  price: FormControl<ICurso['price']>;
  fechaInicio: FormControl<ICurso['fechaInicio']>;
  fechaFin: FormControl<ICurso['fechaFin']>;
  observaciones: FormControl<ICurso['observaciones']>;
  asistencias: FormControl<ICurso['asistencias']>;
  asistencia: FormControl<ICurso['asistencia']>;
  tutors: FormControl<ICurso['tutors']>;
  estudiantes: FormControl<ICurso['estudiantes']>;
};

export type CursoFormGroup = FormGroup<CursoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CursoFormService {
  createCursoFormGroup(curso: CursoFormGroupInput = { id: null }): CursoFormGroup {
    const cursoRawValue = {
      ...this.getFormDefaults(),
      ...curso,
    };
    return new FormGroup<CursoFormGroupContent>({
      id: new FormControl(
        { value: cursoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nombre: new FormControl(cursoRawValue.nombre, {
        validators: [Validators.required],
      }),
      descripcion: new FormControl(cursoRawValue.descripcion),
      price: new FormControl(cursoRawValue.price, {
        validators: [Validators.required],
      }),
      fechaInicio: new FormControl(cursoRawValue.fechaInicio),
      fechaFin: new FormControl(cursoRawValue.fechaFin),
      observaciones: new FormControl(cursoRawValue.observaciones),
      asistencias: new FormControl(cursoRawValue.asistencias),
      asistencia: new FormControl(cursoRawValue.asistencia),
      tutors: new FormControl(cursoRawValue.tutors ?? []),
      estudiantes: new FormControl(cursoRawValue.estudiantes ?? []),
    });
  }

  getCurso(form: CursoFormGroup): ICurso | NewCurso {
    return form.getRawValue() as ICurso | NewCurso;
  }

  resetForm(form: CursoFormGroup, curso: CursoFormGroupInput): void {
    const cursoRawValue = { ...this.getFormDefaults(), ...curso };
    form.reset(
      {
        ...cursoRawValue,
        id: { value: cursoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CursoFormDefaults {
    return {
      id: null,
      tutors: [],
      estudiantes: [],
    };
  }
}
