/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { CompositeSelectorTestModule } from '../../../test.module';
import { NounDeleteDialogComponent } from 'app/entities/noun/noun-delete-dialog.component';
import { NounService } from 'app/entities/noun/noun.service';

describe('Component Tests', () => {
  describe('Noun Management Delete Component', () => {
    let comp: NounDeleteDialogComponent;
    let fixture: ComponentFixture<NounDeleteDialogComponent>;
    let service: NounService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [NounDeleteDialogComponent]
      })
        .overrideTemplate(NounDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(NounDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(NounService);
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
