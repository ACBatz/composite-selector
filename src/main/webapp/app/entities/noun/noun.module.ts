import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CompositeSelectorSharedModule } from 'app/shared';
import {
  NounComponent,
  NounDetailComponent,
  NounUpdateComponent,
  NounDeletePopupComponent,
  NounDeleteDialogComponent,
  nounRoute,
  nounPopupRoute
} from './';

const ENTITY_STATES = [...nounRoute, ...nounPopupRoute];

@NgModule({
  imports: [CompositeSelectorSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [NounComponent, NounDetailComponent, NounUpdateComponent, NounDeleteDialogComponent, NounDeletePopupComponent],
  entryComponents: [NounComponent, NounUpdateComponent, NounDeleteDialogComponent, NounDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CompositeSelectorNounModule {}
