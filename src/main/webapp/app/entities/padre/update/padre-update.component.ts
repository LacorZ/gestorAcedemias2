import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PadreFormService, PadreFormGroup } from './padre-form.service';
import { IPadre } from '../padre.model';
import { PadreService } from '../service/padre.service';
import { IEstudiante } from 'app/entities/estudiante/estudiante.model';
import { EstudianteService } from 'app/entities/estudiante/service/estudiante.service';

@Component({
  selector: 'jhi-padre-update',
  templateUrl: './padre-update.component.html',
})
export class PadreUpdateComponent implements OnInit {
  isSaving = false;
  padre: IPadre | null = null;

  estudiantesSharedCollection: IEstudiante[] = [];

  editForm: PadreFormGroup = this.padreFormService.createPadreFormGroup();

  constructor(
    protected padreService: PadreService,
    protected padreFormService: PadreFormService,
    protected estudianteService: EstudianteService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEstudiante = (o1: IEstudiante | null, o2: IEstudiante | null): boolean => this.estudianteService.compareEstudiante(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ padre }) => {
      this.padre = padre;
      if (padre) {
        this.updateForm(padre);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const padre = this.padreFormService.getPadre(this.editForm);
    if (padre.id !== null) {
      this.subscribeToSaveResponse(this.padreService.update(padre));
    } else {
      this.subscribeToSaveResponse(this.padreService.create(padre));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPadre>>): void {
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

  protected updateForm(padre: IPadre): void {
    this.padre = padre;
    this.padreFormService.resetForm(this.editForm, padre);

    this.estudiantesSharedCollection = this.estudianteService.addEstudianteToCollectionIfMissing<IEstudiante>(
      this.estudiantesSharedCollection,
      ...(padre.estudiantes ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.estudianteService
      .query()
      .pipe(map((res: HttpResponse<IEstudiante[]>) => res.body ?? []))
      .pipe(
        map((estudiantes: IEstudiante[]) =>
          this.estudianteService.addEstudianteToCollectionIfMissing<IEstudiante>(estudiantes, ...(this.padre?.estudiantes ?? []))
        )
      )
      .subscribe((estudiantes: IEstudiante[]) => (this.estudiantesSharedCollection = estudiantes));
  }
}
