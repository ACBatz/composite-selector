/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CompositeSelectorTestModule } from '../../../test.module';
import { LimitDetailComponent } from 'app/entities/limit/limit-detail.component';
import { Limit } from 'app/shared/model/limit.model';

describe('Component Tests', () => {
  describe('Limit Management Detail Component', () => {
    let comp: LimitDetailComponent;
    let fixture: ComponentFixture<LimitDetailComponent>;
    const route = ({ data: of({ limit: new Limit(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [LimitDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(LimitDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LimitDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.limit).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
