import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FacturaFormService } from './factura-form.service';
import { FacturaService } from '../service/factura.service';
import { IFactura } from '../factura.model';
import { IPago } from 'app/entities/pago/pago.model';
import { PagoService } from 'app/entities/pago/service/pago.service';

import { FacturaUpdateComponent } from './factura-update.component';

describe('Factura Management Update Component', () => {
  let comp: FacturaUpdateComponent;
  let fixture: ComponentFixture<FacturaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let facturaFormService: FacturaFormService;
  let facturaService: FacturaService;
  let pagoService: PagoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FacturaUpdateComponent],
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
      .overrideTemplate(FacturaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FacturaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    facturaFormService = TestBed.inject(FacturaFormService);
    facturaService = TestBed.inject(FacturaService);
    pagoService = TestBed.inject(PagoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Pago query and add missing value', () => {
      const factura: IFactura = { id: 456 };
      const pagos: IPago = { id: 26429 };
      factura.pagos = pagos;
      const pago: IPago = { id: 10982 };
      factura.pago = pago;

      const pagoCollection: IPago[] = [{ id: 14157 }];
      jest.spyOn(pagoService, 'query').mockReturnValue(of(new HttpResponse({ body: pagoCollection })));
      const additionalPagos = [pagos, pago];
      const expectedCollection: IPago[] = [...additionalPagos, ...pagoCollection];
      jest.spyOn(pagoService, 'addPagoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ factura });
      comp.ngOnInit();

      expect(pagoService.query).toHaveBeenCalled();
      expect(pagoService.addPagoToCollectionIfMissing).toHaveBeenCalledWith(
        pagoCollection,
        ...additionalPagos.map(expect.objectContaining)
      );
      expect(comp.pagosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const factura: IFactura = { id: 456 };
      const pagos: IPago = { id: 53182 };
      factura.pagos = pagos;
      const pago: IPago = { id: 73879 };
      factura.pago = pago;

      activatedRoute.data = of({ factura });
      comp.ngOnInit();

      expect(comp.pagosSharedCollection).toContain(pagos);
      expect(comp.pagosSharedCollection).toContain(pago);
      expect(comp.factura).toEqual(factura);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFactura>>();
      const factura = { id: 123 };
      jest.spyOn(facturaFormService, 'getFactura').mockReturnValue(factura);
      jest.spyOn(facturaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ factura });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: factura }));
      saveSubject.complete();

      // THEN
      expect(facturaFormService.getFactura).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(facturaService.update).toHaveBeenCalledWith(expect.objectContaining(factura));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFactura>>();
      const factura = { id: 123 };
      jest.spyOn(facturaFormService, 'getFactura').mockReturnValue({ id: null });
      jest.spyOn(facturaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ factura: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: factura }));
      saveSubject.complete();

      // THEN
      expect(facturaFormService.getFactura).toHaveBeenCalled();
      expect(facturaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFactura>>();
      const factura = { id: 123 };
      jest.spyOn(facturaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ factura });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(facturaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePago', () => {
      it('Should forward to pagoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(pagoService, 'comparePago');
        comp.comparePago(entity, entity2);
        expect(pagoService.comparePago).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
