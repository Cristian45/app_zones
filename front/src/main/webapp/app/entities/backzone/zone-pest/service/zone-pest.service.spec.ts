import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IZonePest } from '../zone-pest.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../zone-pest.test-samples';

import { ZonePestService, RestZonePest } from './zone-pest.service';

const requireRestSample: RestZonePest = {
  ...sampleWithRequiredData,
  createdat: sampleWithRequiredData.createdat?.toJSON(),
  updatedat: sampleWithRequiredData.updatedat?.toJSON(),
};

describe('ZonePest Service', () => {
  let service: ZonePestService;
  let httpMock: HttpTestingController;
  let expectedResult: IZonePest | IZonePest[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ZonePestService);
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

    it('should create a ZonePest', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const zonePest = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(zonePest).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ZonePest', () => {
      const zonePest = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(zonePest).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ZonePest', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ZonePest', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ZonePest', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addZonePestToCollectionIfMissing', () => {
      it('should add a ZonePest to an empty array', () => {
        const zonePest: IZonePest = sampleWithRequiredData;
        expectedResult = service.addZonePestToCollectionIfMissing([], zonePest);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(zonePest);
      });

      it('should not add a ZonePest to an array that contains it', () => {
        const zonePest: IZonePest = sampleWithRequiredData;
        const zonePestCollection: IZonePest[] = [
          {
            ...zonePest,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addZonePestToCollectionIfMissing(zonePestCollection, zonePest);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ZonePest to an array that doesn't contain it", () => {
        const zonePest: IZonePest = sampleWithRequiredData;
        const zonePestCollection: IZonePest[] = [sampleWithPartialData];
        expectedResult = service.addZonePestToCollectionIfMissing(zonePestCollection, zonePest);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(zonePest);
      });

      it('should add only unique ZonePest to an array', () => {
        const zonePestArray: IZonePest[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const zonePestCollection: IZonePest[] = [sampleWithRequiredData];
        expectedResult = service.addZonePestToCollectionIfMissing(zonePestCollection, ...zonePestArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const zonePest: IZonePest = sampleWithRequiredData;
        const zonePest2: IZonePest = sampleWithPartialData;
        expectedResult = service.addZonePestToCollectionIfMissing([], zonePest, zonePest2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(zonePest);
        expect(expectedResult).toContain(zonePest2);
      });

      it('should accept null and undefined values', () => {
        const zonePest: IZonePest = sampleWithRequiredData;
        expectedResult = service.addZonePestToCollectionIfMissing([], null, zonePest, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(zonePest);
      });

      it('should return initial array if no ZonePest is added', () => {
        const zonePestCollection: IZonePest[] = [sampleWithRequiredData];
        expectedResult = service.addZonePestToCollectionIfMissing(zonePestCollection, undefined, null);
        expect(expectedResult).toEqual(zonePestCollection);
      });
    });

    describe('compareZonePest', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareZonePest(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareZonePest(entity1, entity2);
        const compareResult2 = service.compareZonePest(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareZonePest(entity1, entity2);
        const compareResult2 = service.compareZonePest(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareZonePest(entity1, entity2);
        const compareResult2 = service.compareZonePest(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
