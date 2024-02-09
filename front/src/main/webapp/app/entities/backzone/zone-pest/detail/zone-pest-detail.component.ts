import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IZonePest } from '../zone-pest.model';
import { ZoneService } from 'app/entities/backzone/zone/service/zone.service';
import { PestService } from 'app/entities/backzone/pest/service/pest.service';

@Component({
  selector: 'jhi-zone-pest-detail',
  templateUrl: './zone-pest-detail.component.html',
})
export class ZonePestDetailComponent implements OnInit {
  zonePest: IZonePest | null = null;

  zoneName: String | "" = '';
  pestName: String | "" = '';
  constructor(protected activatedRoute: ActivatedRoute,
        protected zoneService: ZoneService, 
        protected pestService: PestService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ zonePest }) => {
      this.zonePest = zonePest;
    });
    
    this.zoneService.find(this.zonePest?.zoneId?.id||0).subscribe(
      response => {
          this.zoneName = response.body?.name || '';
        console.log(response.body?.name);
      }
    );

    this.pestService.find(this.zonePest?.pestId?.id||0).subscribe(
      response => {
          this.pestName = response.body?.name || '';
        console.log(response.body?.name);
      }
    );
  }

  previousState(): void {
    window.history.back();
  }
}
