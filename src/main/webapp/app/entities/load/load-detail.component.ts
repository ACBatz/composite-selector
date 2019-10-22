import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILoad } from 'app/shared/model/load.model';

@Component({
  selector: 'jhi-load-detail',
  templateUrl: './load-detail.component.html'
})
export class LoadDetailComponent implements OnInit {
  load: ILoad;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ load }) => {
      this.load = load;
    });
  }

  previousState() {
    window.history.back();
  }
}
