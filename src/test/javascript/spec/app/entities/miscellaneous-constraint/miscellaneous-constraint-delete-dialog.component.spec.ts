/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { CompositeSelectorTestModule } from '../../../test.module';
import { MiscellaneousConstraintDeleteDialogComponent } from 'app/entities/miscellaneous-constraint/miscellaneous-constraint-delete-dialog.component';
import { MiscellaneousConstraintService } from 'app/entities/miscellaneous-constraint/miscellaneous-constraint.service';

describe('Component Tests', () => {
  describe('MiscellaneousConstraint Management Delete Component', () => {
    let comp: MiscellaneousConstraintDeleteDialogComponent;
    let fixture: ComponentFixture<MiscellaneousConstraintDeleteDialogComponent>;
    let service: MiscellaneousConstraintService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [MiscellaneousConstraintDeleteDialogComponent]
      })
        .overrideTemplate(MiscellaneousConstraintDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MiscellaneousConstraintDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MiscellaneousConstraintService);
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
