import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { WeightingFactor } from 'app/shared/model/weighting-factor.model';
import { WeightingFactorService } from './weighting-factor.service';
import { WeightingFactorComponent } from './weighting-factor.component';
import { WeightingFactorDetailComponent } from './weighting-factor-detail.component';
import { WeightingFactorUpdateComponent } from './weighting-factor-update.component';
import { WeightingFactorDeletePopupComponent } from './weighting-factor-delete-dialog.component';
import { IWeightingFactor } from 'app/shared/model/weighting-factor.model';

@Injectable({ providedIn: 'root' })
export class WeightingFactorResolve implements Resolve<IWeightingFactor> {
  constructor(private service: WeightingFactorService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IWeightingFactor> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<WeightingFactor>) => response.ok),
        map((weightingFactor: HttpResponse<WeightingFactor>) => weightingFactor.body)
      );
    }
    return of(new WeightingFactor());
  }
}

export const weightingFactorRoute: Routes = [
  {
    path: '',
    component: WeightingFactorComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'WeightingFactors'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: WeightingFactorDetailComponent,
    resolve: {
      weightingFactor: WeightingFactorResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'WeightingFactors'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: WeightingFactorUpdateComponent,
    resolve: {
      weightingFactor: WeightingFactorResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'WeightingFactors'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: WeightingFactorUpdateComponent,
    resolve: {
      weightingFactor: WeightingFactorResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'WeightingFactors'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const weightingFactorPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: WeightingFactorDeletePopupComponent,
    resolve: {
      weightingFactor: WeightingFactorResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'WeightingFactors'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
