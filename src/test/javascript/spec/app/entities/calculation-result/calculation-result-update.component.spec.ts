/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { CompositeSelectorTestModule } from '../../../test.module';
import { CalculationResultUpdateComponent } from 'app/entities/calculation-result/calculation-result-update.component';
import { CalculationResultService } from 'app/entities/calculation-result/calculation-result.service';
import { CalculationResult } from 'app/shared/model/calculation-result.model';

describe('Component Tests', () => {
  describe('CalculationResult Management Update Component', () => {
    let comp: CalculationResultUpdateComponent;
    let fixture: ComponentFixture<CalculationResultUpdateComponent>;
    let service: CalculationResultService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [CalculationResultUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CalculationResultUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CalculationResultUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CalculationResultService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CalculationResult(123);
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
        const entity = new CalculationResult();
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
