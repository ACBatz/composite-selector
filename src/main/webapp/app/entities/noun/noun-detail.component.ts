import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INoun } from 'app/shared/model/noun.model';

@Component({
  selector: 'jhi-noun-detail',
  templateUrl: './noun-detail.component.html'
})
export class NounDetailComponent implements OnInit {
  noun: INoun;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ noun }) => {
      this.noun = noun;
    });
  }

  previousState() {
    window.history.back();
  }
}
