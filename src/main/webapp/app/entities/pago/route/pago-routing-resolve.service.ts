import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPago } from '../pago.model';
import { PagoService } from '../service/pago.service';

@Injectable({ providedIn: 'root' })
export class PagoRoutingResolveService implements Resolve<IPago | null> {
  constructor(protected service: PagoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPago | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pago: HttpResponse<IPago>) => {
          if (pago.body) {
            return of(pago.body);
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
