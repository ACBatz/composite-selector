import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICalculation } from 'app/shared/model/calculation.model';
import { CalculationService } from './calculation.service';

@Component({
  selector: 'jhi-calculation-delete-dialog',
  templateUrl: './calculation-delete-dialog.component.html'
})
export class CalculationDeleteDialogComponent {
  calculation: ICalculation;

  constructor(
    protected calculationService: CalculationService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.calculationService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'calculationListModification',
        content: 'Deleted an calculation'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-calculation-delete-popup',
  template: ''
})
export class CalculationDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ calculation }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(CalculationDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.calculation = calculation;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/calculation', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/calculation', { outlets: { popup: null } }]);
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
