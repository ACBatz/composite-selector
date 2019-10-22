import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IVerb } from 'app/shared/model/verb.model';
import { VerbService } from './verb.service';

@Component({
  selector: 'jhi-verb-delete-dialog',
  templateUrl: './verb-delete-dialog.component.html'
})
export class VerbDeleteDialogComponent {
  verb: IVerb;

  constructor(protected verbService: VerbService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.verbService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'verbListModification',
        content: 'Deleted an verb'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-verb-delete-popup',
  template: ''
})
export class VerbDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ verb }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(VerbDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.verb = verb;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/verb', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/verb', { outlets: { popup: null } }]);
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
