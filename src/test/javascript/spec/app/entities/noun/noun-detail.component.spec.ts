/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CompositeSelectorTestModule } from '../../../test.module';
import { NounDetailComponent } from 'app/entities/noun/noun-detail.component';
import { Noun } from 'app/shared/model/noun.model';

describe('Component Tests', () => {
  describe('Noun Management Detail Component', () => {
    let comp: NounDetailComponent;
    let fixture: ComponentFixture<NounDetailComponent>;
    const route = ({ data: of({ noun: new Noun(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [NounDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(NounDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(NounDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.noun).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
