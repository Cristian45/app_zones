import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPest, NewPest } from '../pest.model';

export type PartialUpdatePest = Partial<IPest> & Pick<IPest, 'id'>;

type RestOf<T extends IPest | NewPest> = Omit<T, 'createdat' | 'updatedat'> & {
  createdat?: string | null;
  updatedat?: string | null;
};

export type RestPest = RestOf<IPest>;

export type NewRestPest = RestOf<NewPest>;

export type PartialUpdateRestPest = RestOf<PartialUpdatePest>;

export type EntityResponseType = HttpResponse<IPest>;
export type EntityArrayResponseType = HttpResponse<IPest[]>;

@Injectable({ providedIn: 'root' })
export class PestService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pests', 'backzone');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pest: NewPest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pest);
    return this.http.post<RestPest>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(pest: IPest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pest);
    return this.http
      .put<RestPest>(`${this.resourceUrl}/${this.getPestIdentifier(pest)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(pest: PartialUpdatePest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pest);
    return this.http
      .patch<RestPest>(`${this.resourceUrl}/${this.getPestIdentifier(pest)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPest>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPest[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPestIdentifier(pest: Pick<IPest, 'id'>): number {
    return pest.id;
  }

  comparePest(o1: Pick<IPest, 'id'> | null, o2: Pick<IPest, 'id'> | null): boolean {
    return o1 && o2 ? this.getPestIdentifier(o1) === this.getPestIdentifier(o2) : o1 === o2;
  }

  addPestToCollectionIfMissing<Type extends Pick<IPest, 'id'>>(
    pestCollection: Type[],
    ...pestsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const pests: Type[] = pestsToCheck.filter(isPresent);
    if (pests.length > 0) {
      const pestCollectionIdentifiers = pestCollection.map(pestItem => this.getPestIdentifier(pestItem)!);
      const pestsToAdd = pests.filter(pestItem => {
        const pestIdentifier = this.getPestIdentifier(pestItem);
        if (pestCollectionIdentifiers.includes(pestIdentifier)) {
          return false;
        }
        pestCollectionIdentifiers.push(pestIdentifier);
        return true;
      });
      return [...pestsToAdd, ...pestCollection];
    }
    return pestCollection;
  }

  protected convertDateFromClient<T extends IPest | NewPest | PartialUpdatePest>(pest: T): RestOf<T> {
    return {
      ...pest,
      createdat: pest.createdat?.toJSON() ?? null,
      updatedat: pest.updatedat?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restPest: RestPest): IPest {
    return {
      ...restPest,
      createdat: restPest.createdat ? dayjs(restPest.createdat) : undefined,
      updatedat: restPest.updatedat ? dayjs(restPest.updatedat) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPest>): HttpResponse<IPest> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPest[]>): HttpResponse<IPest[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
