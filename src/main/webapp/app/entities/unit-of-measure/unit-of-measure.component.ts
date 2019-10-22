import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IUnitOfMeasure } from 'app/shared/model/unit-of-measure.model';
import { AccountService } from 'app/core';
import { UnitOfMeasureService } from './unit-of-measure.service';

@Component({
  selector: 'jhi-unit-of-measure',
  templateUrl: './unit-of-measure.component.html'
})
export class UnitOfMeasureComponent implements OnInit, OnDestroy {
  unitOfMeasures: IUnitOfMeasure[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected unitOfMeasureService: UnitOfMeasureService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.unitOfMeasureService
      .query()
      .pipe(
        filter((res: HttpResponse<IUnitOfMeasure[]>) => res.ok),
        map((res: HttpResponse<IUnitOfMeasure[]>) => res.body)
      )
      .subscribe(
        (res: IUnitOfMeasure[]) => {
          this.unitOfMeasures = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInUnitOfMeasures();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IUnitOfMeasure) {
    return item.id;
  }

  registerChangeInUnitOfMeasures() {
    this.eventSubscriber = this.eventManager.subscribe('unitOfMeasureListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
