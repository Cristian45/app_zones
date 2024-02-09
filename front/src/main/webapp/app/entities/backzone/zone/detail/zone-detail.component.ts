import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IZone } from '../zone.model';
import { ZoneService } from 'app/entities/backzone/zone/service/zone.service';

@Component({
  selector: 'jhi-zone-detail',
  templateUrl: './zone-detail.component.html',
})
export class ZoneDetailComponent implements OnInit {
  zone: IZone | null = null;

  pests?: String[];

  quantity: number = 0;

  constructor(protected activatedRoute: ActivatedRoute, protected zoneService: ZoneService, ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ zone }) => {
      this.zone = zone;
    });


    this.zoneService.findDataPest(this.zone?.id||0).subscribe(
      response => {      
        console.log('antes');  
        this.pests = response.body?.pets || [];
        console.log(this.pests);
        
        this.quantity = this.pests.length;
        console.log('despues '+this.quantity);
      }
    );
  }

  previousState(): void {
    window.history.back();
  }
}
