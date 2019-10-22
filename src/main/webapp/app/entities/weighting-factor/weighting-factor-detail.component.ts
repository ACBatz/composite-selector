import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWeightingFactor } from 'app/shared/model/weighting-factor.model';

@Component({
  selector: 'jhi-weighting-factor-detail',
  templateUrl: './weighting-factor-detail.component.html'
})
export class WeightingFactorDetailComponent implements OnInit {
  weightingFactor: IWeightingFactor;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ weightingFactor }) => {
      this.weightingFactor = weightingFactor;
    });
  }

  previousState() {
    window.history.back();
  }
}
