import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPadre, NewPadre } from '../padre.model';

export type PartialUpdatePadre = Partial<IPadre> & Pick<IPadre, 'id'>;

export type EntityResponseType = HttpResponse<IPadre>;
export type EntityArrayResponseType = HttpResponse<IPadre[]>;

@Injectable({ providedIn: 'root' })
export class PadreService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/padres');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(padre: NewPadre): Observable<EntityResponseType> {
    return this.http.post<IPadre>(this.resourceUrl, padre, { observe: 'response' });
  }

  update(padre: IPadre): Observable<EntityResponseType> {
    return this.http.put<IPadre>(`${this.resourceUrl}/${this.getPadreIdentifier(padre)}`, padre, { observe: 'response' });
  }

  partialUpdate(padre: PartialUpdatePadre): Observable<EntityResponseType> {
    return this.http.patch<IPadre>(`${this.resourceUrl}/${this.getPadreIdentifier(padre)}`, padre, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPadre>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPadre[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPadreIdentifier(padre: Pick<IPadre, 'id'>): number {
    return padre.id;
  }

  comparePadre(o1: Pick<IPadre, 'id'> | null, o2: Pick<IPadre, 'id'> | null): boolean {
    return o1 && o2 ? this.getPadreIdentifier(o1) === this.getPadreIdentifier(o2) : o1 === o2;
  }

  addPadreToCollectionIfMissing<Type extends Pick<IPadre, 'id'>>(
    padreCollection: Type[],
    ...padresToCheck: (Type | null | undefined)[]
  ): Type[] {
    const padres: Type[] = padresToCheck.filter(isPresent);
    if (padres.length > 0) {
      const padreCollectionIdentifiers = padreCollection.map(padreItem => this.getPadreIdentifier(padreItem)!);
      const padresToAdd = padres.filter(padreItem => {
        const padreIdentifier = this.getPadreIdentifier(padreItem);
        if (padreCollectionIdentifiers.includes(padreIdentifier)) {
          return false;
        }
        padreCollectionIdentifiers.push(padreIdentifier);
        return true;
      });
      return [...padresToAdd, ...padreCollection];
    }
    return padreCollection;
  }
}
