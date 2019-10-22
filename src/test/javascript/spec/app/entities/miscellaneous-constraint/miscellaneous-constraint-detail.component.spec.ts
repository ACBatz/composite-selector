/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CompositeSelectorTestModule } from '../../../test.module';
import { MiscellaneousConstraintDetailComponent } from 'app/entities/miscellaneous-constraint/miscellaneous-constraint-detail.component';
import { MiscellaneousConstraint } from 'app/shared/model/miscellaneous-constraint.model';

describe('Component Tests', () => {
  describe('MiscellaneousConstraint Management Detail Component', () => {
    let comp: MiscellaneousConstraintDetailComponent;
    let fixture: ComponentFixture<MiscellaneousConstraintDetailComponent>;
    const route = ({ data: of({ miscellaneousConstraint: new MiscellaneousConstraint(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [MiscellaneousConstraintDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(MiscellaneousConstraintDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MiscellaneousConstraintDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.miscellaneousConstraint).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
