import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICalculation } from 'app/shared/model/calculation.model';

@Component({
  selector: 'jhi-calculation-detail',
  templateUrl: './calculation-detail.component.html'
})
export class CalculationDetailComponent implements OnInit {
  calculation: ICalculation;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ calculation }) => {
      this.calculation = calculation;
    });
  }

  previousState() {
    window.history.back();
  }
}
