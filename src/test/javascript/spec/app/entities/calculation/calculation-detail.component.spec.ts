/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CompositeSelectorTestModule } from '../../../test.module';
import { CalculationDetailComponent } from 'app/entities/calculation/calculation-detail.component';
import { Calculation } from 'app/shared/model/calculation.model';

describe('Component Tests', () => {
  describe('Calculation Management Detail Component', () => {
    let comp: CalculationDetailComponent;
    let fixture: ComponentFixture<CalculationDetailComponent>;
    const route = ({ data: of({ calculation: new Calculation(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [CalculationDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(CalculationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CalculationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.calculation).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
