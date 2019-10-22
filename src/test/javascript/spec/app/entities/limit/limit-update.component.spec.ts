/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { CompositeSelectorTestModule } from '../../../test.module';
import { LimitUpdateComponent } from 'app/entities/limit/limit-update.component';
import { LimitService } from 'app/entities/limit/limit.service';
import { Limit } from 'app/shared/model/limit.model';

describe('Component Tests', () => {
  describe('Limit Management Update Component', () => {
    let comp: LimitUpdateComponent;
    let fixture: ComponentFixture<LimitUpdateComponent>;
    let service: LimitService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [LimitUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(LimitUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LimitUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LimitService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Limit(123);
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
        const entity = new Limit();
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
