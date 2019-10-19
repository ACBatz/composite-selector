import { NgModule } from '@angular/core';

import { CompositeSelectorSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
  imports: [CompositeSelectorSharedLibsModule],
  declarations: [JhiAlertComponent, JhiAlertErrorComponent],
  exports: [CompositeSelectorSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class CompositeSelectorSharedCommonModule {}
