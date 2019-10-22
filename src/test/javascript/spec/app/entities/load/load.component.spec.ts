/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CompositeSelectorTestModule } from '../../../test.module';
import { LoadComponent } from 'app/entities/load/load.component';
import { LoadService } from 'app/entities/load/load.service';
import { Load } from 'app/shared/model/load.model';

describe('Component Tests', () => {
  describe('Load Management Component', () => {
    let comp: LoadComponent;
    let fixture: ComponentFixture<LoadComponent>;
    let service: LoadService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [LoadComponent],
        providers: []
      })
        .overrideTemplate(LoadComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LoadComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LoadService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Load(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.loads[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
