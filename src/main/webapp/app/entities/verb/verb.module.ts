import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CompositeSelectorSharedModule } from 'app/shared';
import {
  VerbComponent,
  VerbDetailComponent,
  VerbUpdateComponent,
  VerbDeletePopupComponent,
  VerbDeleteDialogComponent,
  verbRoute,
  verbPopupRoute
} from './';

const ENTITY_STATES = [...verbRoute, ...verbPopupRoute];

@NgModule({
  imports: [CompositeSelectorSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [VerbComponent, VerbDetailComponent, VerbUpdateComponent, VerbDeleteDialogComponent, VerbDeletePopupComponent],
  entryComponents: [VerbComponent, VerbUpdateComponent, VerbDeleteDialogComponent, VerbDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CompositeSelectorVerbModule {}
