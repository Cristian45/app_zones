import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPest } from '../pest.model';
import { PestService } from '../service/pest.service';

@Injectable({ providedIn: 'root' })
export class PestRoutingResolveService implements Resolve<IPest | null> {
  constructor(protected service: PestService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPest | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pest: HttpResponse<IPest>) => {
          if (pest.body) {
            return of(pest.body);
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
