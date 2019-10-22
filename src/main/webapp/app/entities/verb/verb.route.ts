import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Verb } from 'app/shared/model/verb.model';
import { VerbService } from './verb.service';
import { VerbComponent } from './verb.component';
import { VerbDetailComponent } from './verb-detail.component';
import { VerbUpdateComponent } from './verb-update.component';
import { VerbDeletePopupComponent } from './verb-delete-dialog.component';
import { IVerb } from 'app/shared/model/verb.model';

@Injectable({ providedIn: 'root' })
export class VerbResolve implements Resolve<IVerb> {
  constructor(private service: VerbService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IVerb> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Verb>) => response.ok),
        map((verb: HttpResponse<Verb>) => verb.body)
      );
    }
    return of(new Verb());
  }
}

export const verbRoute: Routes = [
  {
    path: '',
    component: VerbComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Verbs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: VerbDetailComponent,
    resolve: {
      verb: VerbResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Verbs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: VerbUpdateComponent,
    resolve: {
      verb: VerbResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Verbs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: VerbUpdateComponent,
    resolve: {
      verb: VerbResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Verbs'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const verbPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: VerbDeletePopupComponent,
    resolve: {
      verb: VerbResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Verbs'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
