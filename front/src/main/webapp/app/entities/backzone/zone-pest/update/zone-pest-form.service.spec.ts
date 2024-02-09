import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../zone-pest.test-samples';

import { ZonePestFormService } from './zone-pest-form.service';

describe('ZonePest Form Service', () => {
  let service: ZonePestFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ZonePestFormService);
  });

  describe('Service methods', () => {
    describe('createZonePestFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createZonePestFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createdat: expect.any(Object),
            updatedat: expect.any(Object),
            zoneId: expect.any(Object),
            pestId: expect.any(Object),
          })
        );
      });

      it('passing IZonePest should create a new form with FormGroup', () => {
        const formGroup = service.createZonePestFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createdat: expect.any(Object),
            updatedat: expect.any(Object),
            zoneId: expect.any(Object),
            pestId: expect.any(Object),
          })
        );
      });
    });

    describe('getZonePest', () => {
      it('should return NewZonePest for default ZonePest initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createZonePestFormGroup(sampleWithNewData);

        const zonePest = service.getZonePest(formGroup) as any;

        expect(zonePest).toMatchObject(sampleWithNewData);
      });

      it('should return NewZonePest for empty ZonePest initial value', () => {
        const formGroup = service.createZonePestFormGroup();

        const zonePest = service.getZonePest(formGroup) as any;

        expect(zonePest).toMatchObject({});
      });

      it('should return IZonePest', () => {
        const formGroup = service.createZonePestFormGroup(sampleWithRequiredData);

        const zonePest = service.getZonePest(formGroup) as any;

        expect(zonePest).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IZonePest should not enable id FormControl', () => {
        const formGroup = service.createZonePestFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewZonePest should disable id FormControl', () => {
        const formGroup = service.createZonePestFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
