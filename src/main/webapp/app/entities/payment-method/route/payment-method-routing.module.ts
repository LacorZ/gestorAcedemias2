import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PaymentMethodComponent } from '../list/payment-method.component';
import { PaymentMethodDetailComponent } from '../detail/payment-method-detail.component';
import { PaymentMethodUpdateComponent } from '../update/payment-method-update.component';
import { PaymentMethodRoutingResolveService } from './payment-method-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const paymentMethodRoute: Routes = [
  {
    path: '',
    component: PaymentMethodComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PaymentMethodDetailComponent,
    resolve: {
      paymentMethod: PaymentMethodRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PaymentMethodUpdateComponent,
    resolve: {
      paymentMethod: PaymentMethodRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PaymentMethodUpdateComponent,
    resolve: {
      paymentMethod: PaymentMethodRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(paymentMethodRoute)],
  exports: [RouterModule],
})
export class PaymentMethodRoutingModule {}
