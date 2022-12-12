import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPaymentMethod } from '../payment-method.model';
import { PaymentMethodService } from '../service/payment-method.service';

@Injectable({ providedIn: 'root' })
export class PaymentMethodRoutingResolveService implements Resolve<IPaymentMethod | null> {
  constructor(protected service: PaymentMethodService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPaymentMethod | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((paymentMethod: HttpResponse<IPaymentMethod>) => {
          if (paymentMethod.body) {
            return of(paymentMethod.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
