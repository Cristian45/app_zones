import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../pest.test-samples';

import { PestFormService } from './pest-form.service';

describe('Pest Form Service', () => {
  let service: PestFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PestFormService);
  });

  describe('Service methods', () => {
    describe('createPestFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPestFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            createdat: expect.any(Object),
            updatedat: expect.any(Object),
          })
        );
      });

      it('passing IPest should create a new form with FormGroup', () => {
        const formGroup = service.createPestFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            createdat: expect.any(Object),
            updatedat: expect.any(Object),
          })
        );
      });
    });

    describe('getPest', () => {
      it('should return NewPest for default Pest initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPestFormGroup(sampleWithNewData);

        const pest = service.getPest(formGroup) as any;

        expect(pest).toMatchObject(sampleWithNewData);
      });

      it('should return NewPest for empty Pest initial value', () => {
        const formGroup = service.createPestFormGroup();

        const pest = service.getPest(formGroup) as any;

        expect(pest).toMatchObject({});
      });

      it('should return IPest', () => {
        const formGroup = service.createPestFormGroup(sampleWithRequiredData);

        const pest = service.getPest(formGroup) as any;

        expect(pest).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPest should not enable id FormControl', () => {
        const formGroup = service.createPestFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPest should disable id FormControl', () => {
        const formGroup = service.createPestFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
