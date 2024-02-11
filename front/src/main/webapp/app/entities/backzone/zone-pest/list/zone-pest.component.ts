import { Component, OnInit } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, filter, Observable, switchMap, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IZonePest } from '../zone-pest.model';
import { IZonePestWithName } from '../zone-pest-with-name.model';

import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { EntityArrayResponseType, ZonePestService } from '../service/zone-pest.service';
import { ZonePestDeleteDialogComponent } from '../delete/zone-pest-delete-dialog.component';
import { ZoneService } from 'app/entities/backzone/zone/service/zone.service';

@Component({
  selector: 'jhi-zone-pest',
  templateUrl: './zone-pest.component.html',
})
export class ZonePestComponent implements OnInit {
  zonePests?: IZonePest[];
  isLoading = false;

  predicate = 'id';
  ascending = true;

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  oneZonePest?: IZonePest | null;

  zonePestCustom?: IZonePestWithName[];

  constructor(
    protected zonePestService: ZonePestService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected modalService: NgbModal,
    protected zoneService: ZoneService,
  ) {}

  trackId = (_index: number, item: IZonePest): number => this.zonePestService.getZonePestIdentifier(item);

  ngOnInit(): void {
    this.load();

    this.zonePestService.findDataPest().subscribe(
      response => {      
        //console.log('antes');  
        console.log(response.body);
        this.zonePestCustom = response.body || [];
        console.log(this.zonePestCustom);        
        //console.log('despues ');
      }
    );

  }

  delete(zonePest: IZonePest): void {
    const modalRef = this.modalService.open(ZonePestDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.zonePest = zonePest;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        switchMap(() => this.loadFromBackendWithRouteInformations())
      )
      .subscribe({
        next: (res: EntityArrayResponseType) => {
          this.onResponseSuccess(res);
        },
      });
  }


  deleteOneZonePest(id: any): void {
   this.zonePestService.find(id).subscribe(
      response =>{
        console.log(response.body);
        this.oneZonePest =response.body|| null 

        const modalRef = this.modalService.open(ZonePestDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.zonePest = this.oneZonePest;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        switchMap(() => this.loadFromBackendWithRouteInformations())
      )
      .subscribe({
        next: (res: EntityArrayResponseType) => {
          this.onResponseSuccess(res);
          this.zonePestService.findDataPest().subscribe(
            response => {      
              console.log('antes');  
              console.log(response.body);
              this.zonePestCustom = response.body || [];
              console.log(this.zonePestCustom);        
              console.log('despues ');
            }
          );
        },
      });
      }
   );
    
  }

  load(): void {
    this.loadFromBackendWithRouteInformations().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  navigateToWithComponentValues(): void {
    this.handleNavigation(this.page, this.predicate, this.ascending);
  }

  navigateToPage(page = this.page): void {
    this.handleNavigation(page, this.predicate, this.ascending);
  }

  protected loadFromBackendWithRouteInformations(): Observable<EntityArrayResponseType> {
    return combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data]).pipe(
      tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
      switchMap(() => this.queryBackend(this.page, this.predicate, this.ascending))
    );
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get(PAGE_HEADER);
    this.page = +(page ?? 1);
    const sort = (params.get(SORT) ?? data[DEFAULT_SORT_DATA]).split(',');
    this.predicate = sort[0];
    this.ascending = sort[1] === ASC;
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.zonePests = dataFromBody;
  }

  protected fillComponentAttributesFromResponseBody(data: IZonePest[] | null): IZonePest[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
  }

  protected queryBackend(page?: number, predicate?: string, ascending?: boolean): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const pageToLoad: number = page ?? 1;
    const queryObject = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      sort: this.getSortQueryParam(predicate, ascending),
    };
    return this.zonePestService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(page = this.page, predicate?: string, ascending?: boolean): void {
    const queryParamsObj = {
      page,
      size: this.itemsPerPage,
      sort: this.getSortQueryParam(predicate, ascending),
    };

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }

  protected getSortQueryParam(predicate = this.predicate, ascending = this.ascending): string[] {
    const ascendingQueryParam = ascending ? ASC : DESC;
    if (predicate === '') {
      return [];
    } else {
      return [predicate + ',' + ascendingQueryParam];
    }
  }

}
