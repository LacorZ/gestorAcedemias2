import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IAsistencia } from '../asistencia.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../asistencia.test-samples';

import { AsistenciaService, RestAsistencia } from './asistencia.service';

const requireRestSample: RestAsistencia = {
  ...sampleWithRequiredData,
  fecha: sampleWithRequiredData.fecha?.format(DATE_FORMAT),
  horaEntrada: sampleWithRequiredData.horaEntrada?.toJSON(),
  horaSalida: sampleWithRequiredData.horaSalida?.toJSON(),
};

describe('Asistencia Service', () => {
  let service: AsistenciaService;
  let httpMock: HttpTestingController;
  let expectedResult: IAsistencia | IAsistencia[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AsistenciaService);
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

    it('should create a Asistencia', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const asistencia = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(asistencia).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Asistencia', () => {
      const asistencia = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(asistencia).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Asistencia', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Asistencia', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Asistencia', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAsistenciaToCollectionIfMissing', () => {
      it('should add a Asistencia to an empty array', () => {
        const asistencia: IAsistencia = sampleWithRequiredData;
        expectedResult = service.addAsistenciaToCollectionIfMissing([], asistencia);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(asistencia);
      });

      it('should not add a Asistencia to an array that contains it', () => {
        const asistencia: IAsistencia = sampleWithRequiredData;
        const asistenciaCollection: IAsistencia[] = [
          {
            ...asistencia,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAsistenciaToCollectionIfMissing(asistenciaCollection, asistencia);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Asistencia to an array that doesn't contain it", () => {
        const asistencia: IAsistencia = sampleWithRequiredData;
        const asistenciaCollection: IAsistencia[] = [sampleWithPartialData];
        expectedResult = service.addAsistenciaToCollectionIfMissing(asistenciaCollection, asistencia);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(asistencia);
      });

      it('should add only unique Asistencia to an array', () => {
        const asistenciaArray: IAsistencia[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const asistenciaCollection: IAsistencia[] = [sampleWithRequiredData];
        expectedResult = service.addAsistenciaToCollectionIfMissing(asistenciaCollection, ...asistenciaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const asistencia: IAsistencia = sampleWithRequiredData;
        const asistencia2: IAsistencia = sampleWithPartialData;
        expectedResult = service.addAsistenciaToCollectionIfMissing([], asistencia, asistencia2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(asistencia);
        expect(expectedResult).toContain(asistencia2);
      });

      it('should accept null and undefined values', () => {
        const asistencia: IAsistencia = sampleWithRequiredData;
        expectedResult = service.addAsistenciaToCollectionIfMissing([], null, asistencia, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(asistencia);
      });

      it('should return initial array if no Asistencia is added', () => {
        const asistenciaCollection: IAsistencia[] = [sampleWithRequiredData];
        expectedResult = service.addAsistenciaToCollectionIfMissing(asistenciaCollection, undefined, null);
        expect(expectedResult).toEqual(asistenciaCollection);
      });
    });

    describe('compareAsistencia', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAsistencia(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAsistencia(entity1, entity2);
        const compareResult2 = service.compareAsistencia(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAsistencia(entity1, entity2);
        const compareResult2 = service.compareAsistencia(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAsistencia(entity1, entity2);
        const compareResult2 = service.compareAsistencia(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
