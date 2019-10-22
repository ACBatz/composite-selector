import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ILoadDirection, LoadDirection } from 'app/shared/model/load-direction.model';
import { LoadDirectionService } from './load-direction.service';
import { ILoad } from 'app/shared/model/load.model';
import { LoadService } from 'app/entities/load';

@Component({
  selector: 'jhi-load-direction-update',
  templateUrl: './load-direction-update.component.html'
})
export class LoadDirectionUpdateComponent implements OnInit {
  isSaving: boolean;

  loads: ILoad[];

  editForm = this.fb.group({
    id: [],
    loadDirectionEnum: [null, [Validators.required]],
    loads: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected loadDirectionService: LoadDirectionService,
    protected loadService: LoadService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ loadDirection }) => {
      this.updateForm(loadDirection);
    });
    this.loadService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ILoad[]>) => mayBeOk.ok),
        map((response: HttpResponse<ILoad[]>) => response.body)
      )
      .subscribe((res: ILoad[]) => (this.loads = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(loadDirection: ILoadDirection) {
    this.editForm.patchValue({
      id: loadDirection.id,
      loadDirectionEnum: loadDirection.loadDirectionEnum,
      loads: loadDirection.loads
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const loadDirection = this.createFromForm();
    if (loadDirection.id !== undefined) {
      this.subscribeToSaveResponse(this.loadDirectionService.update(loadDirection));
    } else {
      this.subscribeToSaveResponse(this.loadDirectionService.create(loadDirection));
    }
  }

  private createFromForm(): ILoadDirection {
    return {
      ...new LoadDirection(),
      id: this.editForm.get(['id']).value,
      loadDirectionEnum: this.editForm.get(['loadDirectionEnum']).value,
      loads: this.editForm.get(['loads']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILoadDirection>>) {
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
