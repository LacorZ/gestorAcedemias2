import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IStudent, NewStudent } from '../student.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IStudent for edit and NewStudentFormGroupInput for create.
 */
type StudentFormGroupInput = IStudent | PartialWithRequiredKeyOf<NewStudent>;

type StudentFormDefaults = Pick<NewStudent, 'id' | 'courses'>;

type StudentFormGroupContent = {
  id: FormControl<IStudent['id'] | NewStudent['id']>;
  name: FormControl<IStudent['name']>;
  email: FormControl<IStudent['email']>;
  phone: FormControl<IStudent['phone']>;
  description: FormControl<IStudent['description']>;
  courses: FormControl<IStudent['courses']>;
  attendance: FormControl<IStudent['attendance']>;
  invoice: FormControl<IStudent['invoice']>;
};

export type StudentFormGroup = FormGroup<StudentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class StudentFormService {
  createStudentFormGroup(student: StudentFormGroupInput = { id: null }): StudentFormGroup {
    const studentRawValue = {
      ...this.getFormDefaults(),
      ...student,
    };
    return new FormGroup<StudentFormGroupContent>({
      id: new FormControl(
        { value: studentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(studentRawValue.name, {
        validators: [Validators.required],
      }),
      email: new FormControl(studentRawValue.email, {
        validators: [Validators.required],
      }),
      phone: new FormControl(studentRawValue.phone),
      description: new FormControl(studentRawValue.description),
      courses: new FormControl(studentRawValue.courses ?? []),
      attendance: new FormControl(studentRawValue.attendance),
      invoice: new FormControl(studentRawValue.invoice),
    });
  }

  getStudent(form: StudentFormGroup): IStudent | NewStudent {
    return form.getRawValue() as IStudent | NewStudent;
  }

  resetForm(form: StudentFormGroup, student: StudentFormGroupInput): void {
    const studentRawValue = { ...this.getFormDefaults(), ...student };
    form.reset(
      {
        ...studentRawValue,
        id: { value: studentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): StudentFormDefaults {
    return {
      id: null,
      courses: [],
    };
  }
}
