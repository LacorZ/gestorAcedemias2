import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TutorDetailComponent } from './tutor-detail.component';

describe('Tutor Management Detail Component', () => {
  let comp: TutorDetailComponent;
  let fixture: ComponentFixture<TutorDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TutorDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tutor: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TutorDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TutorDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tutor on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tutor).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
