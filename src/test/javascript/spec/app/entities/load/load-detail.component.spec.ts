/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CompositeSelectorTestModule } from '../../../test.module';
import { LoadDetailComponent } from 'app/entities/load/load-detail.component';
import { Load } from 'app/shared/model/load.model';

describe('Component Tests', () => {
  describe('Load Management Detail Component', () => {
    let comp: LoadDetailComponent;
    let fixture: ComponentFixture<LoadDetailComponent>;
    const route = ({ data: of({ load: new Load(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [LoadDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(LoadDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LoadDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.load).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
