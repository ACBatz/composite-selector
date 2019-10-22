import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IVerb } from 'app/shared/model/verb.model';
import { AccountService } from 'app/core';
import { VerbService } from './verb.service';

@Component({
  selector: 'jhi-verb',
  templateUrl: './verb.component.html'
})
export class VerbComponent implements OnInit, OnDestroy {
  verbs: IVerb[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected verbService: VerbService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.verbService
      .query()
      .pipe(
        filter((res: HttpResponse<IVerb[]>) => res.ok),
        map((res: HttpResponse<IVerb[]>) => res.body)
      )
      .subscribe(
        (res: IVerb[]) => {
          this.verbs = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInVerbs();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IVerb) {
    return item.id;
  }

  registerChangeInVerbs() {
    this.eventSubscriber = this.eventManager.subscribe('verbListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
