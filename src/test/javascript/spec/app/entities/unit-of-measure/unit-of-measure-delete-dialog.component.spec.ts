/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { CompositeSelectorTestModule } from '../../../test.module';
import { UnitOfMeasureDeleteDialogComponent } from 'app/entities/unit-of-measure/unit-of-measure-delete-dialog.component';
import { UnitOfMeasureService } from 'app/entities/unit-of-measure/unit-of-measure.service';

describe('Component Tests', () => {
  describe('UnitOfMeasure Management Delete Component', () => {
    let comp: UnitOfMeasureDeleteDialogComponent;
    let fixture: ComponentFixture<UnitOfMeasureDeleteDialogComponent>;
    let service: UnitOfMeasureService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [UnitOfMeasureDeleteDialogComponent]
      })
        .overrideTemplate(UnitOfMeasureDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UnitOfMeasureDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UnitOfMeasureService);
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
