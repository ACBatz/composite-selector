import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ILimit, Limit } from 'app/shared/model/limit.model';
import { LimitService } from './limit.service';
import { ICalculation } from 'app/shared/model/calculation.model';
import { CalculationService } from 'app/entities/calculation';

@Component({
  selector: 'jhi-limit-update',
  templateUrl: './limit-update.component.html'
})
export class LimitUpdateComponent implements OnInit {
  isSaving: boolean;

  calculations: ICalculation[];

  editForm = this.fb.group({
    id: [],
    maximum: [],
    value: [],
    calculations: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected limitService: LimitService,
    protected calculationService: CalculationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ limit }) => {
      this.updateForm(limit);
    });
    this.calculationService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICalculation[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICalculation[]>) => response.body)
      )
      .subscribe((res: ICalculation[]) => (this.calculations = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(limit: ILimit) {
    this.editForm.patchValue({
      id: limit.id,
      maximum: limit.maximum,
      value: limit.value,
      calculations: limit.calculations
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const limit = this.createFromForm();
    if (limit.id !== undefined) {
      this.subscribeToSaveResponse(this.limitService.update(limit));
    } else {
      this.subscribeToSaveResponse(this.limitService.create(limit));
    }
  }

  private createFromForm(): ILimit {
    return {
      ...new Limit(),
      id: this.editForm.get(['id']).value,
      maximum: this.editForm.get(['maximum']).value,
      value: this.editForm.get(['value']).value,
      calculations: this.editForm.get(['calculations']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILimit>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackCalculationById(index: number, item: ICalculation) {
    return item.id;
  }

  getSelected(selectedVals: Array<any>, option: any) {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
