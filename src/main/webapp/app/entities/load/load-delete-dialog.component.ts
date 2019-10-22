import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ILoad } from 'app/shared/model/load.model';
import { LoadService } from './load.service';

@Component({
  selector: 'jhi-load-delete-dialog',
  templateUrl: './load-delete-dialog.component.html'
})
export class LoadDeleteDialogComponent {
  load: ILoad;

  constructor(protected loadService: LoadService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.loadService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'loadListModification',
        content: 'Deleted an load'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-load-delete-popup',
  template: ''
})
export class LoadDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ load }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(LoadDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.load = load;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/load', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/load', { outlets: { popup: null } }]);
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
