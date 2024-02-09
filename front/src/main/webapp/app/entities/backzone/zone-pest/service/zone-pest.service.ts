import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IZonePest, NewZonePest } from '../zone-pest.model';
import { IZonePestWithName } from '../zone-pest-with-name.model';

export type PartialUpdateZonePest = Partial<IZonePest> & Pick<IZonePest, 'id'>;

type RestOf<T extends IZonePest | NewZonePest> = Omit<T, 'createdat' | 'updatedat'> & {
  createdat?: string | null;
  updatedat?: string | null;
};

type RestOfA<T extends IZonePestWithName> = Omit<T, 'createdat' | 'updatedat'> & {
  createdat?: string | null;
  updatedat?: string | null;
};

export type RestZonePest = RestOf<IZonePest>;

export type NewRestZonePest = RestOf<NewZonePest>;

export type PartialUpdateRestZonePest = RestOf<PartialUpdateZonePest>;

export type EntityResponseType = HttpResponse<IZonePest>;
export type EntityArrayResponseType = HttpResponse<IZonePest[]>;

export type RestZonePestWithName = RestOfA<IZonePestWithName>;

export type EntityArrayResponseTypeWithName = HttpResponse<IZonePestWithName[]>;

@Injectable({ providedIn: 'root' })
export class ZonePestService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/zone-pests', 'backzone');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(zonePest: NewZonePest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(zonePest);
    return this.http
      .post<RestZonePest>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(zonePest: IZonePest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(zonePest);
    return this.http
      .put<RestZonePest>(`${this.resourceUrl}/${this.getZonePestIdentifier(zonePest)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(zonePest: PartialUpdateZonePest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(zonePest);
    return this.http
      .patch<RestZonePest>(`${this.resourceUrl}/${this.getZonePestIdentifier(zonePest)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestZonePest>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  findDataPest(): Observable<EntityArrayResponseTypeWithName> {
    return this.http
      .get<RestZonePestWithName[]>(`${this.resourceUrl}/zonepestwithzonename`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServerWithName(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestZonePest[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getZonePestIdentifier(zonePest: Pick<IZonePest, 'id'>): number {
    return zonePest.id;
  }

  compareZonePest(o1: Pick<IZonePest, 'id'> | null, o2: Pick<IZonePest, 'id'> | null): boolean {
    return o1 && o2 ? this.getZonePestIdentifier(o1) === this.getZonePestIdentifier(o2) : o1 === o2;
  }

  addZonePestToCollectionIfMissing<Type extends Pick<IZonePest, 'id'>>(
    zonePestCollection: Type[],
    ...zonePestsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const zonePests: Type[] = zonePestsToCheck.filter(isPresent);
    if (zonePests.length > 0) {
      const zonePestCollectionIdentifiers = zonePestCollection.map(zonePestItem => this.getZonePestIdentifier(zonePestItem)!);
      const zonePestsToAdd = zonePests.filter(zonePestItem => {
        const zonePestIdentifier = this.getZonePestIdentifier(zonePestItem);
        if (zonePestCollectionIdentifiers.includes(zonePestIdentifier)) {
          return false;
        }
        zonePestCollectionIdentifiers.push(zonePestIdentifier);
        return true;
      });
      return [...zonePestsToAdd, ...zonePestCollection];
    }
    return zonePestCollection;
  }

  protected convertDateFromClient<T extends IZonePest | NewZonePest | PartialUpdateZonePest>(zonePest: T): RestOf<T> {
    return {
      ...zonePest,
      createdat: zonePest.createdat?.toJSON() ?? null,
      updatedat: zonePest.updatedat?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restZonePest: RestZonePest): IZonePest {
    return {
      ...restZonePest,
      createdat: restZonePest.createdat ? dayjs(restZonePest.createdat) : undefined,
      updatedat: restZonePest.updatedat ? dayjs(restZonePest.updatedat) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestZonePest>): HttpResponse<IZonePest> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestZonePest[]>): HttpResponse<IZonePest[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }


  //----data wiith name

  protected convertDateFromServerWithName(restZonePest: RestZonePestWithName): IZonePestWithName {
    return {
      ...restZonePest
    };
  }


  protected convertResponseFromServerWithName(res: HttpResponse<RestZonePestWithName[]>): HttpResponse<IZonePestWithName[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServerWithName(item)) : null,
    });
  }







}
