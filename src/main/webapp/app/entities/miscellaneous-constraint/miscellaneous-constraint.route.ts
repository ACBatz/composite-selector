import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { MiscellaneousConstraint } from 'app/shared/model/miscellaneous-constraint.model';
import { MiscellaneousConstraintService } from './miscellaneous-constraint.service';
import { MiscellaneousConstraintComponent } from './miscellaneous-constraint.component';
import { MiscellaneousConstraintDetailComponent } from './miscellaneous-constraint-detail.component';
import { MiscellaneousConstraintUpdateComponent } from './miscellaneous-constraint-update.component';
import { MiscellaneousConstraintDeletePopupComponent } from './miscellaneous-constraint-delete-dialog.component';
import { IMiscellaneousConstraint } from 'app/shared/model/miscellaneous-constraint.model';

@Injectable({ providedIn: 'root' })
export class MiscellaneousConstraintResolve implements Resolve<IMiscellaneousConstraint> {
  constructor(private service: MiscellaneousConstraintService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IMiscellaneousConstraint> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<MiscellaneousConstraint>) => response.ok),
        map((miscellaneousConstraint: HttpResponse<MiscellaneousConstraint>) => miscellaneousConstraint.body)
      );
    }
    return of(new MiscellaneousConstraint());
  }
}

export const miscellaneousConstraintRoute: Routes = [
  {
    path: '',
    component: MiscellaneousConstraintComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'MiscellaneousConstraints'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: MiscellaneousConstraintDetailComponent,
    resolve: {
      miscellaneousConstraint: MiscellaneousConstraintResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'MiscellaneousConstraints'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: MiscellaneousConstraintUpdateComponent,
    resolve: {
      miscellaneousConstraint: MiscellaneousConstraintResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'MiscellaneousConstraints'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: MiscellaneousConstraintUpdateComponent,
    resolve: {
      miscellaneousConstraint: MiscellaneousConstraintResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'MiscellaneousConstraints'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const miscellaneousConstraintPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: MiscellaneousConstraintDeletePopupComponent,
    resolve: {
      miscellaneousConstraint: MiscellaneousConstraintResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'MiscellaneousConstraints'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
