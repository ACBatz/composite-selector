/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CompositeSelectorTestModule } from '../../../test.module';
import { LimitComponent } from 'app/entities/limit/limit.component';
import { LimitService } from 'app/entities/limit/limit.service';
import { Limit } from 'app/shared/model/limit.model';

describe('Component Tests', () => {
  describe('Limit Management Component', () => {
    let comp: LimitComponent;
    let fixture: ComponentFixture<LimitComponent>;
    let service: LimitService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [LimitComponent],
        providers: []
      })
        .overrideTemplate(LimitComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LimitComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LimitService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Limit(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.limits[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
