import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ICalculationResult } from 'app/shared/model/calculation-result.model';
import { AccountService } from 'app/core';
import { CalculationResultService } from './calculation-result.service';

@Component({
  selector: 'jhi-calculation-result',
  templateUrl: './calculation-result.component.html'
})
export class CalculationResultComponent implements OnInit, OnDestroy {
  calculationResults: ICalculationResult[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected calculationResultService: CalculationResultService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.calculationResultService
      .query()
      .pipe(
        filter((res: HttpResponse<ICalculationResult[]>) => res.ok),
        map((res: HttpResponse<ICalculationResult[]>) => res.body)
      )
      .subscribe(
        (res: ICalculationResult[]) => {
          this.calculationResults = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInCalculationResults();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ICalculationResult) {
    return item.id;
  }

  registerChangeInCalculationResults() {
    this.eventSubscriber = this.eventManager.subscribe('calculationResultListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
