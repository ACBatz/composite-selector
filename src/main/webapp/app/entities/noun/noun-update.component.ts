import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { INoun, Noun } from 'app/shared/model/noun.model';
import { NounService } from './noun.service';
import { ICalculation } from 'app/shared/model/calculation.model';
import { CalculationService } from 'app/entities/calculation';

@Component({
  selector: 'jhi-noun-update',
  templateUrl: './noun-update.component.html'
})
export class NounUpdateComponent implements OnInit {
  isSaving: boolean;

  calculations: ICalculation[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    calculations: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected nounService: NounService,
    protected calculationService: CalculationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ noun }) => {
      this.updateForm(noun);
    });
    this.calculationService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICalculation[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICalculation[]>) => response.body)
      )
      .subscribe((res: ICalculation[]) => (this.calculations = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(noun: INoun) {
    this.editForm.patchValue({
      id: noun.id,
      name: noun.name,
      calculations: noun.calculations
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const noun = this.createFromForm();
    if (noun.id !== undefined) {
      this.subscribeToSaveResponse(this.nounService.update(noun));
    } else {
      this.subscribeToSaveResponse(this.nounService.create(noun));
    }
  }

  private createFromForm(): INoun {
    return {
      ...new Noun(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      calculations: this.editForm.get(['calculations']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INoun>>) {
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
