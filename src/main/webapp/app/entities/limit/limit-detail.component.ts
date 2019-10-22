import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILimit } from 'app/shared/model/limit.model';

@Component({
  selector: 'jhi-limit-detail',
  templateUrl: './limit-detail.component.html'
})
export class LimitDetailComponent implements OnInit {
  limit: ILimit;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ limit }) => {
      this.limit = limit;
    });
  }

  previousState() {
    window.history.back();
  }
}
