import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IVerb, Verb } from 'app/shared/model/verb.model';
import { VerbService } from './verb.service';
import { ICalculation } from 'app/shared/model/calculation.model';
import { CalculationService } from 'app/entities/calculation';

@Component({
  selector: 'jhi-verb-update',
  templateUrl: './verb-update.component.html'
})
export class VerbUpdateComponent implements OnInit {
  isSaving: boolean;

  calculations: ICalculation[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    calculations: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected verbService: VerbService,
    protected calculationService: CalculationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ verb }) => {
      this.updateForm(verb);
    });
    this.calculationService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICalculation[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICalculation[]>) => response.body)
      )
      .subscribe((res: ICalculation[]) => (this.calculations = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(verb: IVerb) {
    this.editForm.patchValue({
      id: verb.id,
      name: verb.name,
      calculations: verb.calculations
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const verb = this.createFromForm();
    if (verb.id !== undefined) {
      this.subscribeToSaveResponse(this.verbService.update(verb));
    } else {
      this.subscribeToSaveResponse(this.verbService.create(verb));
    }
  }

  private createFromForm(): IVerb {
    return {
      ...new Verb(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      calculations: this.editForm.get(['calculations']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVerb>>) {
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
