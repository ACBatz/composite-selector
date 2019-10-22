import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CompositeSelectorSharedModule } from 'app/shared';
import {
  UnitOfMeasureComponent,
  UnitOfMeasureDetailComponent,
  UnitOfMeasureUpdateComponent,
  UnitOfMeasureDeletePopupComponent,
  UnitOfMeasureDeleteDialogComponent,
  unitOfMeasureRoute,
  unitOfMeasurePopupRoute
} from './';

const ENTITY_STATES = [...unitOfMeasureRoute, ...unitOfMeasurePopupRoute];

@NgModule({
  imports: [CompositeSelectorSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    UnitOfMeasureComponent,
    UnitOfMeasureDetailComponent,
    UnitOfMeasureUpdateComponent,
    UnitOfMeasureDeleteDialogComponent,
    UnitOfMeasureDeletePopupComponent
  ],
  entryComponents: [
    UnitOfMeasureComponent,
    UnitOfMeasureUpdateComponent,
    UnitOfMeasureDeleteDialogComponent,
    UnitOfMeasureDeletePopupComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CompositeSelectorUnitOfMeasureModule {}
