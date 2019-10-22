import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IProperty, Property } from 'app/shared/model/property.model';
import { PropertyService } from './property.service';
import { IWeightingFactor } from 'app/shared/model/weighting-factor.model';
import { WeightingFactorService } from 'app/entities/weighting-factor';
import { ILimit } from 'app/shared/model/limit.model';
import { LimitService } from 'app/entities/limit';
import { IMiscellaneousConstraint } from 'app/shared/model/miscellaneous-constraint.model';
import { MiscellaneousConstraintService } from 'app/entities/miscellaneous-constraint';
import { IComposite } from 'app/shared/model/composite.model';
import { CompositeService } from 'app/entities/composite';

@Component({
  selector: 'jhi-property-update',
  templateUrl: './property-update.component.html'
})
export class PropertyUpdateComponent implements OnInit {
  isSaving: boolean;

  weightingfactors: IWeightingFactor[];

  limits: ILimit[];

  miscellaneousconstraints: IMiscellaneousConstraint[];

  composites: IComposite[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    weightingFactors: [],
    limits: [],
    miscellaneousConstraint: [],
    composites: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected propertyService: PropertyService,
    protected weightingFactorService: WeightingFactorService,
    protected limitService: LimitService,
    protected miscellaneousConstraintService: MiscellaneousConstraintService,
    protected compositeService: CompositeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ property }) => {
      this.updateForm(property);
    });
    this.weightingFactorService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IWeightingFactor[]>) => mayBeOk.ok),
        map((response: HttpResponse<IWeightingFactor[]>) => response.body)
      )
      .subscribe((res: IWeightingFactor[]) => (this.weightingfactors = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.limitService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ILimit[]>) => mayBeOk.ok),
        map((response: HttpResponse<ILimit[]>) => response.body)
      )
      .subscribe((res: ILimit[]) => (this.limits = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.miscellaneousConstraintService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IMiscellaneousConstraint[]>) => mayBeOk.ok),
        map((response: HttpResponse<IMiscellaneousConstraint[]>) => response.body)
      )
      .subscribe(
        (res: IMiscellaneousConstraint[]) => (this.miscellaneousconstraints = res),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
    this.compositeService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IComposite[]>) => mayBeOk.ok),
        map((response: HttpResponse<IComposite[]>) => response.body)
      )
      .subscribe((res: IComposite[]) => (this.composites = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(property: IProperty) {
    this.editForm.patchValue({
      id: property.id,
      name: property.name,
      weightingFactors: property.weightingFactors,
      limits: property.limits,
      miscellaneousConstraint: property.miscellaneousConstraint,
      composites: property.composites
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const property = this.createFromForm();
    if (property.id !== undefined) {
      this.subscribeToSaveResponse(this.propertyService.update(property));
    } else {
      this.subscribeToSaveResponse(this.propertyService.create(property));
    }
  }

  private createFromForm(): IProperty {
    return {
      ...new Property(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      weightingFactors: this.editForm.get(['weightingFactors']).value,
      limits: this.editForm.get(['limits']).value,
      miscellaneousConstraint: this.editForm.get(['miscellaneousConstraint']).value,
      composites: this.editForm.get(['composites']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProperty>>) {
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

  trackWeightingFactorById(index: number, item: IWeightingFactor) {
    return item.id;
  }

  trackLimitById(index: number, item: ILimit) {
    return item.id;
  }

  trackMiscellaneousConstraintById(index: number, item: IMiscellaneousConstraint) {
    return item.id;
  }

  trackCompositeById(index: number, item: IComposite) {
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
