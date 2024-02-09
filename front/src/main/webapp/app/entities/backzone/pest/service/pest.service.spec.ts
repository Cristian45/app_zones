import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPest } from '../pest.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../pest.test-samples';

import { PestService, RestPest } from './pest.service';

const requireRestSample: RestPest = {
  ...sampleWithRequiredData,
  createdat: sampleWithRequiredData.createdat?.toJSON(),
  updatedat: sampleWithRequiredData.updatedat?.toJSON(),
};

describe('Pest Service', () => {
  let service: PestService;
  let httpMock: HttpTestingController;
  let expectedResult: IPest | IPest[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PestService);
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

    it('should create a Pest', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const pest = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(pest).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Pest', () => {
      const pest = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(pest).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Pest', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Pest', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Pest', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPestToCollectionIfMissing', () => {
      it('should add a Pest to an empty array', () => {
        const pest: IPest = sampleWithRequiredData;
        expectedResult = service.addPestToCollectionIfMissing([], pest);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pest);
      });

      it('should not add a Pest to an array that contains it', () => {
        const pest: IPest = sampleWithRequiredData;
        const pestCollection: IPest[] = [
          {
            ...pest,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPestToCollectionIfMissing(pestCollection, pest);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Pest to an array that doesn't contain it", () => {
        const pest: IPest = sampleWithRequiredData;
        const pestCollection: IPest[] = [sampleWithPartialData];
        expectedResult = service.addPestToCollectionIfMissing(pestCollection, pest);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pest);
      });

      it('should add only unique Pest to an array', () => {
        const pestArray: IPest[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const pestCollection: IPest[] = [sampleWithRequiredData];
        expectedResult = service.addPestToCollectionIfMissing(pestCollection, ...pestArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pest: IPest = sampleWithRequiredData;
        const pest2: IPest = sampleWithPartialData;
        expectedResult = service.addPestToCollectionIfMissing([], pest, pest2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pest);
        expect(expectedResult).toContain(pest2);
      });

      it('should accept null and undefined values', () => {
        const pest: IPest = sampleWithRequiredData;
        expectedResult = service.addPestToCollectionIfMissing([], null, pest, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pest);
      });

      it('should return initial array if no Pest is added', () => {
        const pestCollection: IPest[] = [sampleWithRequiredData];
        expectedResult = service.addPestToCollectionIfMissing(pestCollection, undefined, null);
        expect(expectedResult).toEqual(pestCollection);
      });
    });

    describe('comparePest', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePest(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePest(entity1, entity2);
        const compareResult2 = service.comparePest(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePest(entity1, entity2);
        const compareResult2 = service.comparePest(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePest(entity1, entity2);
        const compareResult2 = service.comparePest(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
