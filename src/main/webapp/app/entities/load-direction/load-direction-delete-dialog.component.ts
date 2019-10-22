import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ILoadDirection } from 'app/shared/model/load-direction.model';
import { LoadDirectionService } from './load-direction.service';

@Component({
  selector: 'jhi-load-direction-delete-dialog',
  templateUrl: './load-direction-delete-dialog.component.html'
})
export class LoadDirectionDeleteDialogComponent {
  loadDirection: ILoadDirection;

  constructor(
    protected loadDirectionService: LoadDirectionService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.loadDirectionService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'loadDirectionListModification',
        content: 'Deleted an loadDirection'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-load-direction-delete-popup',
  template: ''
})
export class LoadDirectionDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ loadDirection }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(LoadDirectionDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.loadDirection = loadDirection;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/load-direction', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/load-direction', { outlets: { popup: null } }]);
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
