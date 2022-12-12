import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CursoFormService, CursoFormGroup } from './curso-form.service';
import { ICurso } from '../curso.model';
import { CursoService } from '../service/curso.service';
import { IAsistencia } from 'app/entities/asistencia/asistencia.model';
import { AsistenciaService } from 'app/entities/asistencia/service/asistencia.service';

@Component({
  selector: 'jhi-curso-update',
  templateUrl: './curso-update.component.html',
})
export class CursoUpdateComponent implements OnInit {
  isSaving = false;
  curso: ICurso | null = null;

  asistenciasSharedCollection: IAsistencia[] = [];

  editForm: CursoFormGroup = this.cursoFormService.createCursoFormGroup();

  constructor(
    protected cursoService: CursoService,
    protected cursoFormService: CursoFormService,
    protected asistenciaService: AsistenciaService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareAsistencia = (o1: IAsistencia | null, o2: IAsistencia | null): boolean => this.asistenciaService.compareAsistencia(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ curso }) => {
      this.curso = curso;
      if (curso) {
        this.updateForm(curso);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const curso = this.cursoFormService.getCurso(this.editForm);
    if (curso.id !== null) {
      this.subscribeToSaveResponse(this.cursoService.update(curso));
    } else {
      this.subscribeToSaveResponse(this.cursoService.create(curso));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICurso>>): void {
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

  protected updateForm(curso: ICurso): void {
    this.curso = curso;
    this.cursoFormService.resetForm(this.editForm, curso);

    this.asistenciasSharedCollection = this.asistenciaService.addAsistenciaToCollectionIfMissing<IAsistencia>(
      this.asistenciasSharedCollection,
      curso.asistencias,
      curso.asistencia
    );
  }

  protected loadRelationshipsOptions(): void {
    this.asistenciaService
      .query()
      .pipe(map((res: HttpResponse<IAsistencia[]>) => res.body ?? []))
      .pipe(
        map((asistencias: IAsistencia[]) =>
          this.asistenciaService.addAsistenciaToCollectionIfMissing<IAsistencia>(
            asistencias,
            this.curso?.asistencias,
            this.curso?.asistencia
          )
        )
      )
      .subscribe((asistencias: IAsistencia[]) => (this.asistenciasSharedCollection = asistencias));
  }
}
