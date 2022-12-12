import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICourse, NewCourse } from '../course.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICourse for edit and NewCourseFormGroupInput for create.
 */
type CourseFormGroupInput = ICourse | PartialWithRequiredKeyOf<NewCourse>;

type CourseFormDefaults = Pick<NewCourse, 'id' | 'tutors' | 'students'>;

type CourseFormGroupContent = {
  id: FormControl<ICourse['id'] | NewCourse['id']>;
  name: FormControl<ICourse['name']>;
  description: FormControl<ICourse['description']>;
  price: FormControl<ICourse['price']>;
  duration: FormControl<ICourse['duration']>;
  attendance: FormControl<ICourse['attendance']>;
  tutors: FormControl<ICourse['tutors']>;
  students: FormControl<ICourse['students']>;
};

export type CourseFormGroup = FormGroup<CourseFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CourseFormService {
  createCourseFormGroup(course: CourseFormGroupInput = { id: null }): CourseFormGroup {
    const courseRawValue = {
      ...this.getFormDefaults(),
      ...course,
    };
    return new FormGroup<CourseFormGroupContent>({
      id: new FormControl(
        { value: courseRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(courseRawValue.name, {
        validators: [Validators.required],
      }),
      description: new FormControl(courseRawValue.description),
      price: new FormControl(courseRawValue.price, {
        validators: [Validators.required],
      }),
      duration: new FormControl(courseRawValue.duration, {
        validators: [Validators.required],
      }),
      attendance: new FormControl(courseRawValue.attendance),
      tutors: new FormControl(courseRawValue.tutors ?? []),
      students: new FormControl(courseRawValue.students ?? []),
    });
  }

  getCourse(form: CourseFormGroup): ICourse | NewCourse {
    return form.getRawValue() as ICourse | NewCourse;
  }

  resetForm(form: CourseFormGroup, course: CourseFormGroupInput): void {
    const courseRawValue = { ...this.getFormDefaults(), ...course };
    form.reset(
      {
        ...courseRawValue,
        id: { value: courseRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CourseFormDefaults {
    return {
      id: null,
      tutors: [],
      students: [],
    };
  }
}
