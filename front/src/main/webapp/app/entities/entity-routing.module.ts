import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'zone',
        data: { pageTitle: 'Zonas' },
        loadChildren: () => import('./backzone/zone/zone.module').then(m => m.BackzoneZoneModule),
      },
      {
        path: 'pest',
        data: { pageTitle: 'Plagas' },
        loadChildren: () => import('./backzone/pest/pest.module').then(m => m.BackzonePestModule),
      },
      {
        path: 'zone-pest',
        data: { pageTitle: 'Afectaciones' },
        loadChildren: () => import('./backzone/zone-pest/zone-pest.module').then(m => m.BackzoneZonePestModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
