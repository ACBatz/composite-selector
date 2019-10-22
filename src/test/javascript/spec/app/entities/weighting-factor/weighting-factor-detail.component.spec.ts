/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CompositeSelectorTestModule } from '../../../test.module';
import { WeightingFactorDetailComponent } from 'app/entities/weighting-factor/weighting-factor-detail.component';
import { WeightingFactor } from 'app/shared/model/weighting-factor.model';

describe('Component Tests', () => {
  describe('WeightingFactor Management Detail Component', () => {
    let comp: WeightingFactorDetailComponent;
    let fixture: ComponentFixture<WeightingFactorDetailComponent>;
    const route = ({ data: of({ weightingFactor: new WeightingFactor(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [WeightingFactorDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(WeightingFactorDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(WeightingFactorDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.weightingFactor).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
