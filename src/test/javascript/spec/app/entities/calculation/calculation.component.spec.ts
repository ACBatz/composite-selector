/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CompositeSelectorTestModule } from '../../../test.module';
import { CalculationComponent } from 'app/entities/calculation/calculation.component';
import { CalculationService } from 'app/entities/calculation/calculation.service';
import { Calculation } from 'app/shared/model/calculation.model';

describe('Component Tests', () => {
  describe('Calculation Management Component', () => {
    let comp: CalculationComponent;
    let fixture: ComponentFixture<CalculationComponent>;
    let service: CalculationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [CalculationComponent],
        providers: []
      })
        .overrideTemplate(CalculationComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CalculationComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CalculationService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Calculation(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.calculations[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
