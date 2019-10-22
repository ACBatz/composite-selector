import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ILoadShear, LoadShear } from 'app/shared/model/load-shear.model';
import { LoadShearService } from './load-shear.service';
import { ILoad } from 'app/shared/model/load.model';
import { LoadService } from 'app/entities/load';

@Component({
  selector: 'jhi-load-shear-update',
  templateUrl: './load-shear-update.component.html'
})
export class LoadShearUpdateComponent implements OnInit {
  isSaving: boolean;

  loads: ILoad[];

  editForm = this.fb.group({
    id: [],
    loadShearEnum: [null, [Validators.required]],
    loads: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected loadShearService: LoadShearService,
    protected loadService: LoadService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ loadShear }) => {
      this.updateForm(loadShear);
    });
    this.loadService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ILoad[]>) => mayBeOk.ok),
        map((response: HttpResponse<ILoad[]>) => response.body)
      )
      .subscribe((res: ILoad[]) => (this.loads = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(loadShear: ILoadShear) {
    this.editForm.patchValue({
      id: loadShear.id,
      loadShearEnum: loadShear.loadShearEnum,
      loads: loadShear.loads
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const loadShear = this.createFromForm();
    if (loadShear.id !== undefined) {
      this.subscribeToSaveResponse(this.loadShearService.update(loadShear));
    } else {
      this.subscribeToSaveResponse(this.loadShearService.create(loadShear));
    }
  }

  private createFromForm(): ILoadShear {
    return {
      ...new LoadShear(),
      id: this.editForm.get(['id']).value,
      loadShearEnum: this.editForm.get(['loadShearEnum']).value,
      loads: this.editForm.get(['loads']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILoadShear>>) {
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

  trackLoadById(index: number, item: ILoad) {
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
