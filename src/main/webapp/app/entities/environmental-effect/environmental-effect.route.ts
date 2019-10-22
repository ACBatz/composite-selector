import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { EnvironmentalEffect } from 'app/shared/model/environmental-effect.model';
import { EnvironmentalEffectService } from './environmental-effect.service';
import { EnvironmentalEffectComponent } from './environmental-effect.component';
import { EnvironmentalEffectDetailComponent } from './environmental-effect-detail.component';
import { EnvironmentalEffectUpdateComponent } from './environmental-effect-update.component';
import { EnvironmentalEffectDeletePopupComponent } from './environmental-effect-delete-dialog.component';
import { IEnvironmentalEffect } from 'app/shared/model/environmental-effect.model';

@Injectable({ providedIn: 'root' })
export class EnvironmentalEffectResolve implements Resolve<IEnvironmentalEffect> {
  constructor(private service: EnvironmentalEffectService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IEnvironmentalEffect> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<EnvironmentalEffect>) => response.ok),
        map((environmentalEffect: HttpResponse<EnvironmentalEffect>) => environmentalEffect.body)
      );
    }
    return of(new EnvironmentalEffect());
  }
}

export const environmentalEffectRoute: Routes = [
  {
    path: '',
    component: EnvironmentalEffectComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'EnvironmentalEffects'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: EnvironmentalEffectDetailComponent,
    resolve: {
      environmentalEffect: EnvironmentalEffectResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'EnvironmentalEffects'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: EnvironmentalEffectUpdateComponent,
    resolve: {
      environmentalEffect: EnvironmentalEffectResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'EnvironmentalEffects'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: EnvironmentalEffectUpdateComponent,
    resolve: {
      environmentalEffect: EnvironmentalEffectResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'EnvironmentalEffects'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const environmentalEffectPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: EnvironmentalEffectDeletePopupComponent,
    resolve: {
      environmentalEffect: EnvironmentalEffectResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'EnvironmentalEffects'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
