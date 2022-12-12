import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CourseFormService, CourseFormGroup } from './course-form.service';
import { ICourse } from '../course.model';
import { CourseService } from '../service/course.service';
import { IAttendance } from 'app/entities/attendance/attendance.model';
import { AttendanceService } from 'app/entities/attendance/service/attendance.service';

@Component({
  selector: 'jhi-course-update',
  templateUrl: './course-update.component.html',
})
export class CourseUpdateComponent implements OnInit {
  isSaving = false;
  course: ICourse | null = null;

  attendancesSharedCollection: IAttendance[] = [];

  editForm: CourseFormGroup = this.courseFormService.createCourseFormGroup();

  constructor(
    protected courseService: CourseService,
    protected courseFormService: CourseFormService,
    protected attendanceService: AttendanceService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareAttendance = (o1: IAttendance | null, o2: IAttendance | null): boolean => this.attendanceService.compareAttendance(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ course }) => {
      this.course = course;
      if (course) {
        this.updateForm(course);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const course = this.courseFormService.getCourse(this.editForm);
    if (course.id !== null) {
      this.subscribeToSaveResponse(this.courseService.update(course));
    } else {
      this.subscribeToSaveResponse(this.courseService.create(course));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICourse>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(course: ICourse): void {
    this.course = course;
    this.courseFormService.resetForm(this.editForm, course);

    this.attendancesSharedCollection = this.attendanceService.addAttendanceToCollectionIfMissing<IAttendance>(
      this.attendancesSharedCollection,
      course.attendance
    );
  }

  protected loadRelationshipsOptions(): void {
    this.attendanceService
      .query()
      .pipe(map((res: HttpResponse<IAttendance[]>) => res.body ?? []))
      .pipe(
        map((attendances: IAttendance[]) =>
          this.attendanceService.addAttendanceToCollectionIfMissing<IAttendance>(attendances, this.course?.attendance)
        )
      )
      .subscribe((attendances: IAttendance[]) => (this.attendancesSharedCollection = attendances));
  }
}
