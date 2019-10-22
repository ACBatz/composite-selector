import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ILoadShear } from 'app/shared/model/load-shear.model';
import { AccountService } from 'app/core';
import { LoadShearService } from './load-shear.service';

@Component({
  selector: 'jhi-load-shear',
  templateUrl: './load-shear.component.html'
})
export class LoadShearComponent implements OnInit, OnDestroy {
  loadShears: ILoadShear[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected loadShearService: LoadShearService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.loadShearService
      .query()
      .pipe(
        filter((res: HttpResponse<ILoadShear[]>) => res.ok),
        map((res: HttpResponse<ILoadShear[]>) => res.body)
      )
      .subscribe(
        (res: ILoadShear[]) => {
          this.loadShears = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInLoadShears();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ILoadShear) {
    return item.id;
  }

  registerChangeInLoadShears() {
    this.eventSubscriber = this.eventManager.subscribe('loadShearListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
