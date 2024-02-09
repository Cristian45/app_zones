import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IZonePest } from '../zone-pest.model';
import { ZonePestService } from '../service/zone-pest.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './zone-pest-delete-dialog.component.html',
})
export class ZonePestDeleteDialogComponent {
  zonePest?: IZonePest;

  constructor(protected zonePestService: ZonePestService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.zonePestService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
