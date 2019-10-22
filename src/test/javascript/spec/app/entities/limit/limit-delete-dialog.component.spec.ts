/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { CompositeSelectorTestModule } from '../../../test.module';
import { LimitDeleteDialogComponent } from 'app/entities/limit/limit-delete-dialog.component';
import { LimitService } from 'app/entities/limit/limit.service';

describe('Component Tests', () => {
  describe('Limit Management Delete Component', () => {
    let comp: LimitDeleteDialogComponent;
    let fixture: ComponentFixture<LimitDeleteDialogComponent>;
    let service: LimitService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [LimitDeleteDialogComponent]
      })
        .overrideTemplate(LimitDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LimitDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LimitService);
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
