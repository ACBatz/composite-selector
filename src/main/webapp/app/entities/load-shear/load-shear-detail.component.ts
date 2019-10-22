import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILoadShear } from 'app/shared/model/load-shear.model';

@Component({
  selector: 'jhi-load-shear-detail',
  templateUrl: './load-shear-detail.component.html'
})
export class LoadShearDetailComponent implements OnInit {
  loadShear: ILoadShear;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ loadShear }) => {
      this.loadShear = loadShear;
    });
  }

  previousState() {
    window.history.back();
  }
}
