/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CompositeSelectorTestModule } from '../../../test.module';
import { EnvironmentalEffectDetailComponent } from 'app/entities/environmental-effect/environmental-effect-detail.component';
import { EnvironmentalEffect } from 'app/shared/model/environmental-effect.model';

describe('Component Tests', () => {
  describe('EnvironmentalEffect Management Detail Component', () => {
    let comp: EnvironmentalEffectDetailComponent;
    let fixture: ComponentFixture<EnvironmentalEffectDetailComponent>;
    const route = ({ data: of({ environmentalEffect: new EnvironmentalEffect(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [EnvironmentalEffectDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(EnvironmentalEffectDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EnvironmentalEffectDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.environmentalEffect).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
