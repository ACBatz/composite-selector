/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { CompositeSelectorTestModule } from '../../../test.module';
import { VerbUpdateComponent } from 'app/entities/verb/verb-update.component';
import { VerbService } from 'app/entities/verb/verb.service';
import { Verb } from 'app/shared/model/verb.model';

describe('Component Tests', () => {
  describe('Verb Management Update Component', () => {
    let comp: VerbUpdateComponent;
    let fixture: ComponentFixture<VerbUpdateComponent>;
    let service: VerbService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [VerbUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(VerbUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VerbUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(VerbService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Verb(123);
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
        const entity = new Verb();
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
