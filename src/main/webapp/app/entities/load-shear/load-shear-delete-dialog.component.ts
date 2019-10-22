import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ILoadShear } from 'app/shared/model/load-shear.model';
import { LoadShearService } from './load-shear.service';

@Component({
  selector: 'jhi-load-shear-delete-dialog',
  templateUrl: './load-shear-delete-dialog.component.html'
})
export class LoadShearDeleteDialogComponent {
  loadShear: ILoadShear;

  constructor(protected loadShearService: LoadShearService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.loadShearService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'loadShearListModification',
        content: 'Deleted an loadShear'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-load-shear-delete-popup',
  template: ''
})
export class LoadShearDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ loadShear }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(LoadShearDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.loadShear = loadShear;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/load-shear', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/load-shear', { outlets: { popup: null } }]);
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
