import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EstudianteFormService, EstudianteFormGroup } from './estudiante-form.service';
import { IEstudiante } from '../estudiante.model';
import { EstudianteService } from '../service/estudiante.service';
import { IAsistencia } from 'app/entities/asistencia/asistencia.model';
import { AsistenciaService } from 'app/entities/asistencia/service/asistencia.service';
import { IFactura } from 'app/entities/factura/factura.model';
import { FacturaService } from 'app/entities/factura/service/factura.service';
import { ICurso } from 'app/entities/curso/curso.model';
import { CursoService } from 'app/entities/curso/service/curso.service';

@Component({
  selector: 'jhi-estudiante-update',
  templateUrl: './estudiante-update.component.html',
})
export class EstudianteUpdateComponent implements OnInit {
  isSaving = false;
  estudiante: IEstudiante | null = null;

  asistenciasSharedCollection: IAsistencia[] = [];
  facturasSharedCollection: IFactura[] = [];
  cursosSharedCollection: ICurso[] = [];

  editForm: EstudianteFormGroup = this.estudianteFormService.createEstudianteFormGroup();

  constructor(
    protected estudianteService: EstudianteService,
    protected estudianteFormService: EstudianteFormService,
    protected asistenciaService: AsistenciaService,
    protected facturaService: FacturaService,
    protected cursoService: CursoService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareAsistencia = (o1: IAsistencia | null, o2: IAsistencia | null): boolean => this.asistenciaService.compareAsistencia(o1, o2);

  compareFactura = (o1: IFactura | null, o2: IFactura | null): boolean => this.facturaService.compareFactura(o1, o2);

  compareCurso = (o1: ICurso | null, o2: ICurso | null): boolean => this.cursoService.compareCurso(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ estudiante }) => {
      this.estudiante = estudiante;
      if (estudiante) {
        this.updateForm(estudiante);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const estudiante = this.estudianteFormService.getEstudiante(this.editForm);
    if (estudiante.id !== null) {
      this.subscribeToSaveResponse(this.estudianteService.update(estudiante));
    } else {
      this.subscribeToSaveResponse(this.estudianteService.create(estudiante));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEstudiante>>): void {
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

  protected updateForm(estudiante: IEstudiante): void {
    this.estudiante = estudiante;
    this.estudianteFormService.resetForm(this.editForm, estudiante);

    this.asistenciasSharedCollection = this.asistenciaService.addAsistenciaToCollectionIfMissing<IAsistencia>(
      this.asistenciasSharedCollection,
      estudiante.asistencias,
      estudiante.asistencia
    );
    this.facturasSharedCollection = this.facturaService.addFacturaToCollectionIfMissing<IFactura>(
      this.facturasSharedCollection,
      estudiante.facturas,
      estudiante.factura
    );
    this.cursosSharedCollection = this.cursoService.addCursoToCollectionIfMissing<ICurso>(
      this.cursosSharedCollection,
      ...(estudiante.cursos ?? [])
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
            this.estudiante?.asistencias,
            this.estudiante?.asistencia
          )
        )
      )
      .subscribe((asistencias: IAsistencia[]) => (this.asistenciasSharedCollection = asistencias));

    this.facturaService
      .query()
      .pipe(map((res: HttpResponse<IFactura[]>) => res.body ?? []))
      .pipe(
        map((facturas: IFactura[]) =>
          this.facturaService.addFacturaToCollectionIfMissing<IFactura>(facturas, this.estudiante?.facturas, this.estudiante?.factura)
        )
      )
      .subscribe((facturas: IFactura[]) => (this.facturasSharedCollection = facturas));

    this.cursoService
      .query()
      .pipe(map((res: HttpResponse<ICurso[]>) => res.body ?? []))
      .pipe(map((cursos: ICurso[]) => this.cursoService.addCursoToCollectionIfMissing<ICurso>(cursos, ...(this.estudiante?.cursos ?? []))))
      .subscribe((cursos: ICurso[]) => (this.cursosSharedCollection = cursos));
  }
}
