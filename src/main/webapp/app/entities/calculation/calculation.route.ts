import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Calculation } from 'app/shared/model/calculation.model';
import { CalculationService } from './calculation.service';
import { CalculationComponent } from './calculation.component';
import { CalculationDetailComponent } from './calculation-detail.component';
import { CalculationUpdateComponent } from './calculation-update.component';
import { CalculationDeletePopupComponent } from './calculation-delete-dialog.component';
import { ICalculation } from 'app/shared/model/calculation.model';

@Injectable({ providedIn: 'root' })
export class CalculationResolve implements Resolve<ICalculation> {
  constructor(private service: CalculationService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ICalculation> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Calculation>) => response.ok),
        map((calculation: HttpResponse<Calculation>) => calculation.body)
      );
    }
    return of(new Calculation());
  }
}

export const calculationRoute: Routes = [
  {
    path: '',
    component: CalculationComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Calculations'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CalculationDetailComponent,
    resolve: {
      calculation: CalculationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Calculations'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CalculationUpdateComponent,
    resolve: {
      calculation: CalculationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Calculations'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CalculationUpdateComponent,
    resolve: {
      calculation: CalculationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Calculations'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const calculationPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: CalculationDeletePopupComponent,
    resolve: {
      calculation: CalculationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Calculations'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
