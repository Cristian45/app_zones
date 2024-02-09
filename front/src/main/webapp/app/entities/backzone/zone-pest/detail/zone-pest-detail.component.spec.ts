import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ZonePestDetailComponent } from './zone-pest-detail.component';

describe('ZonePest Management Detail Component', () => {
  let comp: ZonePestDetailComponent;
  let fixture: ComponentFixture<ZonePestDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ZonePestDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ zonePest: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ZonePestDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ZonePestDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load zonePest on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.zonePest).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
