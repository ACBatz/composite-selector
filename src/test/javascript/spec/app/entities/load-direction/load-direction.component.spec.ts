/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CompositeSelectorTestModule } from '../../../test.module';
import { LoadDirectionComponent } from 'app/entities/load-direction/load-direction.component';
import { LoadDirectionService } from 'app/entities/load-direction/load-direction.service';
import { LoadDirection } from 'app/shared/model/load-direction.model';

describe('Component Tests', () => {
  describe('LoadDirection Management Component', () => {
    let comp: LoadDirectionComponent;
    let fixture: ComponentFixture<LoadDirectionComponent>;
    let service: LoadDirectionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [LoadDirectionComponent],
        providers: []
      })
        .overrideTemplate(LoadDirectionComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LoadDirectionComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LoadDirectionService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new LoadDirection(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.loadDirections[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
