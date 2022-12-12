import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AsistenciaFormService } from './asistencia-form.service';
import { AsistenciaService } from '../service/asistencia.service';
import { IAsistencia } from '../asistencia.model';

import { AsistenciaUpdateComponent } from './asistencia-update.component';

describe('Asistencia Management Update Component', () => {
  let comp: AsistenciaUpdateComponent;
  let fixture: ComponentFixture<AsistenciaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let asistenciaFormService: AsistenciaFormService;
  let asistenciaService: AsistenciaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AsistenciaUpdateComponent],
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
      .overrideTemplate(AsistenciaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AsistenciaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    asistenciaFormService = TestBed.inject(AsistenciaFormService);
    asistenciaService = TestBed.inject(AsistenciaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const asistencia: IAsistencia = { id: 456 };

      activatedRoute.data = of({ asistencia });
      comp.ngOnInit();

      expect(comp.asistencia).toEqual(asistencia);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAsistencia>>();
      const asistencia = { id: 123 };
      jest.spyOn(asistenciaFormService, 'getAsistencia').mockReturnValue(asistencia);
      jest.spyOn(asistenciaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ asistencia });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: asistencia }));
      saveSubject.complete();

      // THEN
      expect(asistenciaFormService.getAsistencia).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(asistenciaService.update).toHaveBeenCalledWith(expect.objectContaining(asistencia));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAsistencia>>();
      const asistencia = { id: 123 };
      jest.spyOn(asistenciaFormService, 'getAsistencia').mockReturnValue({ id: null });
      jest.spyOn(asistenciaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ asistencia: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: asistencia }));
      saveSubject.complete();

      // THEN
      expect(asistenciaFormService.getAsistencia).toHaveBeenCalled();
      expect(asistenciaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAsistencia>>();
      const asistencia = { id: 123 };
      jest.spyOn(asistenciaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ asistencia });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(asistenciaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
