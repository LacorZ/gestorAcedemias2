import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { AttendanceFormService, AttendanceFormGroup } from './attendance-form.service';
import { IAttendance } from '../attendance.model';
import { AttendanceService } from '../service/attendance.service';

@Component({
  selector: 'jhi-attendance-update',
  templateUrl: './attendance-update.component.html',
})
export class AttendanceUpdateComponent implements OnInit {
  isSaving = false;
  attendance: IAttendance | null = null;

  editForm: AttendanceFormGroup = this.attendanceFormService.createAttendanceFormGroup();

  constructor(
    protected attendanceService: AttendanceService,
    protected attendanceFormService: AttendanceFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ attendance }) => {
      this.attendance = attendance;
      if (attendance) {
        this.updateForm(attendance);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const attendance = this.attendanceFormService.getAttendance(this.editForm);
    if (attendance.id !== null) {
      this.subscribeToSaveResponse(this.attendanceService.update(attendance));
    } else {
      this.subscribeToSaveResponse(this.attendanceService.create(attendance));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAttendance>>): void {
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

  protected updateForm(attendance: IAttendance): void {
    this.attendance = attendance;
    this.attendanceFormService.resetForm(this.editForm, attendance);
  }
}
