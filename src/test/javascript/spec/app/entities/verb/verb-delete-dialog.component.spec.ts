/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { CompositeSelectorTestModule } from '../../../test.module';
import { VerbDeleteDialogComponent } from 'app/entities/verb/verb-delete-dialog.component';
import { VerbService } from 'app/entities/verb/verb.service';

describe('Component Tests', () => {
  describe('Verb Management Delete Component', () => {
    let comp: VerbDeleteDialogComponent;
    let fixture: ComponentFixture<VerbDeleteDialogComponent>;
    let service: VerbService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [VerbDeleteDialogComponent]
      })
        .overrideTemplate(VerbDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(VerbDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(VerbService);
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
