import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IEnvironmentalEffect } from 'app/shared/model/environmental-effect.model';
import { AccountService } from 'app/core';
import { EnvironmentalEffectService } from './environmental-effect.service';

@Component({
  selector: 'jhi-environmental-effect',
  templateUrl: './environmental-effect.component.html'
})
export class EnvironmentalEffectComponent implements OnInit, OnDestroy {
  environmentalEffects: IEnvironmentalEffect[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected environmentalEffectService: EnvironmentalEffectService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.environmentalEffectService
      .query()
      .pipe(
        filter((res: HttpResponse<IEnvironmentalEffect[]>) => res.ok),
        map((res: HttpResponse<IEnvironmentalEffect[]>) => res.body)
      )
      .subscribe(
        (res: IEnvironmentalEffect[]) => {
          this.environmentalEffects = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInEnvironmentalEffects();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IEnvironmentalEffect) {
    return item.id;
  }

  registerChangeInEnvironmentalEffects() {
    this.eventSubscriber = this.eventManager.subscribe('environmentalEffectListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
