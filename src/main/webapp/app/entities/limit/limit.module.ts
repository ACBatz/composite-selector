import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CompositeSelectorSharedModule } from 'app/shared';
import {
  LimitComponent,
  LimitDetailComponent,
  LimitUpdateComponent,
  LimitDeletePopupComponent,
  LimitDeleteDialogComponent,
  limitRoute,
  limitPopupRoute
} from './';

const ENTITY_STATES = [...limitRoute, ...limitPopupRoute];

@NgModule({
  imports: [CompositeSelectorSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [LimitComponent, LimitDetailComponent, LimitUpdateComponent, LimitDeleteDialogComponent, LimitDeletePopupComponent],
  entryComponents: [LimitComponent, LimitUpdateComponent, LimitDeleteDialogComponent, LimitDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CompositeSelectorLimitModule {}
