import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IWeightingFactor } from 'app/shared/model/weighting-factor.model';
import { WeightingFactorService } from './weighting-factor.service';

@Component({
  selector: 'jhi-weighting-factor-delete-dialog',
  templateUrl: './weighting-factor-delete-dialog.component.html'
})
export class WeightingFactorDeleteDialogComponent {
  weightingFactor: IWeightingFactor;

  constructor(
    protected weightingFactorService: WeightingFactorService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.weightingFactorService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'weightingFactorListModification',
        content: 'Deleted an weightingFactor'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-weighting-factor-delete-popup',
  template: ''
})
export class WeightingFactorDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ weightingFactor }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(WeightingFactorDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.weightingFactor = weightingFactor;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/weighting-factor', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/weighting-factor', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
