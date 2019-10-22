import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IComposite } from 'app/shared/model/composite.model';
import { CompositeService } from './composite.service';

@Component({
  selector: 'jhi-composite-delete-dialog',
  templateUrl: './composite-delete-dialog.component.html'
})
export class CompositeDeleteDialogComponent {
  composite: IComposite;

  constructor(protected compositeService: CompositeService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.compositeService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'compositeListModification',
        content: 'Deleted an composite'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-composite-delete-popup',
  template: ''
})
export class CompositeDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ composite }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(CompositeDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.composite = composite;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/composite', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/composite', { outlets: { popup: null } }]);
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
