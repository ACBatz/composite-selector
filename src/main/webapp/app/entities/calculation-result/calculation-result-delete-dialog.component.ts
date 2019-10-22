import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICalculationResult } from 'app/shared/model/calculation-result.model';
import { CalculationResultService } from './calculation-result.service';

@Component({
  selector: 'jhi-calculation-result-delete-dialog',
  templateUrl: './calculation-result-delete-dialog.component.html'
})
export class CalculationResultDeleteDialogComponent {
  calculationResult: ICalculationResult;

  constructor(
    protected calculationResultService: CalculationResultService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.calculationResultService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'calculationResultListModification',
        content: 'Deleted an calculationResult'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-calculation-result-delete-popup',
  template: ''
})
export class CalculationResultDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ calculationResult }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(CalculationResultDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.calculationResult = calculationResult;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/calculation-result', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/calculation-result', { outlets: { popup: null } }]);
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
