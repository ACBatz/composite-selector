/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CompositeSelectorTestModule } from '../../../test.module';
import { LoadDirectionDetailComponent } from 'app/entities/load-direction/load-direction-detail.component';
import { LoadDirection } from 'app/shared/model/load-direction.model';

describe('Component Tests', () => {
  describe('LoadDirection Management Detail Component', () => {
    let comp: LoadDirectionDetailComponent;
    let fixture: ComponentFixture<LoadDirectionDetailComponent>;
    const route = ({ data: of({ loadDirection: new LoadDirection(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [LoadDirectionDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(LoadDirectionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LoadDirectionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.loadDirection).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
