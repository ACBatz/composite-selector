import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IEnvironmentalEffect, EnvironmentalEffect } from 'app/shared/model/environmental-effect.model';
import { EnvironmentalEffectService } from './environmental-effect.service';
import { ICalculation } from 'app/shared/model/calculation.model';
import { CalculationService } from 'app/entities/calculation';

@Component({
  selector: 'jhi-environmental-effect-update',
  templateUrl: './environmental-effect-update.component.html'
})
export class EnvironmentalEffectUpdateComponent implements OnInit {
  isSaving: boolean;

  calculations: ICalculation[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    calculations: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected environmentalEffectService: EnvironmentalEffectService,
    protected calculationService: CalculationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ environmentalEffect }) => {
      this.updateForm(environmentalEffect);
    });
    this.calculationService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICalculation[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICalculation[]>) => response.body)
      )
      .subscribe((res: ICalculation[]) => (this.calculations = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(environmentalEffect: IEnvironmentalEffect) {
    this.editForm.patchValue({
      id: environmentalEffect.id,
      name: environmentalEffect.name,
      calculations: environmentalEffect.calculations
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const environmentalEffect = this.createFromForm();
    if (environmentalEffect.id !== undefined) {
      this.subscribeToSaveResponse(this.environmentalEffectService.update(environmentalEffect));
    } else {
      this.subscribeToSaveResponse(this.environmentalEffectService.create(environmentalEffect));
    }
  }

  private createFromForm(): IEnvironmentalEffect {
    return {
      ...new EnvironmentalEffect(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      calculations: this.editForm.get(['calculations']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEnvironmentalEffect>>) {
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
