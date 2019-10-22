import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Composite } from 'app/shared/model/composite.model';
import { CompositeService } from './composite.service';
import { CompositeComponent } from './composite.component';
import { CompositeDetailComponent } from './composite-detail.component';
import { CompositeUpdateComponent } from './composite-update.component';
import { CompositeDeletePopupComponent } from './composite-delete-dialog.component';
import { IComposite } from 'app/shared/model/composite.model';

@Injectable({ providedIn: 'root' })
export class CompositeResolve implements Resolve<IComposite> {
  constructor(private service: CompositeService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IComposite> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Composite>) => response.ok),
        map((composite: HttpResponse<Composite>) => composite.body)
      );
    }
    return of(new Composite());
  }
}

export const compositeRoute: Routes = [
  {
    path: '',
    component: CompositeComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Composites'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CompositeDetailComponent,
    resolve: {
      composite: CompositeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Composites'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CompositeUpdateComponent,
    resolve: {
      composite: CompositeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Composites'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CompositeUpdateComponent,
    resolve: {
      composite: CompositeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Composites'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const compositePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: CompositeDeletePopupComponent,
    resolve: {
      composite: CompositeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Composites'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
