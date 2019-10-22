/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CompositeSelectorTestModule } from '../../../test.module';
import { VerbDetailComponent } from 'app/entities/verb/verb-detail.component';
import { Verb } from 'app/shared/model/verb.model';

describe('Component Tests', () => {
  describe('Verb Management Detail Component', () => {
    let comp: VerbDetailComponent;
    let fixture: ComponentFixture<VerbDetailComponent>;
    const route = ({ data: of({ verb: new Verb(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [VerbDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(VerbDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(VerbDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.verb).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
