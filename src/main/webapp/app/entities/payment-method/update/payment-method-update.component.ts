import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PaymentMethodFormService, PaymentMethodFormGroup } from './payment-method-form.service';
import { IPaymentMethod } from '../payment-method.model';
import { PaymentMethodService } from '../service/payment-method.service';
import { IPayment } from 'app/entities/payment/payment.model';
import { PaymentService } from 'app/entities/payment/service/payment.service';

@Component({
  selector: 'jhi-payment-method-update',
  templateUrl: './payment-method-update.component.html',
})
export class PaymentMethodUpdateComponent implements OnInit {
  isSaving = false;
  paymentMethod: IPaymentMethod | null = null;

  paymentsSharedCollection: IPayment[] = [];

  editForm: PaymentMethodFormGroup = this.paymentMethodFormService.createPaymentMethodFormGroup();

  constructor(
    protected paymentMethodService: PaymentMethodService,
    protected paymentMethodFormService: PaymentMethodFormService,
    protected paymentService: PaymentService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePayment = (o1: IPayment | null, o2: IPayment | null): boolean => this.paymentService.comparePayment(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paymentMethod }) => {
      this.paymentMethod = paymentMethod;
      if (paymentMethod) {
        this.updateForm(paymentMethod);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const paymentMethod = this.paymentMethodFormService.getPaymentMethod(this.editForm);
    if (paymentMethod.id !== null) {
      this.subscribeToSaveResponse(this.paymentMethodService.update(paymentMethod));
    } else {
      this.subscribeToSaveResponse(this.paymentMethodService.create(paymentMethod));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaymentMethod>>): void {
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

  protected updateForm(paymentMethod: IPaymentMethod): void {
    this.paymentMethod = paymentMethod;
    this.paymentMethodFormService.resetForm(this.editForm, paymentMethod);

    this.paymentsSharedCollection = this.paymentService.addPaymentToCollectionIfMissing<IPayment>(
      this.paymentsSharedCollection,
      paymentMethod.payment
    );
  }

  protected loadRelationshipsOptions(): void {
    this.paymentService
      .query()
      .pipe(map((res: HttpResponse<IPayment[]>) => res.body ?? []))
      .pipe(
        map((payments: IPayment[]) => this.paymentService.addPaymentToCollectionIfMissing<IPayment>(payments, this.paymentMethod?.payment))
      )
      .subscribe((payments: IPayment[]) => (this.paymentsSharedCollection = payments));
  }
}
