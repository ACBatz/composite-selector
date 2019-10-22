/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { CompositeSelectorTestModule } from '../../../test.module';
import { LoadShearUpdateComponent } from 'app/entities/load-shear/load-shear-update.component';
import { LoadShearService } from 'app/entities/load-shear/load-shear.service';
import { LoadShear } from 'app/shared/model/load-shear.model';

describe('Component Tests', () => {
  describe('LoadShear Management Update Component', () => {
    let comp: LoadShearUpdateComponent;
    let fixture: ComponentFixture<LoadShearUpdateComponent>;
    let service: LoadShearService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [LoadShearUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(LoadShearUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LoadShearUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LoadShearService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new LoadShear(123);
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
        const entity = new LoadShear();
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
