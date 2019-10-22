import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUnitOfMeasure } from 'app/shared/model/unit-of-measure.model';

@Component({
  selector: 'jhi-unit-of-measure-detail',
  templateUrl: './unit-of-measure-detail.component.html'
})
export class UnitOfMeasureDetailComponent implements OnInit {
  unitOfMeasure: IUnitOfMeasure;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ unitOfMeasure }) => {
      this.unitOfMeasure = unitOfMeasure;
    });
  }

  previousState() {
    window.history.back();
  }
}
