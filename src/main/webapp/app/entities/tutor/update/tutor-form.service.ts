import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITutor, NewTutor } from '../tutor.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITutor for edit and NewTutorFormGroupInput for create.
 */
type TutorFormGroupInput = ITutor | PartialWithRequiredKeyOf<NewTutor>;

type TutorFormDefaults = Pick<NewTutor, 'id' | 'cursos'>;

type TutorFormGroupContent = {
  id: FormControl<ITutor['id'] | NewTutor['id']>;
  nombre: FormControl<ITutor['nombre']>;
  email: FormControl<ITutor['email']>;
  telefono: FormControl<ITutor['telefono']>;
  observaciones: FormControl<ITutor['observaciones']>;
  cursos: FormControl<ITutor['cursos']>;
};

export type TutorFormGroup = FormGroup<TutorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TutorFormService {
  createTutorFormGroup(tutor: TutorFormGroupInput = { id: null }): TutorFormGroup {
    const tutorRawValue = {
      ...this.getFormDefaults(),
      ...tutor,
    };
    return new FormGroup<TutorFormGroupContent>({
      id: new FormControl(
        { value: tutorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nombre: new FormControl(tutorRawValue.nombre, {
        validators: [Validators.required],
      }),
      email: new FormControl(tutorRawValue.email, {
        validators: [Validators.required],
      }),
      telefono: new FormControl(tutorRawValue.telefono),
      observaciones: new FormControl(tutorRawValue.observaciones),
      cursos: new FormControl(tutorRawValue.cursos ?? []),
    });
  }

  getTutor(form: TutorFormGroup): ITutor | NewTutor {
    return form.getRawValue() as ITutor | NewTutor;
  }

  resetForm(form: TutorFormGroup, tutor: TutorFormGroupInput): void {
    const tutorRawValue = { ...this.getFormDefaults(), ...tutor };
    form.reset(
      {
        ...tutorRawValue,
        id: { value: tutorRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TutorFormDefaults {
    return {
      id: null,
      cursos: [],
    };
  }
}
