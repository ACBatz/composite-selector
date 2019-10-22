import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Load } from 'app/shared/model/load.model';
import { LoadService } from './load.service';
import { LoadComponent } from './load.component';
import { LoadDetailComponent } from './load-detail.component';
import { LoadUpdateComponent } from './load-update.component';
import { LoadDeletePopupComponent } from './load-delete-dialog.component';
import { ILoad } from 'app/shared/model/load.model';

@Injectable({ providedIn: 'root' })
export class LoadResolve implements Resolve<ILoad> {
  constructor(private service: LoadService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ILoad> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Load>) => response.ok),
        map((load: HttpResponse<Load>) => load.body)
      );
    }
    return of(new Load());
  }
}

export const loadRoute: Routes = [
  {
    path: '',
    component: LoadComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Loads'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: LoadDetailComponent,
    resolve: {
      load: LoadResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Loads'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: LoadUpdateComponent,
    resolve: {
      load: LoadResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Loads'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: LoadUpdateComponent,
    resolve: {
      load: LoadResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Loads'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const loadPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: LoadDeletePopupComponent,
    resolve: {
      load: LoadResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Loads'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
