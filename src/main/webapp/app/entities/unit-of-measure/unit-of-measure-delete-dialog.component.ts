import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IUnitOfMeasure } from 'app/shared/model/unit-of-measure.model';
import { UnitOfMeasureService } from './unit-of-measure.service';

@Component({
  selector: 'jhi-unit-of-measure-delete-dialog',
  templateUrl: './unit-of-measure-delete-dialog.component.html'
})
export class UnitOfMeasureDeleteDialogComponent {
  unitOfMeasure: IUnitOfMeasure;

  constructor(
    protected unitOfMeasureService: UnitOfMeasureService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.unitOfMeasureService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'unitOfMeasureListModification',
        content: 'Deleted an unitOfMeasure'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-unit-of-measure-delete-popup',
  template: ''
})
export class UnitOfMeasureDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ unitOfMeasure }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(UnitOfMeasureDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.unitOfMeasure = unitOfMeasure;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/unit-of-measure', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/unit-of-measure', { outlets: { popup: null } }]);
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
