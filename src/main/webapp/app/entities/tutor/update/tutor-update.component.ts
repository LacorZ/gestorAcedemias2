import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { TutorFormService, TutorFormGroup } from './tutor-form.service';
import { ITutor } from '../tutor.model';
import { TutorService } from '../service/tutor.service';
import { ICurso } from 'app/entities/curso/curso.model';
import { CursoService } from 'app/entities/curso/service/curso.service';

@Component({
  selector: 'jhi-tutor-update',
  templateUrl: './tutor-update.component.html',
})
export class TutorUpdateComponent implements OnInit {
  isSaving = false;
  tutor: ITutor | null = null;

  cursosSharedCollection: ICurso[] = [];

  editForm: TutorFormGroup = this.tutorFormService.createTutorFormGroup();

  constructor(
    protected tutorService: TutorService,
    protected tutorFormService: TutorFormService,
    protected cursoService: CursoService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCurso = (o1: ICurso | null, o2: ICurso | null): boolean => this.cursoService.compareCurso(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tutor }) => {
      this.tutor = tutor;
      if (tutor) {
        this.updateForm(tutor);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tutor = this.tutorFormService.getTutor(this.editForm);
    if (tutor.id !== null) {
      this.subscribeToSaveResponse(this.tutorService.update(tutor));
    } else {
      this.subscribeToSaveResponse(this.tutorService.create(tutor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITutor>>): void {
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

  protected updateForm(tutor: ITutor): void {
    this.tutor = tutor;
    this.tutorFormService.resetForm(this.editForm, tutor);

    this.cursosSharedCollection = this.cursoService.addCursoToCollectionIfMissing<ICurso>(
      this.cursosSharedCollection,
      ...(tutor.cursos ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.cursoService
      .query()
      .pipe(map((res: HttpResponse<ICurso[]>) => res.body ?? []))
      .pipe(map((cursos: ICurso[]) => this.cursoService.addCursoToCollectionIfMissing<ICurso>(cursos, ...(this.tutor?.cursos ?? []))))
      .subscribe((cursos: ICurso[]) => (this.cursosSharedCollection = cursos));
  }
}
