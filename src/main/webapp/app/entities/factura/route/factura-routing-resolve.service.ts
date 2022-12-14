import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFactura } from '../factura.model';
import { FacturaService } from '../service/factura.service';

@Injectable({ providedIn: 'root' })
export class FacturaRoutingResolveService implements Resolve<IFactura | null> {
  constructor(protected service: FacturaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFactura | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((factura: HttpResponse<IFactura>) => {
          if (factura.body) {
            return of(factura.body);
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
