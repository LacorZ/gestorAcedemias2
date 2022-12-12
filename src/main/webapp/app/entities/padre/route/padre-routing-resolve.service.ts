import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPadre } from '../padre.model';
import { PadreService } from '../service/padre.service';

@Injectable({ providedIn: 'root' })
export class PadreRoutingResolveService implements Resolve<IPadre | null> {
  constructor(protected service: PadreService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPadre | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((padre: HttpResponse<IPadre>) => {
          if (padre.body) {
            return of(padre.body);
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
