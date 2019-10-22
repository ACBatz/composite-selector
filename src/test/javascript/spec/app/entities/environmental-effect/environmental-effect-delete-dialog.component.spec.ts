/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { CompositeSelectorTestModule } from '../../../test.module';
import { EnvironmentalEffectDeleteDialogComponent } from 'app/entities/environmental-effect/environmental-effect-delete-dialog.component';
import { EnvironmentalEffectService } from 'app/entities/environmental-effect/environmental-effect.service';

describe('Component Tests', () => {
  describe('EnvironmentalEffect Management Delete Component', () => {
    let comp: EnvironmentalEffectDeleteDialogComponent;
    let fixture: ComponentFixture<EnvironmentalEffectDeleteDialogComponent>;
    let service: EnvironmentalEffectService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [EnvironmentalEffectDeleteDialogComponent]
      })
        .overrideTemplate(EnvironmentalEffectDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EnvironmentalEffectDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(EnvironmentalEffectService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
