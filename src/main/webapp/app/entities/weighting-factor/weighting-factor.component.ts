import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IWeightingFactor } from 'app/shared/model/weighting-factor.model';
import { AccountService } from 'app/core';
import { WeightingFactorService } from './weighting-factor.service';

@Component({
  selector: 'jhi-weighting-factor',
  templateUrl: './weighting-factor.component.html'
})
export class WeightingFactorComponent implements OnInit, OnDestroy {
  weightingFactors: IWeightingFactor[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected weightingFactorService: WeightingFactorService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.weightingFactorService
      .query()
      .pipe(
        filter((res: HttpResponse<IWeightingFactor[]>) => res.ok),
        map((res: HttpResponse<IWeightingFactor[]>) => res.body)
      )
      .subscribe(
        (res: IWeightingFactor[]) => {
          this.weightingFactors = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInWeightingFactors();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IWeightingFactor) {
    return item.id;
  }

  registerChangeInWeightingFactors() {
    this.eventSubscriber = this.eventManager.subscribe('weightingFactorListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
