/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CompositeSelectorTestModule } from '../../../test.module';
import { LoadShearDetailComponent } from 'app/entities/load-shear/load-shear-detail.component';
import { LoadShear } from 'app/shared/model/load-shear.model';

describe('Component Tests', () => {
  describe('LoadShear Management Detail Component', () => {
    let comp: LoadShearDetailComponent;
    let fixture: ComponentFixture<LoadShearDetailComponent>;
    const route = ({ data: of({ loadShear: new LoadShear(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [LoadShearDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(LoadShearDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LoadShearDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.loadShear).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
