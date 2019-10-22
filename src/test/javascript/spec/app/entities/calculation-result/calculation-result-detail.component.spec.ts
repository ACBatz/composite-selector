/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CompositeSelectorTestModule } from '../../../test.module';
import { CalculationResultDetailComponent } from 'app/entities/calculation-result/calculation-result-detail.component';
import { CalculationResult } from 'app/shared/model/calculation-result.model';

describe('Component Tests', () => {
  describe('CalculationResult Management Detail Component', () => {
    let comp: CalculationResultDetailComponent;
    let fixture: ComponentFixture<CalculationResultDetailComponent>;
    const route = ({ data: of({ calculationResult: new CalculationResult(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [CalculationResultDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(CalculationResultDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CalculationResultDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.calculationResult).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
