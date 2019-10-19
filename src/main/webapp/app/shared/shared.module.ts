import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CompositeSelectorSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [CompositeSelectorSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [CompositeSelectorSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CompositeSelectorSharedModule {
  static forRoot() {
    return {
      ngModule: CompositeSelectorSharedModule
    };
  }
}
