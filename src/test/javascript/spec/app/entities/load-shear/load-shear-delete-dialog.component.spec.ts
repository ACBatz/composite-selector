/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { CompositeSelectorTestModule } from '../../../test.module';
import { LoadShearDeleteDialogComponent } from 'app/entities/load-shear/load-shear-delete-dialog.component';
import { LoadShearService } from 'app/entities/load-shear/load-shear.service';

describe('Component Tests', () => {
  describe('LoadShear Management Delete Component', () => {
    let comp: LoadShearDeleteDialogComponent;
    let fixture: ComponentFixture<LoadShearDeleteDialogComponent>;
    let service: LoadShearService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [LoadShearDeleteDialogComponent]
      })
        .overrideTemplate(LoadShearDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LoadShearDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LoadShearService);
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
