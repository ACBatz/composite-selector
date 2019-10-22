import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CompositeSelectorSharedModule } from 'app/shared';
import {
  MiscellaneousConstraintComponent,
  MiscellaneousConstraintDetailComponent,
  MiscellaneousConstraintUpdateComponent,
  MiscellaneousConstraintDeletePopupComponent,
  MiscellaneousConstraintDeleteDialogComponent,
  miscellaneousConstraintRoute,
  miscellaneousConstraintPopupRoute
} from './';

const ENTITY_STATES = [...miscellaneousConstraintRoute, ...miscellaneousConstraintPopupRoute];

@NgModule({
  imports: [CompositeSelectorSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    MiscellaneousConstraintComponent,
    MiscellaneousConstraintDetailComponent,
    MiscellaneousConstraintUpdateComponent,
    MiscellaneousConstraintDeleteDialogComponent,
    MiscellaneousConstraintDeletePopupComponent
  ],
  entryComponents: [
    MiscellaneousConstraintComponent,
    MiscellaneousConstraintUpdateComponent,
    MiscellaneousConstraintDeleteDialogComponent,
    MiscellaneousConstraintDeletePopupComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CompositeSelectorMiscellaneousConstraintModule {}
