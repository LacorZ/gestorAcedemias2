import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PadreComponent } from '../list/padre.component';
import { PadreDetailComponent } from '../detail/padre-detail.component';
import { PadreUpdateComponent } from '../update/padre-update.component';
import { PadreRoutingResolveService } from './padre-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const padreRoute: Routes = [
  {
    path: '',
    component: PadreComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PadreDetailComponent,
    resolve: {
      padre: PadreRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PadreUpdateComponent,
    resolve: {
      padre: PadreRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PadreUpdateComponent,
    resolve: {
      padre: PadreRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(padreRoute)],
  exports: [RouterModule],
})
export class PadreRoutingModule {}
