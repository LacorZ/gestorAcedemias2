import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPaymentMethod } from '../payment-method.model';

@Component({
  selector: 'jhi-payment-method-detail',
  templateUrl: './payment-method-detail.component.html',
})
export class PaymentMethodDetailComponent implements OnInit {
  paymentMethod: IPaymentMethod | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paymentMethod }) => {
      this.paymentMethod = paymentMethod;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
