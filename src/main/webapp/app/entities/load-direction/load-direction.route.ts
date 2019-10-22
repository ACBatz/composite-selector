import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { LoadDirection } from 'app/shared/model/load-direction.model';
import { LoadDirectionService } from './load-direction.service';
import { LoadDirectionComponent } from './load-direction.component';
import { LoadDirectionDetailComponent } from './load-direction-detail.component';
import { LoadDirectionUpdateComponent } from './load-direction-update.component';
import { LoadDirectionDeletePopupComponent } from './load-direction-delete-dialog.component';
import { ILoadDirection } from 'app/shared/model/load-direction.model';

@Injectable({ providedIn: 'root' })
export class LoadDirectionResolve implements Resolve<ILoadDirection> {
  constructor(private service: LoadDirectionService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ILoadDirection> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<LoadDirection>) => response.ok),
        map((loadDirection: HttpResponse<LoadDirection>) => loadDirection.body)
      );
    }
    return of(new LoadDirection());
  }
}

export const loadDirectionRoute: Routes = [
  {
    path: '',
    component: LoadDirectionComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'LoadDirections'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: LoadDirectionDetailComponent,
    resolve: {
      loadDirection: LoadDirectionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'LoadDirections'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: LoadDirectionUpdateComponent,
    resolve: {
      loadDirection: LoadDirectionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'LoadDirections'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: LoadDirectionUpdateComponent,
    resolve: {
      loadDirection: LoadDirectionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'LoadDirections'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const loadDirectionPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: LoadDirectionDeletePopupComponent,
    resolve: {
      loadDirection: LoadDirectionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'LoadDirections'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
