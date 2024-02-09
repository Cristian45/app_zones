import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IZonePest } from '../zone-pest.model';
import { ZonePestService } from '../service/zone-pest.service';

@Injectable({ providedIn: 'root' })
export class ZonePestRoutingResolveService implements Resolve<IZonePest | null> {
  constructor(protected service: ZonePestService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IZonePest | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((zonePest: HttpResponse<IZonePest>) => {
          if (zonePest.body) {
            return of(zonePest.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
