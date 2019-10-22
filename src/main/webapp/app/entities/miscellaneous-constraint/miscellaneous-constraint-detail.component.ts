import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMiscellaneousConstraint } from 'app/shared/model/miscellaneous-constraint.model';

@Component({
  selector: 'jhi-miscellaneous-constraint-detail',
  templateUrl: './miscellaneous-constraint-detail.component.html'
})
export class MiscellaneousConstraintDetailComponent implements OnInit {
  miscellaneousConstraint: IMiscellaneousConstraint;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ miscellaneousConstraint }) => {
      this.miscellaneousConstraint = miscellaneousConstraint;
    });
  }

  previousState() {
    window.history.back();
  }
}
