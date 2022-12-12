import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EstudianteFormService } from './estudiante-form.service';
import { EstudianteService } from '../service/estudiante.service';
import { IEstudiante } from '../estudiante.model';
import { IAsistencia } from 'app/entities/asistencia/asistencia.model';
import { AsistenciaService } from 'app/entities/asistencia/service/asistencia.service';
import { IFactura } from 'app/entities/factura/factura.model';
import { FacturaService } from 'app/entities/factura/service/factura.service';
import { ICurso } from 'app/entities/curso/curso.model';
import { CursoService } from 'app/entities/curso/service/curso.service';

import { EstudianteUpdateComponent } from './estudiante-update.component';

describe('Estudiante Management Update Component', () => {
  let comp: EstudianteUpdateComponent;
  let fixture: ComponentFixture<EstudianteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let estudianteFormService: EstudianteFormService;
  let estudianteService: EstudianteService;
  let asistenciaService: AsistenciaService;
  let facturaService: FacturaService;
  let cursoService: CursoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EstudianteUpdateComponent],
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
      .overrideTemplate(EstudianteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EstudianteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    estudianteFormService = TestBed.inject(EstudianteFormService);
    estudianteService = TestBed.inject(EstudianteService);
    asistenciaService = TestBed.inject(AsistenciaService);
    facturaService = TestBed.inject(FacturaService);
    cursoService = TestBed.inject(CursoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Asistencia query and add missing value', () => {
      const estudiante: IEstudiante = { id: 456 };
      const asistencias: IAsistencia = { id: 62710 };
      estudiante.asistencias = asistencias;
      const asistencia: IAsistencia = { id: 67591 };
      estudiante.asistencia = asistencia;

      const asistenciaCollection: IAsistencia[] = [{ id: 90081 }];
      jest.spyOn(asistenciaService, 'query').mockReturnValue(of(new HttpResponse({ body: asistenciaCollection })));
      const additionalAsistencias = [asistencias, asistencia];
      const expectedCollection: IAsistencia[] = [...additionalAsistencias, ...asistenciaCollection];
      jest.spyOn(asistenciaService, 'addAsistenciaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ estudiante });
      comp.ngOnInit();

      expect(asistenciaService.query).toHaveBeenCalled();
      expect(asistenciaService.addAsistenciaToCollectionIfMissing).toHaveBeenCalledWith(
        asistenciaCollection,
        ...additionalAsistencias.map(expect.objectContaining)
      );
      expect(comp.asistenciasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Factura query and add missing value', () => {
      const estudiante: IEstudiante = { id: 456 };
      const facturas: IFactura = { id: 8797 };
      estudiante.facturas = facturas;
      const factura: IFactura = { id: 85590 };
      estudiante.factura = factura;

      const facturaCollection: IFactura[] = [{ id: 99883 }];
      jest.spyOn(facturaService, 'query').mockReturnValue(of(new HttpResponse({ body: facturaCollection })));
      const additionalFacturas = [facturas, factura];
      const expectedCollection: IFactura[] = [...additionalFacturas, ...facturaCollection];
      jest.spyOn(facturaService, 'addFacturaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ estudiante });
      comp.ngOnInit();

      expect(facturaService.query).toHaveBeenCalled();
      expect(facturaService.addFacturaToCollectionIfMissing).toHaveBeenCalledWith(
        facturaCollection,
        ...additionalFacturas.map(expect.objectContaining)
      );
      expect(comp.facturasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Curso query and add missing value', () => {
      const estudiante: IEstudiante = { id: 456 };
      const cursos: ICurso[] = [{ id: 89189 }];
      estudiante.cursos = cursos;

      const cursoCollection: ICurso[] = [{ id: 56829 }];
      jest.spyOn(cursoService, 'query').mockReturnValue(of(new HttpResponse({ body: cursoCollection })));
      const additionalCursos = [...cursos];
      const expectedCollection: ICurso[] = [...additionalCursos, ...cursoCollection];
      jest.spyOn(cursoService, 'addCursoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ estudiante });
      comp.ngOnInit();

      expect(cursoService.query).toHaveBeenCalled();
      expect(cursoService.addCursoToCollectionIfMissing).toHaveBeenCalledWith(
        cursoCollection,
        ...additionalCursos.map(expect.objectContaining)
      );
      expect(comp.cursosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const estudiante: IEstudiante = { id: 456 };
      const asistencias: IAsistencia = { id: 83653 };
      estudiante.asistencias = asistencias;
      const asistencia: IAsistencia = { id: 11181 };
      estudiante.asistencia = asistencia;
      const facturas: IFactura = { id: 57758 };
      estudiante.facturas = facturas;
      const factura: IFactura = { id: 67666 };
      estudiante.factura = factura;
      const cursos: ICurso = { id: 38570 };
      estudiante.cursos = [cursos];

      activatedRoute.data = of({ estudiante });
      comp.ngOnInit();

      expect(comp.asistenciasSharedCollection).toContain(asistencias);
      expect(comp.asistenciasSharedCollection).toContain(asistencia);
      expect(comp.facturasSharedCollection).toContain(facturas);
      expect(comp.facturasSharedCollection).toContain(factura);
      expect(comp.cursosSharedCollection).toContain(cursos);
      expect(comp.estudiante).toEqual(estudiante);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEstudiante>>();
      const estudiante = { id: 123 };
      jest.spyOn(estudianteFormService, 'getEstudiante').mockReturnValue(estudiante);
      jest.spyOn(estudianteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ estudiante });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: estudiante }));
      saveSubject.complete();

      // THEN
      expect(estudianteFormService.getEstudiante).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(estudianteService.update).toHaveBeenCalledWith(expect.objectContaining(estudiante));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEstudiante>>();
      const estudiante = { id: 123 };
      jest.spyOn(estudianteFormService, 'getEstudiante').mockReturnValue({ id: null });
      jest.spyOn(estudianteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ estudiante: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: estudiante }));
      saveSubject.complete();

      // THEN
      expect(estudianteFormService.getEstudiante).toHaveBeenCalled();
      expect(estudianteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEstudiante>>();
      const estudiante = { id: 123 };
      jest.spyOn(estudianteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ estudiante });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(estudianteService.update).toHaveBeenCalled();
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

    describe('compareFactura', () => {
      it('Should forward to facturaService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(facturaService, 'compareFactura');
        comp.compareFactura(entity, entity2);
        expect(facturaService.compareFactura).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
