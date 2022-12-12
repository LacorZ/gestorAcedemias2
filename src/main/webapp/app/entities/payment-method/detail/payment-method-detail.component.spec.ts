import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PaymentMethodDetailComponent } from './payment-method-detail.component';

describe('PaymentMethod Management Detail Component', () => {
  let comp: PaymentMethodDetailComponent;
  let fixture: ComponentFixture<PaymentMethodDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PaymentMethodDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ paymentMethod: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PaymentMethodDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PaymentMethodDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load paymentMethod on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.paymentMethod).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
