import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ICalculationResult, CalculationResult } from 'app/shared/model/calculation-result.model';
import { CalculationResultService } from './calculation-result.service';
import { IComposite } from 'app/shared/model/composite.model';
import { CompositeService } from 'app/entities/composite';
import { ICalculation } from 'app/shared/model/calculation.model';
import { CalculationService } from 'app/entities/calculation';

@Component({
  selector: 'jhi-calculation-result-update',
  templateUrl: './calculation-result-update.component.html'
})
export class CalculationResultUpdateComponent implements OnInit {
  isSaving: boolean;

  composites: IComposite[];

  calculations: ICalculation[];

  editForm = this.fb.group({
    id: [],
    ratingFactor: [],
    composite: [],
    calculation: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected calculationResultService: CalculationResultService,
    protected compositeService: CompositeService,
    protected calculationService: CalculationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ calculationResult }) => {
      this.updateForm(calculationResult);
    });
    this.compositeService
      .query({ filter: 'result-is-null' })
      .pipe(
        filter((mayBeOk: HttpResponse<IComposite[]>) => mayBeOk.ok),
        map((response: HttpResponse<IComposite[]>) => response.body)
      )
      .subscribe(
        (res: IComposite[]) => {
          if (!this.editForm.get('composite').value || !this.editForm.get('composite').value.id) {
            this.composites = res;
          } else {
            this.compositeService
              .find(this.editForm.get('composite').value.id)
              .pipe(
                filter((subResMayBeOk: HttpResponse<IComposite>) => subResMayBeOk.ok),
                map((subResponse: HttpResponse<IComposite>) => subResponse.body)
              )
              .subscribe(
                (subRes: IComposite) => (this.composites = [subRes].concat(res)),
                (subRes: HttpErrorResponse) => this.onError(subRes.message)
              );
          }
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
    this.calculationService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICalculation[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICalculation[]>) => response.body)
      )
      .subscribe((res: ICalculation[]) => (this.calculations = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(calculationResult: ICalculationResult) {
    this.editForm.patchValue({
      id: calculationResult.id,
      ratingFactor: calculationResult.ratingFactor,
      composite: calculationResult.composite,
      calculation: calculationResult.calculation
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const calculationResult = this.createFromForm();
    if (calculationResult.id !== undefined) {
      this.subscribeToSaveResponse(this.calculationResultService.update(calculationResult));
    } else {
      this.subscribeToSaveResponse(this.calculationResultService.create(calculationResult));
    }
  }

  private createFromForm(): ICalculationResult {
    return {
      ...new CalculationResult(),
      id: this.editForm.get(['id']).value,
      ratingFactor: this.editForm.get(['ratingFactor']).value,
      composite: this.editForm.get(['composite']).value,
      calculation: this.editForm.get(['calculation']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICalculationResult>>) {
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

  trackCompositeById(index: number, item: IComposite) {
    return item.id;
  }

  trackCalculationById(index: number, item: ICalculation) {
    return item.id;
  }
}
