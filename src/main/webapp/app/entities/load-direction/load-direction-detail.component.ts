import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILoadDirection } from 'app/shared/model/load-direction.model';

@Component({
  selector: 'jhi-load-direction-detail',
  templateUrl: './load-direction-detail.component.html'
})
export class LoadDirectionDetailComponent implements OnInit {
  loadDirection: ILoadDirection;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ loadDirection }) => {
      this.loadDirection = loadDirection;
    });
  }

  previousState() {
    window.history.back();
  }
}
