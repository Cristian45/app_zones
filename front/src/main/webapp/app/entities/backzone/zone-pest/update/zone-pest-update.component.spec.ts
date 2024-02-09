import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ZonePestFormService } from './zone-pest-form.service';
import { ZonePestService } from '../service/zone-pest.service';
import { IZonePest } from '../zone-pest.model';
import { IZone } from 'app/entities/backzone/zone/zone.model';
import { ZoneService } from 'app/entities/backzone/zone/service/zone.service';
import { IPest } from 'app/entities/backzone/pest/pest.model';
import { PestService } from 'app/entities/backzone/pest/service/pest.service';

import { ZonePestUpdateComponent } from './zone-pest-update.component';

describe('ZonePest Management Update Component', () => {
  let comp: ZonePestUpdateComponent;
  let fixture: ComponentFixture<ZonePestUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let zonePestFormService: ZonePestFormService;
  let zonePestService: ZonePestService;
  let zoneService: ZoneService;
  let pestService: PestService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ZonePestUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ZonePestUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ZonePestUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    zonePestFormService = TestBed.inject(ZonePestFormService);
    zonePestService = TestBed.inject(ZonePestService);
    zoneService = TestBed.inject(ZoneService);
    pestService = TestBed.inject(PestService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Zone query and add missing value', () => {
      const zonePest: IZonePest = { id: 456 };
      const zoneId: IZone = { id: 93887 };
      zonePest.zoneId = zoneId;

      const zoneCollection: IZone[] = [{ id: 4350 }];
      jest.spyOn(zoneService, 'query').mockReturnValue(of(new HttpResponse({ body: zoneCollection })));
      const additionalZones = [zoneId];
      const expectedCollection: IZone[] = [...additionalZones, ...zoneCollection];
      jest.spyOn(zoneService, 'addZoneToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ zonePest });
      comp.ngOnInit();

      expect(zoneService.query).toHaveBeenCalled();
      expect(zoneService.addZoneToCollectionIfMissing).toHaveBeenCalledWith(
        zoneCollection,
        ...additionalZones.map(expect.objectContaining)
      );
      expect(comp.zonesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Pest query and add missing value', () => {
      const zonePest: IZonePest = { id: 456 };
      const pestId: IPest = { id: 61553 };
      zonePest.pestId = pestId;

      const pestCollection: IPest[] = [{ id: 14253 }];
      jest.spyOn(pestService, 'query').mockReturnValue(of(new HttpResponse({ body: pestCollection })));
      const additionalPests = [pestId];
      const expectedCollection: IPest[] = [...additionalPests, ...pestCollection];
      jest.spyOn(pestService, 'addPestToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ zonePest });
      comp.ngOnInit();

      expect(pestService.query).toHaveBeenCalled();
      expect(pestService.addPestToCollectionIfMissing).toHaveBeenCalledWith(
        pestCollection,
        ...additionalPests.map(expect.objectContaining)
      );
      expect(comp.pestsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const zonePest: IZonePest = { id: 456 };
      const zoneId: IZone = { id: 73661 };
      zonePest.zoneId = zoneId;
      const pestId: IPest = { id: 72101 };
      zonePest.pestId = pestId;

      activatedRoute.data = of({ zonePest });
      comp.ngOnInit();

      expect(comp.zonesSharedCollection).toContain(zoneId);
      expect(comp.pestsSharedCollection).toContain(pestId);
      expect(comp.zonePest).toEqual(zonePest);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IZonePest>>();
      const zonePest = { id: 123 };
      jest.spyOn(zonePestFormService, 'getZonePest').mockReturnValue(zonePest);
      jest.spyOn(zonePestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ zonePest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: zonePest }));
      saveSubject.complete();

      // THEN
      expect(zonePestFormService.getZonePest).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(zonePestService.update).toHaveBeenCalledWith(expect.objectContaining(zonePest));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IZonePest>>();
      const zonePest = { id: 123 };
      jest.spyOn(zonePestFormService, 'getZonePest').mockReturnValue({ id: null });
      jest.spyOn(zonePestService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ zonePest: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: zonePest }));
      saveSubject.complete();

      // THEN
      expect(zonePestFormService.getZonePest).toHaveBeenCalled();
      expect(zonePestService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IZonePest>>();
      const zonePest = { id: 123 };
      jest.spyOn(zonePestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ zonePest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(zonePestService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareZone', () => {
      it('Should forward to zoneService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(zoneService, 'compareZone');
        comp.compareZone(entity, entity2);
        expect(zoneService.compareZone).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('comparePest', () => {
      it('Should forward to pestService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(pestService, 'comparePest');
        comp.comparePest(entity, entity2);
        expect(pestService.comparePest).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
