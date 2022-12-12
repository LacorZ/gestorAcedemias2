import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TutorComponent } from '../list/tutor.component';
import { TutorDetailComponent } from '../detail/tutor-detail.component';
import { TutorUpdateComponent } from '../update/tutor-update.component';
import { TutorRoutingResolveService } from './tutor-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const tutorRoute: Routes = [
  {
    path: '',
    component: TutorComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TutorDetailComponent,
    resolve: {
      tutor: TutorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TutorUpdateComponent,
    resolve: {
      tutor: TutorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TutorUpdateComponent,
    resolve: {
      tutor: TutorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tutorRoute)],
  exports: [RouterModule],
})
export class TutorRoutingModule {}
