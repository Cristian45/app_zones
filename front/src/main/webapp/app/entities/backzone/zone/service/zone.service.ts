import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IZone, NewZone } from '../zone.model';
import { IDataPest } from '../zonepest.model';

export type PartialUpdateZone = Partial<IZone> & Pick<IZone, 'id'>;

type RestOf<T extends IZone | NewZone> = Omit<T, 'createdat' | 'updatedat'> & {
  createdat?: string | null;
  updatedat?: string | null;
};

type RestOfA<T extends IDataPest> = Omit<T, 'createdat' | 'updatedat'> & {
  createdat?: string | null;
  updatedat?: string | null;
};

export type RestZone = RestOf<IZone>;

export type RestDifZone = RestOfA<IDataPest>;

export type NewRestZone = RestOf<NewZone>;

export type PartialUpdateRestZone = RestOf<PartialUpdateZone>;

export type EntityResponseType = HttpResponse<IZone>;
export type EntityArrayResponseType = HttpResponse<IZone[]>;
export type EntityResponseTypeA = HttpResponse<IDataPest>;

@Injectable({ providedIn: 'root' })
export class ZoneService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/zones', 'backzone');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(zone: NewZone): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(zone);
    return this.http.post<RestZone>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(zone: IZone): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(zone);
    return this.http
      .put<RestZone>(`${this.resourceUrl}/${this.getZoneIdentifier(zone)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(zone: PartialUpdateZone): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(zone);
    return this.http
      .patch<RestZone>(`${this.resourceUrl}/${this.getZoneIdentifier(zone)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestZone>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }


  findDataPest(id: number): Observable<EntityResponseTypeA> {
    return this.http
      .get<RestDifZone>(`${this.resourceUrl}/datapest/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServerData(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestZone[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getZoneIdentifier(zone: Pick<IZone, 'id'>): number {
    return zone.id;
  }

  compareZone(o1: Pick<IZone, 'id'> | null, o2: Pick<IZone, 'id'> | null): boolean {
    return o1 && o2 ? this.getZoneIdentifier(o1) === this.getZoneIdentifier(o2) : o1 === o2;
  }

  addZoneToCollectionIfMissing<Type extends Pick<IZone, 'id'>>(
    zoneCollection: Type[],
    ...zonesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const zones: Type[] = zonesToCheck.filter(isPresent);
    if (zones.length > 0) {
      const zoneCollectionIdentifiers = zoneCollection.map(zoneItem => this.getZoneIdentifier(zoneItem)!);
      const zonesToAdd = zones.filter(zoneItem => {
        const zoneIdentifier = this.getZoneIdentifier(zoneItem);
        if (zoneCollectionIdentifiers.includes(zoneIdentifier)) {
          return false;
        }
        zoneCollectionIdentifiers.push(zoneIdentifier);
        return true;
      });
      return [...zonesToAdd, ...zoneCollection];
    }
    return zoneCollection;
  }

  protected convertDateFromClient<T extends IZone | NewZone | PartialUpdateZone>(zone: T): RestOf<T> {
    return {
      ...zone,
      createdat: zone.createdat?.toJSON() ?? null,
      updatedat: zone.updatedat?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restZone: RestZone): IZone {
    return {
      ...restZone,
      createdat: restZone.createdat ? dayjs(restZone.createdat) : undefined,
      updatedat: restZone.updatedat ? dayjs(restZone.updatedat) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestZone>): HttpResponse<IZone> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertDateFromServerData(restZone: RestDifZone): IDataPest {
    return {
      ...restZone,
    };
  }


  protected convertResponseFromServerData(res: HttpResponse<any>): HttpResponse<IDataPest> {
    return res.clone({
      body: res.body ? this.convertDateFromServerData(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestZone[]>): HttpResponse<IZone[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
