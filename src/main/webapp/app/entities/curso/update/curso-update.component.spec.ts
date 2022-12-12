import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CursoFormService } from './curso-form.service';
import { CursoService } from '../service/curso.service';
import { ICurso } from '../curso.model';
import { IAsistencia } from 'app/entities/asistencia/asistencia.model';
import { AsistenciaService } from 'app/entities/asistencia/service/asistencia.service';

import { CursoUpdateComponent } from './curso-update.component';

describe('Curso Management Update Component', () => {
  let comp: CursoUpdateComponent;
  let fixture: ComponentFixture<CursoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cursoFormService: CursoFormService;
  let cursoService: CursoService;
  let asistenciaService: AsistenciaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CursoUpdateComponent],
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
      .overrideTemplate(CursoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CursoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cursoFormService = TestBed.inject(CursoFormService);
    cursoService = TestBed.inject(CursoService);
    asistenciaService = TestBed.inject(AsistenciaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Asistencia query and add missing value', () => {
      const curso: ICurso = { id: 456 };
      const asistencias: IAsistencia = { id: 87238 };
      curso.asistencias = asistencias;
      const asistencia: IAsistencia = { id: 54323 };
      curso.asistencia = asistencia;

      const asistenciaCollection: IAsistencia[] = [{ id: 8447 }];
      jest.spyOn(asistenciaService, 'query').mockReturnValue(of(new HttpResponse({ body: asistenciaCollection })));
      const additionalAsistencias = [asistencias, asistencia];
      const expectedCollection: IAsistencia[] = [...additionalAsistencias, ...asistenciaCollection];
      jest.spyOn(asistenciaService, 'addAsistenciaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ curso });
      comp.ngOnInit();

      expect(asistenciaService.query).toHaveBeenCalled();
      expect(asistenciaService.addAsistenciaToCollectionIfMissing).toHaveBeenCalledWith(
        asistenciaCollection,
        ...additionalAsistencias.map(expect.objectContaining)
      );
      expect(comp.asistenciasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const curso: ICurso = { id: 456 };
      const asistencias: IAsistencia = { id: 70980 };
      curso.asistencias = asistencias;
      const asistencia: IAsistencia = { id: 99826 };
      curso.asistencia = asistencia;

      activatedRoute.data = of({ curso });
      comp.ngOnInit();

      expect(comp.asistenciasSharedCollection).toContain(asistencias);
      expect(comp.asistenciasSharedCollection).toContain(asistencia);
      expect(comp.curso).toEqual(curso);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICurso>>();
      const curso = { id: 123 };
      jest.spyOn(cursoFormService, 'getCurso').mockReturnValue(curso);
      jest.spyOn(cursoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ curso });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: curso }));
      saveSubject.complete();

      // THEN
      expect(cursoFormService.getCurso).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cursoService.update).toHaveBeenCalledWith(expect.objectContaining(curso));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICurso>>();
      const curso = { id: 123 };
      jest.spyOn(cursoFormService, 'getCurso').mockReturnValue({ id: null });
      jest.spyOn(cursoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ curso: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: curso }));
      saveSubject.complete();

      // THEN
      expect(cursoFormService.getCurso).toHaveBeenCalled();
      expect(cursoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICurso>>();
      const curso = { id: 123 };
      jest.spyOn(cursoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ curso });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cursoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAsistencia', () => {
      it('Should forward to asistenciaService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(asistenciaService, 'compareAsistencia');
        comp.compareAsistencia(entity, entity2);
        expect(asistenciaService.compareAsistencia).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
