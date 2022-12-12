import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TutorFormService } from './tutor-form.service';
import { TutorService } from '../service/tutor.service';
import { ITutor } from '../tutor.model';
import { ICurso } from 'app/entities/curso/curso.model';
import { CursoService } from 'app/entities/curso/service/curso.service';

import { TutorUpdateComponent } from './tutor-update.component';

describe('Tutor Management Update Component', () => {
  let comp: TutorUpdateComponent;
  let fixture: ComponentFixture<TutorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tutorFormService: TutorFormService;
  let tutorService: TutorService;
  let cursoService: CursoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TutorUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(TutorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TutorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tutorFormService = TestBed.inject(TutorFormService);
    tutorService = TestBed.inject(TutorService);
    cursoService = TestBed.inject(CursoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Curso query and add missing value', () => {
      const tutor: ITutor = { id: 456 };
      const cursos: ICurso[] = [{ id: 59132 }];
      tutor.cursos = cursos;

      const cursoCollection: ICurso[] = [{ id: 71066 }];
      jest.spyOn(cursoService, 'query').mockReturnValue(of(new HttpResponse({ body: cursoCollection })));
      const additionalCursos = [...cursos];
      const expectedCollection: ICurso[] = [...additionalCursos, ...cursoCollection];
      jest.spyOn(cursoService, 'addCursoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tutor });
      comp.ngOnInit();

      expect(cursoService.query).toHaveBeenCalled();
      expect(cursoService.addCursoToCollectionIfMissing).toHaveBeenCalledWith(
        cursoCollection,
        ...additionalCursos.map(expect.objectContaining)
      );
      expect(comp.cursosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const tutor: ITutor = { id: 456 };
      const cursos: ICurso = { id: 93917 };
      tutor.cursos = [cursos];

      activatedRoute.data = of({ tutor });
      comp.ngOnInit();

      expect(comp.cursosSharedCollection).toContain(cursos);
      expect(comp.tutor).toEqual(tutor);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITutor>>();
      const tutor = { id: 123 };
      jest.spyOn(tutorFormService, 'getTutor').mockReturnValue(tutor);
      jest.spyOn(tutorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tutor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tutor }));
      saveSubject.complete();

      // THEN
      expect(tutorFormService.getTutor).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tutorService.update).toHaveBeenCalledWith(expect.objectContaining(tutor));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITutor>>();
      const tutor = { id: 123 };
      jest.spyOn(tutorFormService, 'getTutor').mockReturnValue({ id: null });
      jest.spyOn(tutorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tutor: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tutor }));
      saveSubject.complete();

      // THEN
      expect(tutorFormService.getTutor).toHaveBeenCalled();
      expect(tutorService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITutor>>();
      const tutor = { id: 123 };
      jest.spyOn(tutorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tutor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tutorService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCurso', () => {
      it('Should forward to cursoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(cursoService, 'compareCurso');
        comp.compareCurso(entity, entity2);
        expect(cursoService.compareCurso).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
