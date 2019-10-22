/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CompositeSelectorTestModule } from '../../../test.module';
import { VerbComponent } from 'app/entities/verb/verb.component';
import { VerbService } from 'app/entities/verb/verb.service';
import { Verb } from 'app/shared/model/verb.model';

describe('Component Tests', () => {
  describe('Verb Management Component', () => {
    let comp: VerbComponent;
    let fixture: ComponentFixture<VerbComponent>;
    let service: VerbService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [VerbComponent],
        providers: []
      })
        .overrideTemplate(VerbComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VerbComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(VerbService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Verb(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.verbs[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
