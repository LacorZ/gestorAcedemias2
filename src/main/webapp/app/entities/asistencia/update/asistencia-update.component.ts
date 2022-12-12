import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { AsistenciaFormService, AsistenciaFormGroup } from './asistencia-form.service';
import { IAsistencia } from '../asistencia.model';
import { AsistenciaService } from '../service/asistencia.service';
import { AsistenciaEstado } from 'app/entities/enumerations/asistencia-estado.model';

@Component({
  selector: 'jhi-asistencia-update',
  templateUrl: './asistencia-update.component.html',
})
export class AsistenciaUpdateComponent implements OnInit {
  isSaving = false;
  asistencia: IAsistencia | null = null;
  asistenciaEstadoValues = Object.keys(AsistenciaEstado);

  editForm: AsistenciaFormGroup = this.asistenciaFormService.createAsistenciaFormGroup();

  constructor(
    protected asistenciaService: AsistenciaService,
    protected asistenciaFormService: AsistenciaFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ asistencia }) => {
      this.asistencia = asistencia;
      if (asistencia) {
        this.updateForm(asistencia);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const asistencia = this.asistenciaFormService.getAsistencia(this.editForm);
    if (asistencia.id !== null) {
      this.subscribeToSaveResponse(this.asistenciaService.update(asistencia));
    } else {
      this.subscribeToSaveResponse(this.asistenciaService.create(asistencia));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAsistencia>>): void {
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

  protected updateForm(asistencia: IAsistencia): void {
    this.asistencia = asistencia;
    this.asistenciaFormService.resetForm(this.editForm, asistencia);
  }
}
