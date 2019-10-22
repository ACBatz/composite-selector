/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { CompositeSelectorTestModule } from '../../../test.module';
import { WeightingFactorUpdateComponent } from 'app/entities/weighting-factor/weighting-factor-update.component';
import { WeightingFactorService } from 'app/entities/weighting-factor/weighting-factor.service';
import { WeightingFactor } from 'app/shared/model/weighting-factor.model';

describe('Component Tests', () => {
  describe('WeightingFactor Management Update Component', () => {
    let comp: WeightingFactorUpdateComponent;
    let fixture: ComponentFixture<WeightingFactorUpdateComponent>;
    let service: WeightingFactorService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [WeightingFactorUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(WeightingFactorUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WeightingFactorUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(WeightingFactorService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new WeightingFactor(123);
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
        const entity = new WeightingFactor();
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
