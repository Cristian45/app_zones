import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PestDetailComponent } from './pest-detail.component';

describe('Pest Management Detail Component', () => {
  let comp: PestDetailComponent;
  let fixture: ComponentFixture<PestDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PestDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ pest: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PestDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PestDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load pest on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.pest).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
