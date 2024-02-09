import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ZonePestComponent } from '../list/zone-pest.component';
import { ZonePestDetailComponent } from '../detail/zone-pest-detail.component';
import { ZonePestUpdateComponent } from '../update/zone-pest-update.component';
import { ZonePestRoutingResolveService } from './zone-pest-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const zonePestRoute: Routes = [
  {
    path: '',
    component: ZonePestComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ZonePestDetailComponent,
    resolve: {
      zonePest: ZonePestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ZonePestUpdateComponent,
    resolve: {
      zonePest: ZonePestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ZonePestUpdateComponent,
    resolve: {
      zonePest: ZonePestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(zonePestRoute)],
  exports: [RouterModule],
})
export class ZonePestRoutingModule {}
