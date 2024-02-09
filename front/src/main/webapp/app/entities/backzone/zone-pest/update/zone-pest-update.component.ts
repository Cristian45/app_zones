import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ZonePestFormService, ZonePestFormGroup } from './zone-pest-form.service';
import { IZonePest } from '../zone-pest.model';
import { ZonePestService } from '../service/zone-pest.service';
import { IZone } from 'app/entities/backzone/zone/zone.model';
import { ZoneService } from 'app/entities/backzone/zone/service/zone.service';
import { IPest } from 'app/entities/backzone/pest/pest.model';
import { PestService } from 'app/entities/backzone/pest/service/pest.service';

@Component({
  selector: 'jhi-zone-pest-update',
  templateUrl: './zone-pest-update.component.html',
})
export class ZonePestUpdateComponent implements OnInit {
  isSaving = false;
  zonePest: IZonePest | null = null;

  zonesSharedCollection: IZone[] = [];
  pestsSharedCollection: IPest[] = [];

  editForm: ZonePestFormGroup = this.zonePestFormService.createZonePestFormGroup();

  constructor(
    protected zonePestService: ZonePestService,
    protected zonePestFormService: ZonePestFormService,
    protected zoneService: ZoneService,
    protected pestService: PestService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareZone = (o1: IZone | null, o2: IZone | null): boolean => this.zoneService.compareZone(o1, o2);

  comparePest = (o1: IPest | null, o2: IPest | null): boolean => this.pestService.comparePest(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ zonePest }) => {
      this.zonePest = zonePest;
      if (zonePest) {
        this.updateForm(zonePest);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const zonePest = this.zonePestFormService.getZonePest(this.editForm);
    if (zonePest.id !== null) {
      this.subscribeToSaveResponse(this.zonePestService.update(zonePest));
    } else {
      this.subscribeToSaveResponse(this.zonePestService.create(zonePest));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IZonePest>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(zonePest: IZonePest): void {
    this.zonePest = zonePest;
    this.zonePestFormService.resetForm(this.editForm, zonePest);

    this.zonesSharedCollection = this.zoneService.addZoneToCollectionIfMissing<IZone>(this.zonesSharedCollection, zonePest.zoneId);
    this.pestsSharedCollection = this.pestService.addPestToCollectionIfMissing<IPest>(this.pestsSharedCollection, zonePest.pestId);
  }

  protected loadRelationshipsOptions(): void {
    this.zoneService
      .query()
      .pipe(map((res: HttpResponse<IZone[]>) => res.body ?? []))
      .pipe(map((zones: IZone[]) => this.zoneService.addZoneToCollectionIfMissing<IZone>(zones, this.zonePest?.zoneId)))
      .subscribe((zones: IZone[]) => (this.zonesSharedCollection = zones));

    this.pestService
      .query()
      .pipe(map((res: HttpResponse<IPest[]>) => res.body ?? []))
      .pipe(map((pests: IPest[]) => this.pestService.addPestToCollectionIfMissing<IPest>(pests, this.zonePest?.pestId)))
      .subscribe((pests: IPest[]) => (this.pestsSharedCollection = pests));
  }
}
