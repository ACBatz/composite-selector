/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CompositeSelectorTestModule } from '../../../test.module';
import { LoadShearComponent } from 'app/entities/load-shear/load-shear.component';
import { LoadShearService } from 'app/entities/load-shear/load-shear.service';
import { LoadShear } from 'app/shared/model/load-shear.model';

describe('Component Tests', () => {
  describe('LoadShear Management Component', () => {
    let comp: LoadShearComponent;
    let fixture: ComponentFixture<LoadShearComponent>;
    let service: LoadShearService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [LoadShearComponent],
        providers: []
      })
        .overrideTemplate(LoadShearComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LoadShearComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LoadShearService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new LoadShear(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.loadShears[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
