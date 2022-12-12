import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { PagoFormService, PagoFormGroup } from './pago-form.service';
import { IPago } from '../pago.model';
import { PagoService } from '../service/pago.service';
import { MetodoPago } from 'app/entities/enumerations/metodo-pago.model';

@Component({
  selector: 'jhi-pago-update',
  templateUrl: './pago-update.component.html',
})
export class PagoUpdateComponent implements OnInit {
  isSaving = false;
  pago: IPago | null = null;
  metodoPagoValues = Object.keys(MetodoPago);

  editForm: PagoFormGroup = this.pagoFormService.createPagoFormGroup();

  constructor(protected pagoService: PagoService, protected pagoFormService: PagoFormService, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pago }) => {
      this.pago = pago;
      if (pago) {
        this.updateForm(pago);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pago = this.pagoFormService.getPago(this.editForm);
    if (pago.id !== null) {
      this.subscribeToSaveResponse(this.pagoService.update(pago));
    } else {
      this.subscribeToSaveResponse(this.pagoService.create(pago));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPago>>): void {
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

  protected updateForm(pago: IPago): void {
    this.pago = pago;
    this.pagoFormService.resetForm(this.editForm, pago);
  }
}
