import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { CalculationResult } from 'app/shared/model/calculation-result.model';
import { CalculationResultService } from './calculation-result.service';
import { CalculationResultComponent } from './calculation-result.component';
import { CalculationResultDetailComponent } from './calculation-result-detail.component';
import { CalculationResultUpdateComponent } from './calculation-result-update.component';
import { CalculationResultDeletePopupComponent } from './calculation-result-delete-dialog.component';
import { ICalculationResult } from 'app/shared/model/calculation-result.model';

@Injectable({ providedIn: 'root' })
export class CalculationResultResolve implements Resolve<ICalculationResult> {
  constructor(private service: CalculationResultService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ICalculationResult> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<CalculationResult>) => response.ok),
        map((calculationResult: HttpResponse<CalculationResult>) => calculationResult.body)
      );
    }
    return of(new CalculationResult());
  }
}

export const calculationResultRoute: Routes = [
  {
    path: '',
    component: CalculationResultComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CalculationResults'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CalculationResultDetailComponent,
    resolve: {
      calculationResult: CalculationResultResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CalculationResults'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CalculationResultUpdateComponent,
    resolve: {
      calculationResult: CalculationResultResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CalculationResults'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CalculationResultUpdateComponent,
    resolve: {
      calculationResult: CalculationResultResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CalculationResults'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const calculationResultPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: CalculationResultDeletePopupComponent,
    resolve: {
      calculationResult: CalculationResultResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CalculationResults'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
