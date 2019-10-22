/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CompositeSelectorTestModule } from '../../../test.module';
import { NounComponent } from 'app/entities/noun/noun.component';
import { NounService } from 'app/entities/noun/noun.service';
import { Noun } from 'app/shared/model/noun.model';

describe('Component Tests', () => {
  describe('Noun Management Component', () => {
    let comp: NounComponent;
    let fixture: ComponentFixture<NounComponent>;
    let service: NounService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [NounComponent],
        providers: []
      })
        .overrideTemplate(NounComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(NounComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(NounService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Noun(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.nouns[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
