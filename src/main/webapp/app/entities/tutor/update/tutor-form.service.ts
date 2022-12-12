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

type TutorFormDefaults = Pick<NewTutor, 'id' | 'courses'>;

type TutorFormGroupContent = {
  id: FormControl<ITutor['id'] | NewTutor['id']>;
  name: FormControl<ITutor['name']>;
  email: FormControl<ITutor['email']>;
  phone: FormControl<ITutor['phone']>;
  description: FormControl<ITutor['description']>;
  photo: FormControl<ITutor['photo']>;
  photoContentType: FormControl<ITutor['photoContentType']>;
  courses: FormControl<ITutor['courses']>;
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
      name: new FormControl(tutorRawValue.name, {
        validators: [Validators.required],
      }),
      email: new FormControl(tutorRawValue.email, {
        validators: [Validators.required],
      }),
      phone: new FormControl(tutorRawValue.phone),
      description: new FormControl(tutorRawValue.description),
      photo: new FormControl(tutorRawValue.photo),
      photoContentType: new FormControl(tutorRawValue.photoContentType),
      courses: new FormControl(tutorRawValue.courses ?? []),
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
      courses: [],
    };
  }
}
