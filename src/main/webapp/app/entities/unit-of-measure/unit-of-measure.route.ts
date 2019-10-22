import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { UnitOfMeasure } from 'app/shared/model/unit-of-measure.model';
import { UnitOfMeasureService } from './unit-of-measure.service';
import { UnitOfMeasureComponent } from './unit-of-measure.component';
import { UnitOfMeasureDetailComponent } from './unit-of-measure-detail.component';
import { UnitOfMeasureUpdateComponent } from './unit-of-measure-update.component';
import { UnitOfMeasureDeletePopupComponent } from './unit-of-measure-delete-dialog.component';
import { IUnitOfMeasure } from 'app/shared/model/unit-of-measure.model';

@Injectable({ providedIn: 'root' })
export class UnitOfMeasureResolve implements Resolve<IUnitOfMeasure> {
  constructor(private service: UnitOfMeasureService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IUnitOfMeasure> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<UnitOfMeasure>) => response.ok),
        map((unitOfMeasure: HttpResponse<UnitOfMeasure>) => unitOfMeasure.body)
      );
    }
    return of(new UnitOfMeasure());
  }
}

export const unitOfMeasureRoute: Routes = [
  {
    path: '',
    component: UnitOfMeasureComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'UnitOfMeasures'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: UnitOfMeasureDetailComponent,
    resolve: {
      unitOfMeasure: UnitOfMeasureResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'UnitOfMeasures'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: UnitOfMeasureUpdateComponent,
    resolve: {
      unitOfMeasure: UnitOfMeasureResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'UnitOfMeasures'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: UnitOfMeasureUpdateComponent,
    resolve: {
      unitOfMeasure: UnitOfMeasureResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'UnitOfMeasures'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const unitOfMeasurePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: UnitOfMeasureDeletePopupComponent,
    resolve: {
      unitOfMeasure: UnitOfMeasureResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'UnitOfMeasures'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
