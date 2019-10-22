/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CompositeSelectorTestModule } from '../../../test.module';
import { CompositeDetailComponent } from 'app/entities/composite/composite-detail.component';
import { Composite } from 'app/shared/model/composite.model';

describe('Component Tests', () => {
  describe('Composite Management Detail Component', () => {
    let comp: CompositeDetailComponent;
    let fixture: ComponentFixture<CompositeDetailComponent>;
    const route = ({ data: of({ composite: new Composite(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [CompositeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(CompositeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CompositeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.composite).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
