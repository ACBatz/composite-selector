import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IComposite } from 'app/shared/model/composite.model';
import { AccountService } from 'app/core';
import { CompositeService } from './composite.service';

@Component({
  selector: 'jhi-composite',
  templateUrl: './composite.component.html'
})
export class CompositeComponent implements OnInit, OnDestroy {
  composites: IComposite[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected compositeService: CompositeService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.compositeService
      .query()
      .pipe(
        filter((res: HttpResponse<IComposite[]>) => res.ok),
        map((res: HttpResponse<IComposite[]>) => res.body)
      )
      .subscribe(
        (res: IComposite[]) => {
          this.composites = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInComposites();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IComposite) {
    return item.id;
  }

  registerChangeInComposites() {
    this.eventSubscriber = this.eventManager.subscribe('compositeListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
