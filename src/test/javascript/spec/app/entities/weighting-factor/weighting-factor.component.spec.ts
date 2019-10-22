/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CompositeSelectorTestModule } from '../../../test.module';
import { WeightingFactorComponent } from 'app/entities/weighting-factor/weighting-factor.component';
import { WeightingFactorService } from 'app/entities/weighting-factor/weighting-factor.service';
import { WeightingFactor } from 'app/shared/model/weighting-factor.model';

describe('Component Tests', () => {
  describe('WeightingFactor Management Component', () => {
    let comp: WeightingFactorComponent;
    let fixture: ComponentFixture<WeightingFactorComponent>;
    let service: WeightingFactorService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [WeightingFactorComponent],
        providers: []
      })
        .overrideTemplate(WeightingFactorComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WeightingFactorComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(WeightingFactorService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new WeightingFactor(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.weightingFactors[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
