import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ICalculation } from 'app/shared/model/calculation.model';
import { AccountService } from 'app/core';
import { CalculationService } from './calculation.service';

@Component({
  selector: 'jhi-calculation',
  templateUrl: './calculation.component.html'
})
export class CalculationComponent implements OnInit, OnDestroy {
  calculations: ICalculation[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected calculationService: CalculationService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.calculationService
      .query()
      .pipe(
        filter((res: HttpResponse<ICalculation[]>) => res.ok),
        map((res: HttpResponse<ICalculation[]>) => res.body)
      )
      .subscribe(
        (res: ICalculation[]) => {
          this.calculations = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInCalculations();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ICalculation) {
    return item.id;
  }

  registerChangeInCalculations() {
    this.eventSubscriber = this.eventManager.subscribe('calculationListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
