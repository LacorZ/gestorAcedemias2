import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITutor, NewTutor } from '../tutor.model';

export type PartialUpdateTutor = Partial<ITutor> & Pick<ITutor, 'id'>;

export type EntityResponseType = HttpResponse<ITutor>;
export type EntityArrayResponseType = HttpResponse<ITutor[]>;

@Injectable({ providedIn: 'root' })
export class TutorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tutors');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tutor: NewTutor): Observable<EntityResponseType> {
    return this.http.post<ITutor>(this.resourceUrl, tutor, { observe: 'response' });
  }

  update(tutor: ITutor): Observable<EntityResponseType> {
    return this.http.put<ITutor>(`${this.resourceUrl}/${this.getTutorIdentifier(tutor)}`, tutor, { observe: 'response' });
  }

  partialUpdate(tutor: PartialUpdateTutor): Observable<EntityResponseType> {
    return this.http.patch<ITutor>(`${this.resourceUrl}/${this.getTutorIdentifier(tutor)}`, tutor, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITutor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITutor[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTutorIdentifier(tutor: Pick<ITutor, 'id'>): number {
    return tutor.id;
  }

  compareTutor(o1: Pick<ITutor, 'id'> | null, o2: Pick<ITutor, 'id'> | null): boolean {
    return o1 && o2 ? this.getTutorIdentifier(o1) === this.getTutorIdentifier(o2) : o1 === o2;
  }

  addTutorToCollectionIfMissing<Type extends Pick<ITutor, 'id'>>(
    tutorCollection: Type[],
    ...tutorsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tutors: Type[] = tutorsToCheck.filter(isPresent);
    if (tutors.length > 0) {
      const tutorCollectionIdentifiers = tutorCollection.map(tutorItem => this.getTutorIdentifier(tutorItem)!);
      const tutorsToAdd = tutors.filter(tutorItem => {
        const tutorIdentifier = this.getTutorIdentifier(tutorItem);
        if (tutorCollectionIdentifiers.includes(tutorIdentifier)) {
          return false;
        }
        tutorCollectionIdentifiers.push(tutorIdentifier);
        return true;
      });
      return [...tutorsToAdd, ...tutorCollection];
    }
    return tutorCollection;
  }
}
