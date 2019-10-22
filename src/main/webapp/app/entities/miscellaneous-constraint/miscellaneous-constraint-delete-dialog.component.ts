import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMiscellaneousConstraint } from 'app/shared/model/miscellaneous-constraint.model';
import { MiscellaneousConstraintService } from './miscellaneous-constraint.service';

@Component({
  selector: 'jhi-miscellaneous-constraint-delete-dialog',
  templateUrl: './miscellaneous-constraint-delete-dialog.component.html'
})
export class MiscellaneousConstraintDeleteDialogComponent {
  miscellaneousConstraint: IMiscellaneousConstraint;

  constructor(
    protected miscellaneousConstraintService: MiscellaneousConstraintService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.miscellaneousConstraintService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'miscellaneousConstraintListModification',
        content: 'Deleted an miscellaneousConstraint'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-miscellaneous-constraint-delete-popup',
  template: ''
})
export class MiscellaneousConstraintDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ miscellaneousConstraint }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(MiscellaneousConstraintDeleteDialogComponent as Component, {
          size: 'lg',
          backdrop: 'static'
        });
        this.ngbModalRef.componentInstance.miscellaneousConstraint = miscellaneousConstraint;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/miscellaneous-constraint', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/miscellaneous-constraint', { outlets: { popup: null } }]);
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
