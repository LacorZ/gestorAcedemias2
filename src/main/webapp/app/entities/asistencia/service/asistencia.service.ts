import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAsistencia, NewAsistencia } from '../asistencia.model';

export type PartialUpdateAsistencia = Partial<IAsistencia> & Pick<IAsistencia, 'id'>;

type RestOf<T extends IAsistencia | NewAsistencia> = Omit<T, 'fecha' | 'horaEntrada' | 'horaSalida'> & {
  fecha?: string | null;
  horaEntrada?: string | null;
  horaSalida?: string | null;
};

export type RestAsistencia = RestOf<IAsistencia>;

export type NewRestAsistencia = RestOf<NewAsistencia>;

export type PartialUpdateRestAsistencia = RestOf<PartialUpdateAsistencia>;

export type EntityResponseType = HttpResponse<IAsistencia>;
export type EntityArrayResponseType = HttpResponse<IAsistencia[]>;

@Injectable({ providedIn: 'root' })
export class AsistenciaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/asistencias');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(asistencia: NewAsistencia): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(asistencia);
    return this.http
      .post<RestAsistencia>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(asistencia: IAsistencia): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(asistencia);
    return this.http
      .put<RestAsistencia>(`${this.resourceUrl}/${this.getAsistenciaIdentifier(asistencia)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(asistencia: PartialUpdateAsistencia): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(asistencia);
    return this.http
      .patch<RestAsistencia>(`${this.resourceUrl}/${this.getAsistenciaIdentifier(asistencia)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAsistencia>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAsistencia[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAsistenciaIdentifier(asistencia: Pick<IAsistencia, 'id'>): number {
    return asistencia.id;
  }

  compareAsistencia(o1: Pick<IAsistencia, 'id'> | null, o2: Pick<IAsistencia, 'id'> | null): boolean {
    return o1 && o2 ? this.getAsistenciaIdentifier(o1) === this.getAsistenciaIdentifier(o2) : o1 === o2;
  }

  addAsistenciaToCollectionIfMissing<Type extends Pick<IAsistencia, 'id'>>(
    asistenciaCollection: Type[],
    ...asistenciasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const asistencias: Type[] = asistenciasToCheck.filter(isPresent);
    if (asistencias.length > 0) {
      const asistenciaCollectionIdentifiers = asistenciaCollection.map(asistenciaItem => this.getAsistenciaIdentifier(asistenciaItem)!);
      const asistenciasToAdd = asistencias.filter(asistenciaItem => {
        const asistenciaIdentifier = this.getAsistenciaIdentifier(asistenciaItem);
        if (asistenciaCollectionIdentifiers.includes(asistenciaIdentifier)) {
          return false;
        }
        asistenciaCollectionIdentifiers.push(asistenciaIdentifier);
        return true;
      });
      return [...asistenciasToAdd, ...asistenciaCollection];
    }
    return asistenciaCollection;
  }

  protected convertDateFromClient<T extends IAsistencia | NewAsistencia | PartialUpdateAsistencia>(asistencia: T): RestOf<T> {
    return {
      ...asistencia,
      fecha: asistencia.fecha?.format(DATE_FORMAT) ?? null,
      horaEntrada: asistencia.horaEntrada?.toJSON() ?? null,
      horaSalida: asistencia.horaSalida?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAsistencia: RestAsistencia): IAsistencia {
    return {
      ...restAsistencia,
      fecha: restAsistencia.fecha ? dayjs(restAsistencia.fecha) : undefined,
      horaEntrada: restAsistencia.horaEntrada ? dayjs(restAsistencia.horaEntrada) : undefined,
      horaSalida: restAsistencia.horaSalida ? dayjs(restAsistencia.horaSalida) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAsistencia>): HttpResponse<IAsistencia> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAsistencia[]>): HttpResponse<IAsistencia[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
