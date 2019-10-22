import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Noun } from 'app/shared/model/noun.model';
import { NounService } from './noun.service';
import { NounComponent } from './noun.component';
import { NounDetailComponent } from './noun-detail.component';
import { NounUpdateComponent } from './noun-update.component';
import { NounDeletePopupComponent } from './noun-delete-dialog.component';
import { INoun } from 'app/shared/model/noun.model';

@Injectable({ providedIn: 'root' })
export class NounResolve implements Resolve<INoun> {
  constructor(private service: NounService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<INoun> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Noun>) => response.ok),
        map((noun: HttpResponse<Noun>) => noun.body)
      );
    }
    return of(new Noun());
  }
}

export const nounRoute: Routes = [
  {
    path: '',
    component: NounComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Nouns'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: NounDetailComponent,
    resolve: {
      noun: NounResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Nouns'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: NounUpdateComponent,
    resolve: {
      noun: NounResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Nouns'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: NounUpdateComponent,
    resolve: {
      noun: NounResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Nouns'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const nounPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: NounDeletePopupComponent,
    resolve: {
      noun: NounResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Nouns'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
