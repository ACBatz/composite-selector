/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { CompositeSelectorTestModule } from '../../../test.module';
import { LoadDirectionUpdateComponent } from 'app/entities/load-direction/load-direction-update.component';
import { LoadDirectionService } from 'app/entities/load-direction/load-direction.service';
import { LoadDirection } from 'app/shared/model/load-direction.model';

describe('Component Tests', () => {
  describe('LoadDirection Management Update Component', () => {
    let comp: LoadDirectionUpdateComponent;
    let fixture: ComponentFixture<LoadDirectionUpdateComponent>;
    let service: LoadDirectionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [LoadDirectionUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(LoadDirectionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LoadDirectionUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LoadDirectionService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new LoadDirection(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new LoadDirection();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
