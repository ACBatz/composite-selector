import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IUnitOfMeasure, UnitOfMeasure } from 'app/shared/model/unit-of-measure.model';
import { UnitOfMeasureService } from './unit-of-measure.service';
import { ILimit } from 'app/shared/model/limit.model';
import { LimitService } from 'app/entities/limit';

@Component({
  selector: 'jhi-unit-of-measure-update',
  templateUrl: './unit-of-measure-update.component.html'
})
export class UnitOfMeasureUpdateComponent implements OnInit {
  isSaving: boolean;

  limits: ILimit[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    limits: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected unitOfMeasureService: UnitOfMeasureService,
    protected limitService: LimitService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ unitOfMeasure }) => {
      this.updateForm(unitOfMeasure);
    });
    this.limitService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ILimit[]>) => mayBeOk.ok),
        map((response: HttpResponse<ILimit[]>) => response.body)
      )
      .subscribe((res: ILimit[]) => (this.limits = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(unitOfMeasure: IUnitOfMeasure) {
    this.editForm.patchValue({
      id: unitOfMeasure.id,
      name: unitOfMeasure.name,
      limits: unitOfMeasure.limits
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const unitOfMeasure = this.createFromForm();
    if (unitOfMeasure.id !== undefined) {
      this.subscribeToSaveResponse(this.unitOfMeasureService.update(unitOfMeasure));
    } else {
      this.subscribeToSaveResponse(this.unitOfMeasureService.create(unitOfMeasure));
    }
  }

  private createFromForm(): IUnitOfMeasure {
    return {
      ...new UnitOfMeasure(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      limits: this.editForm.get(['limits']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUnitOfMeasure>>) {
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

  trackLimitById(index: number, item: ILimit) {
    return item.id;
  }
}
