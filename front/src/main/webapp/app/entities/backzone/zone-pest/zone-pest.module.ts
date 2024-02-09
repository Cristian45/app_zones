import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ZonePestComponent } from './list/zone-pest.component';
import { ZonePestDetailComponent } from './detail/zone-pest-detail.component';
import { ZonePestUpdateComponent } from './update/zone-pest-update.component';
import { ZonePestDeleteDialogComponent } from './delete/zone-pest-delete-dialog.component';
import { ZonePestRoutingModule } from './route/zone-pest-routing.module';

@NgModule({
  imports: [SharedModule, ZonePestRoutingModule],
  declarations: [ZonePestComponent, ZonePestDetailComponent, ZonePestUpdateComponent, ZonePestDeleteDialogComponent],
})
export class BackzoneZonePestModule {}
