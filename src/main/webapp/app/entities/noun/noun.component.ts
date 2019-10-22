import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { INoun } from 'app/shared/model/noun.model';
import { AccountService } from 'app/core';
import { NounService } from './noun.service';

@Component({
  selector: 'jhi-noun',
  templateUrl: './noun.component.html'
})
export class NounComponent implements OnInit, OnDestroy {
  nouns: INoun[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected nounService: NounService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.nounService
      .query()
      .pipe(
        filter((res: HttpResponse<INoun[]>) => res.ok),
        map((res: HttpResponse<INoun[]>) => res.body)
      )
      .subscribe(
        (res: INoun[]) => {
          this.nouns = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInNouns();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: INoun) {
    return item.id;
  }

  registerChangeInNouns() {
    this.eventSubscriber = this.eventManager.subscribe('nounListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
