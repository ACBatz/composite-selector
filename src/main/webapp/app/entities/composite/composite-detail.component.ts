import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IComposite } from 'app/shared/model/composite.model';

@Component({
  selector: 'jhi-composite-detail',
  templateUrl: './composite-detail.component.html'
})
export class CompositeDetailComponent implements OnInit {
  composite: IComposite;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ composite }) => {
      this.composite = composite;
    });
  }

  previousState() {
    window.history.back();
  }
}
