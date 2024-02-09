import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PestComponent } from './list/pest.component';
import { PestDetailComponent } from './detail/pest-detail.component';
import { PestUpdateComponent } from './update/pest-update.component';
import { PestDeleteDialogComponent } from './delete/pest-delete-dialog.component';
import { PestRoutingModule } from './route/pest-routing.module';

@NgModule({
  imports: [SharedModule, PestRoutingModule],
  declarations: [PestComponent, PestDetailComponent, PestUpdateComponent, PestDeleteDialogComponent],
})
export class BackzonePestModule {}
