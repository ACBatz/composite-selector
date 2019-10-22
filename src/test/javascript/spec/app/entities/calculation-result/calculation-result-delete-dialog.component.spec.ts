/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { CompositeSelectorTestModule } from '../../../test.module';
import { CalculationResultDeleteDialogComponent } from 'app/entities/calculation-result/calculation-result-delete-dialog.component';
import { CalculationResultService } from 'app/entities/calculation-result/calculation-result.service';

describe('Component Tests', () => {
  describe('CalculationResult Management Delete Component', () => {
    let comp: CalculationResultDeleteDialogComponent;
    let fixture: ComponentFixture<CalculationResultDeleteDialogComponent>;
    let service: CalculationResultService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [CalculationResultDeleteDialogComponent]
      })
        .overrideTemplate(CalculationResultDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CalculationResultDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CalculationResultService);
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
