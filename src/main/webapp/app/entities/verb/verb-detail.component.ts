import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVerb } from 'app/shared/model/verb.model';

@Component({
  selector: 'jhi-verb-detail',
  templateUrl: './verb-detail.component.html'
})
export class VerbDetailComponent implements OnInit {
  verb: IVerb;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ verb }) => {
      this.verb = verb;
    });
  }

  previousState() {
    window.history.back();
  }
}
