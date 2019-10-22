import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IWeightingFactor, WeightingFactor } from 'app/shared/model/weighting-factor.model';
import { WeightingFactorService } from './weighting-factor.service';
import { ICalculation } from 'app/shared/model/calculation.model';
import { CalculationService } from 'app/entities/calculation';

@Component({
  selector: 'jhi-weighting-factor-update',
  templateUrl: './weighting-factor-update.component.html'
})
export class WeightingFactorUpdateComponent implements OnInit {
  isSaving: boolean;

  calculations: ICalculation[];

  editForm = this.fb.group({
    id: [],
    value: [null, [Validators.min(0), Validators.max(10)]],
    calculation: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected weightingFactorService: WeightingFactorService,
    protected calculationService: CalculationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ weightingFactor }) => {
      this.updateForm(weightingFactor);
    });
    this.calculationService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICalculation[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICalculation[]>) => response.body)
      )
      .subscribe((res: ICalculation[]) => (this.calculations = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(weightingFactor: IWeightingFactor) {
    this.editForm.patchValue({
      id: weightingFactor.id,
      value: weightingFactor.value,
      calculation: weightingFactor.calculation
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const weightingFactor = this.createFromForm();
    if (weightingFactor.id !== undefined) {
      this.subscribeToSaveResponse(this.weightingFactorService.update(weightingFactor));
    } else {
      this.subscribeToSaveResponse(this.weightingFactorService.create(weightingFactor));
    }
  }

  private createFromForm(): IWeightingFactor {
    return {
      ...new WeightingFactor(),
      id: this.editForm.get(['id']).value,
      value: this.editForm.get(['value']).value,
      calculation: this.editForm.get(['calculation']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWeightingFactor>>) {
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
}
