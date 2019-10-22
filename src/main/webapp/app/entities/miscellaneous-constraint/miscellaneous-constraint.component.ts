import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IMiscellaneousConstraint } from 'app/shared/model/miscellaneous-constraint.model';
import { AccountService } from 'app/core';
import { MiscellaneousConstraintService } from './miscellaneous-constraint.service';

@Component({
  selector: 'jhi-miscellaneous-constraint',
  templateUrl: './miscellaneous-constraint.component.html'
})
export class MiscellaneousConstraintComponent implements OnInit, OnDestroy {
  miscellaneousConstraints: IMiscellaneousConstraint[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected miscellaneousConstraintService: MiscellaneousConstraintService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.miscellaneousConstraintService
      .query()
      .pipe(
        filter((res: HttpResponse<IMiscellaneousConstraint[]>) => res.ok),
        map((res: HttpResponse<IMiscellaneousConstraint[]>) => res.body)
      )
      .subscribe(
        (res: IMiscellaneousConstraint[]) => {
          this.miscellaneousConstraints = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInMiscellaneousConstraints();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IMiscellaneousConstraint) {
    return item.id;
  }

  registerChangeInMiscellaneousConstraints() {
    this.eventSubscriber = this.eventManager.subscribe('miscellaneousConstraintListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
