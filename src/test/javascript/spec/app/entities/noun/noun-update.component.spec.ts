/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { CompositeSelectorTestModule } from '../../../test.module';
import { NounUpdateComponent } from 'app/entities/noun/noun-update.component';
import { NounService } from 'app/entities/noun/noun.service';
import { Noun } from 'app/shared/model/noun.model';

describe('Component Tests', () => {
  describe('Noun Management Update Component', () => {
    let comp: NounUpdateComponent;
    let fixture: ComponentFixture<NounUpdateComponent>;
    let service: NounService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [NounUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(NounUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(NounUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(NounService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Noun(123);
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
        const entity = new Noun();
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
