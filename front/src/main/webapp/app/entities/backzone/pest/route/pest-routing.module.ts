import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PestComponent } from '../list/pest.component';
import { PestDetailComponent } from '../detail/pest-detail.component';
import { PestUpdateComponent } from '../update/pest-update.component';
import { PestRoutingResolveService } from './pest-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const pestRoute: Routes = [
  {
    path: '',
    component: PestComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PestDetailComponent,
    resolve: {
      pest: PestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PestUpdateComponent,
    resolve: {
      pest: PestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PestUpdateComponent,
    resolve: {
      pest: PestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pestRoute)],
  exports: [RouterModule],
})
export class PestRoutingModule {}
