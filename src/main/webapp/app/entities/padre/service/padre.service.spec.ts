import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPadre } from '../padre.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../padre.test-samples';

import { PadreService } from './padre.service';

const requireRestSample: IPadre = {
  ...sampleWithRequiredData,
};

describe('Padre Service', () => {
  let service: PadreService;
  let httpMock: HttpTestingController;
  let expectedResult: IPadre | IPadre[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PadreService);
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

    it('should create a Padre', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const padre = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(padre).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Padre', () => {
      const padre = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(padre).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Padre', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Padre', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Padre', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPadreToCollectionIfMissing', () => {
      it('should add a Padre to an empty array', () => {
        const padre: IPadre = sampleWithRequiredData;
        expectedResult = service.addPadreToCollectionIfMissing([], padre);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(padre);
      });

      it('should not add a Padre to an array that contains it', () => {
        const padre: IPadre = sampleWithRequiredData;
        const padreCollection: IPadre[] = [
          {
            ...padre,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPadreToCollectionIfMissing(padreCollection, padre);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Padre to an array that doesn't contain it", () => {
        const padre: IPadre = sampleWithRequiredData;
        const padreCollection: IPadre[] = [sampleWithPartialData];
        expectedResult = service.addPadreToCollectionIfMissing(padreCollection, padre);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(padre);
      });

      it('should add only unique Padre to an array', () => {
        const padreArray: IPadre[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const padreCollection: IPadre[] = [sampleWithRequiredData];
        expectedResult = service.addPadreToCollectionIfMissing(padreCollection, ...padreArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const padre: IPadre = sampleWithRequiredData;
        const padre2: IPadre = sampleWithPartialData;
        expectedResult = service.addPadreToCollectionIfMissing([], padre, padre2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(padre);
        expect(expectedResult).toContain(padre2);
      });

      it('should accept null and undefined values', () => {
        const padre: IPadre = sampleWithRequiredData;
        expectedResult = service.addPadreToCollectionIfMissing([], null, padre, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(padre);
      });

      it('should return initial array if no Padre is added', () => {
        const padreCollection: IPadre[] = [sampleWithRequiredData];
        expectedResult = service.addPadreToCollectionIfMissing(padreCollection, undefined, null);
        expect(expectedResult).toEqual(padreCollection);
      });
    });

    describe('comparePadre', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePadre(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePadre(entity1, entity2);
        const compareResult2 = service.comparePadre(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePadre(entity1, entity2);
        const compareResult2 = service.comparePadre(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePadre(entity1, entity2);
        const compareResult2 = service.comparePadre(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
