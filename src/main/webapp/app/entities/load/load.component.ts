import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ILoad } from 'app/shared/model/load.model';
import { AccountService } from 'app/core';
import { LoadService } from './load.service';

@Component({
  selector: 'jhi-load',
  templateUrl: './load.component.html'
})
export class LoadComponent implements OnInit, OnDestroy {
  loads: ILoad[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected loadService: LoadService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.loadService
      .query()
      .pipe(
        filter((res: HttpResponse<ILoad[]>) => res.ok),
        map((res: HttpResponse<ILoad[]>) => res.body)
      )
      .subscribe(
        (res: ILoad[]) => {
          this.loads = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInLoads();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ILoad) {
    return item.id;
  }

  registerChangeInLoads() {
    this.eventSubscriber = this.eventManager.subscribe('loadListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
