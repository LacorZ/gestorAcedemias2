import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PadreDetailComponent } from './padre-detail.component';

describe('Padre Management Detail Component', () => {
  let comp: PadreDetailComponent;
  let fixture: ComponentFixture<PadreDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PadreDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ padre: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PadreDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PadreDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load padre on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.padre).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
