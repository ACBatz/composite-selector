/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CompositeSelectorTestModule } from '../../../test.module';
import { CompositeComponent } from 'app/entities/composite/composite.component';
import { CompositeService } from 'app/entities/composite/composite.service';
import { Composite } from 'app/shared/model/composite.model';

describe('Component Tests', () => {
  describe('Composite Management Component', () => {
    let comp: CompositeComponent;
    let fixture: ComponentFixture<CompositeComponent>;
    let service: CompositeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [CompositeComponent],
        providers: []
      })
        .overrideTemplate(CompositeComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CompositeComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CompositeService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Composite(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.composites[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
