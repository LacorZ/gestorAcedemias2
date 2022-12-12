import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITutor } from '../tutor.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../tutor.test-samples';

import { TutorService } from './tutor.service';

const requireRestSample: ITutor = {
  ...sampleWithRequiredData,
};

describe('Tutor Service', () => {
  let service: TutorService;
  let httpMock: HttpTestingController;
  let expectedResult: ITutor | ITutor[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TutorService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Tutor', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const tutor = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(tutor).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Tutor', () => {
      const tutor = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(tutor).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Tutor', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Tutor', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Tutor', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTutorToCollectionIfMissing', () => {
      it('should add a Tutor to an empty array', () => {
        const tutor: ITutor = sampleWithRequiredData;
        expectedResult = service.addTutorToCollectionIfMissing([], tutor);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tutor);
      });

      it('should not add a Tutor to an array that contains it', () => {
        const tutor: ITutor = sampleWithRequiredData;
        const tutorCollection: ITutor[] = [
          {
            ...tutor,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTutorToCollectionIfMissing(tutorCollection, tutor);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Tutor to an array that doesn't contain it", () => {
        const tutor: ITutor = sampleWithRequiredData;
        const tutorCollection: ITutor[] = [sampleWithPartialData];
        expectedResult = service.addTutorToCollectionIfMissing(tutorCollection, tutor);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tutor);
      });

      it('should add only unique Tutor to an array', () => {
        const tutorArray: ITutor[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const tutorCollection: ITutor[] = [sampleWithRequiredData];
        expectedResult = service.addTutorToCollectionIfMissing(tutorCollection, ...tutorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tutor: ITutor = sampleWithRequiredData;
        const tutor2: ITutor = sampleWithPartialData;
        expectedResult = service.addTutorToCollectionIfMissing([], tutor, tutor2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tutor);
        expect(expectedResult).toContain(tutor2);
      });

      it('should accept null and undefined values', () => {
        const tutor: ITutor = sampleWithRequiredData;
        expectedResult = service.addTutorToCollectionIfMissing([], null, tutor, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tutor);
      });

      it('should return initial array if no Tutor is added', () => {
        const tutorCollection: ITutor[] = [sampleWithRequiredData];
        expectedResult = service.addTutorToCollectionIfMissing(tutorCollection, undefined, null);
        expect(expectedResult).toEqual(tutorCollection);
      });
    });

    describe('compareTutor', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTutor(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTutor(entity1, entity2);
        const compareResult2 = service.compareTutor(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTutor(entity1, entity2);
        const compareResult2 = service.compareTutor(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTutor(entity1, entity2);
        const compareResult2 = service.compareTutor(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
