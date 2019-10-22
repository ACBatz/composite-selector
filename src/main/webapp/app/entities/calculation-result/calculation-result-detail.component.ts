import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICalculationResult } from 'app/shared/model/calculation-result.model';

@Component({
  selector: 'jhi-calculation-result-detail',
  templateUrl: './calculation-result-detail.component.html'
})
export class CalculationResultDetailComponent implements OnInit {
  calculationResult: ICalculationResult;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ calculationResult }) => {
      this.calculationResult = calculationResult;
    });
  }

  previousState() {
    window.history.back();
  }
}
