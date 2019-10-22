/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CompositeSelectorTestModule } from '../../../test.module';
import { UnitOfMeasureComponent } from 'app/entities/unit-of-measure/unit-of-measure.component';
import { UnitOfMeasureService } from 'app/entities/unit-of-measure/unit-of-measure.service';
import { UnitOfMeasure } from 'app/shared/model/unit-of-measure.model';

describe('Component Tests', () => {
  describe('UnitOfMeasure Management Component', () => {
    let comp: UnitOfMeasureComponent;
    let fixture: ComponentFixture<UnitOfMeasureComponent>;
    let service: UnitOfMeasureService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [UnitOfMeasureComponent],
        providers: []
      })
        .overrideTemplate(UnitOfMeasureComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UnitOfMeasureComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UnitOfMeasureService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new UnitOfMeasure(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.unitOfMeasures[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
