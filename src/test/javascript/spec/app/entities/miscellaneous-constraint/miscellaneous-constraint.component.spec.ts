/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CompositeSelectorTestModule } from '../../../test.module';
import { MiscellaneousConstraintComponent } from 'app/entities/miscellaneous-constraint/miscellaneous-constraint.component';
import { MiscellaneousConstraintService } from 'app/entities/miscellaneous-constraint/miscellaneous-constraint.service';
import { MiscellaneousConstraint } from 'app/shared/model/miscellaneous-constraint.model';

describe('Component Tests', () => {
  describe('MiscellaneousConstraint Management Component', () => {
    let comp: MiscellaneousConstraintComponent;
    let fixture: ComponentFixture<MiscellaneousConstraintComponent>;
    let service: MiscellaneousConstraintService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [MiscellaneousConstraintComponent],
        providers: []
      })
        .overrideTemplate(MiscellaneousConstraintComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MiscellaneousConstraintComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MiscellaneousConstraintService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new MiscellaneousConstraint(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.miscellaneousConstraints[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
