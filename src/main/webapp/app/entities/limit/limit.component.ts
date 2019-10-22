import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ILimit } from 'app/shared/model/limit.model';
import { AccountService } from 'app/core';
import { LimitService } from './limit.service';

@Component({
  selector: 'jhi-limit',
  templateUrl: './limit.component.html'
})
export class LimitComponent implements OnInit, OnDestroy {
  limits: ILimit[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected limitService: LimitService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.limitService
      .query()
      .pipe(
        filter((res: HttpResponse<ILimit[]>) => res.ok),
        map((res: HttpResponse<ILimit[]>) => res.body)
      )
      .subscribe(
        (res: ILimit[]) => {
          this.limits = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInLimits();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ILimit) {
    return item.id;
  }

  registerChangeInLimits() {
    this.eventSubscriber = this.eventManager.subscribe('limitListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
