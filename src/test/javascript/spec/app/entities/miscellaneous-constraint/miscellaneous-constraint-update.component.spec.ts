/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { CompositeSelectorTestModule } from '../../../test.module';
import { MiscellaneousConstraintUpdateComponent } from 'app/entities/miscellaneous-constraint/miscellaneous-constraint-update.component';
import { MiscellaneousConstraintService } from 'app/entities/miscellaneous-constraint/miscellaneous-constraint.service';
import { MiscellaneousConstraint } from 'app/shared/model/miscellaneous-constraint.model';

describe('Component Tests', () => {
  describe('MiscellaneousConstraint Management Update Component', () => {
    let comp: MiscellaneousConstraintUpdateComponent;
    let fixture: ComponentFixture<MiscellaneousConstraintUpdateComponent>;
    let service: MiscellaneousConstraintService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [MiscellaneousConstraintUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(MiscellaneousConstraintUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MiscellaneousConstraintUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MiscellaneousConstraintService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new MiscellaneousConstraint(123);
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
        const entity = new MiscellaneousConstraint();
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
