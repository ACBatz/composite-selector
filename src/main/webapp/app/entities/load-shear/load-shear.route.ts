import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { LoadShear } from 'app/shared/model/load-shear.model';
import { LoadShearService } from './load-shear.service';
import { LoadShearComponent } from './load-shear.component';
import { LoadShearDetailComponent } from './load-shear-detail.component';
import { LoadShearUpdateComponent } from './load-shear-update.component';
import { LoadShearDeletePopupComponent } from './load-shear-delete-dialog.component';
import { ILoadShear } from 'app/shared/model/load-shear.model';

@Injectable({ providedIn: 'root' })
export class LoadShearResolve implements Resolve<ILoadShear> {
  constructor(private service: LoadShearService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ILoadShear> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<LoadShear>) => response.ok),
        map((loadShear: HttpResponse<LoadShear>) => loadShear.body)
      );
    }
    return of(new LoadShear());
  }
}

export const loadShearRoute: Routes = [
  {
    path: '',
    component: LoadShearComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'LoadShears'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: LoadShearDetailComponent,
    resolve: {
      loadShear: LoadShearResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'LoadShears'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: LoadShearUpdateComponent,
    resolve: {
      loadShear: LoadShearResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'LoadShears'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: LoadShearUpdateComponent,
    resolve: {
      loadShear: LoadShearResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'LoadShears'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const loadShearPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: LoadShearDeletePopupComponent,
    resolve: {
      loadShear: LoadShearResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'LoadShears'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
