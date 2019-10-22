import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Limit } from 'app/shared/model/limit.model';
import { LimitService } from './limit.service';
import { LimitComponent } from './limit.component';
import { LimitDetailComponent } from './limit-detail.component';
import { LimitUpdateComponent } from './limit-update.component';
import { LimitDeletePopupComponent } from './limit-delete-dialog.component';
import { ILimit } from 'app/shared/model/limit.model';

@Injectable({ providedIn: 'root' })
export class LimitResolve implements Resolve<ILimit> {
  constructor(private service: LimitService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ILimit> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Limit>) => response.ok),
        map((limit: HttpResponse<Limit>) => limit.body)
      );
    }
    return of(new Limit());
  }
}

export const limitRoute: Routes = [
  {
    path: '',
    component: LimitComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Limits'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: LimitDetailComponent,
    resolve: {
      limit: LimitResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Limits'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: LimitUpdateComponent,
    resolve: {
      limit: LimitResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Limits'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: LimitUpdateComponent,
    resolve: {
      limit: LimitResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Limits'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const limitPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: LimitDeletePopupComponent,
    resolve: {
      limit: LimitResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Limits'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
