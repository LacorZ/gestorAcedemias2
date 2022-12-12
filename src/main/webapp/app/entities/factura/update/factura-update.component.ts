import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { FacturaFormService, FacturaFormGroup } from './factura-form.service';
import { IFactura } from '../factura.model';
import { FacturaService } from '../service/factura.service';
import { IPago } from 'app/entities/pago/pago.model';
import { PagoService } from 'app/entities/pago/service/pago.service';

@Component({
  selector: 'jhi-factura-update',
  templateUrl: './factura-update.component.html',
})
export class FacturaUpdateComponent implements OnInit {
  isSaving = false;
  factura: IFactura | null = null;

  pagosSharedCollection: IPago[] = [];

  editForm: FacturaFormGroup = this.facturaFormService.createFacturaFormGroup();

  constructor(
    protected facturaService: FacturaService,
    protected facturaFormService: FacturaFormService,
    protected pagoService: PagoService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePago = (o1: IPago | null, o2: IPago | null): boolean => this.pagoService.comparePago(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ factura }) => {
      this.factura = factura;
      if (factura) {
        this.updateForm(factura);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const factura = this.facturaFormService.getFactura(this.editForm);
    if (factura.id !== null) {
      this.subscribeToSaveResponse(this.facturaService.update(factura));
    } else {
      this.subscribeToSaveResponse(this.facturaService.create(factura));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFactura>>): void {
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

  protected updateForm(factura: IFactura): void {
    this.factura = factura;
    this.facturaFormService.resetForm(this.editForm, factura);

    this.pagosSharedCollection = this.pagoService.addPagoToCollectionIfMissing<IPago>(
      this.pagosSharedCollection,
      factura.pagos,
      factura.pago
    );
  }

  protected loadRelationshipsOptions(): void {
    this.pagoService
      .query()
      .pipe(map((res: HttpResponse<IPago[]>) => res.body ?? []))
      .pipe(map((pagos: IPago[]) => this.pagoService.addPagoToCollectionIfMissing<IPago>(pagos, this.factura?.pagos, this.factura?.pago)))
      .subscribe((pagos: IPago[]) => (this.pagosSharedCollection = pagos));
  }
}
