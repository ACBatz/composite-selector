/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CompositeSelectorTestModule } from '../../../test.module';
import { EnvironmentalEffectComponent } from 'app/entities/environmental-effect/environmental-effect.component';
import { EnvironmentalEffectService } from 'app/entities/environmental-effect/environmental-effect.service';
import { EnvironmentalEffect } from 'app/shared/model/environmental-effect.model';

describe('Component Tests', () => {
  describe('EnvironmentalEffect Management Component', () => {
    let comp: EnvironmentalEffectComponent;
    let fixture: ComponentFixture<EnvironmentalEffectComponent>;
    let service: EnvironmentalEffectService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CompositeSelectorTestModule],
        declarations: [EnvironmentalEffectComponent],
        providers: []
      })
        .overrideTemplate(EnvironmentalEffectComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EnvironmentalEffectComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(EnvironmentalEffectService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new EnvironmentalEffect(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.environmentalEffects[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
