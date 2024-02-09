import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PestFormService } from './pest-form.service';
import { PestService } from '../service/pest.service';
import { IPest } from '../pest.model';

import { PestUpdateComponent } from './pest-update.component';

describe('Pest Management Update Component', () => {
  let comp: PestUpdateComponent;
  let fixture: ComponentFixture<PestUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pestFormService: PestFormService;
  let pestService: PestService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PestUpdateComponent],
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
      .overrideTemplate(PestUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PestUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pestFormService = TestBed.inject(PestFormService);
    pestService = TestBed.inject(PestService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const pest: IPest = { id: 456 };

      activatedRoute.data = of({ pest });
      comp.ngOnInit();

      expect(comp.pest).toEqual(pest);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPest>>();
      const pest = { id: 123 };
      jest.spyOn(pestFormService, 'getPest').mockReturnValue(pest);
      jest.spyOn(pestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pest }));
      saveSubject.complete();

      // THEN
      expect(pestFormService.getPest).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(pestService.update).toHaveBeenCalledWith(expect.objectContaining(pest));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPest>>();
      const pest = { id: 123 };
      jest.spyOn(pestFormService, 'getPest').mockReturnValue({ id: null });
      jest.spyOn(pestService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pest: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pest }));
      saveSubject.complete();

      // THEN
      expect(pestFormService.getPest).toHaveBeenCalled();
      expect(pestService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPest>>();
      const pest = { id: 123 };
      jest.spyOn(pestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pestService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
