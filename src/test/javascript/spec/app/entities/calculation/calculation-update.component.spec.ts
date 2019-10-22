/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { CompositeSelectorTestModule } from '../../../test.module';
import { CalculationUpdateComponent } from 'app/entities/calculation/calculation-update.component';
import { CalculationService } from 'app/entities/calculation/calculation.service';
import { Calculation } from 'app/shared/model/calculation.model';

describe('Component Tests', () => {
  describe('Calculation Management Update Component', () => {
    let comp: CalculationUpdateComponent;
    let fixture: ComponentFixture<CalculationUpdateComponent>;
    let service: CalculationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [CalculationUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CalculationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CalculationUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CalculationService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Calculation(123);
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
        const entity = new Calculation();
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
