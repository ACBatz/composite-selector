import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEnvironmentalEffect } from 'app/shared/model/environmental-effect.model';

@Component({
  selector: 'jhi-environmental-effect-detail',
  templateUrl: './environmental-effect-detail.component.html'
})
export class EnvironmentalEffectDetailComponent implements OnInit {
  environmentalEffect: IEnvironmentalEffect;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ environmentalEffect }) => {
      this.environmentalEffect = environmentalEffect;
    });
  }

  previousState() {
    window.history.back();
  }
}
