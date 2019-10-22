import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ILimit } from 'app/shared/model/limit.model';
import { LimitService } from './limit.service';

@Component({
  selector: 'jhi-limit-delete-dialog',
  templateUrl: './limit-delete-dialog.component.html'
})
export class LimitDeleteDialogComponent {
  limit: ILimit;

  constructor(protected limitService: LimitService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.limitService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'limitListModification',
        content: 'Deleted an limit'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-limit-delete-popup',
  template: ''
})
export class LimitDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ limit }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(LimitDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.limit = limit;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/limit', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/limit', { outlets: { popup: null } }]);
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
