/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { CompositeSelectorTestModule } from '../../../test.module';
import { LoadUpdateComponent } from 'app/entities/load/load-update.component';
import { LoadService } from 'app/entities/load/load.service';
import { Load } from 'app/shared/model/load.model';

describe('Component Tests', () => {
  describe('Load Management Update Component', () => {
    let comp: LoadUpdateComponent;
    let fixture: ComponentFixture<LoadUpdateComponent>;
    let service: LoadService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [LoadUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(LoadUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LoadUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LoadService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Load(123);
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
        const entity = new Load();
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
