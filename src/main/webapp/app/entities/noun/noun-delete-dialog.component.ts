import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { INoun } from 'app/shared/model/noun.model';
import { NounService } from './noun.service';

@Component({
  selector: 'jhi-noun-delete-dialog',
  templateUrl: './noun-delete-dialog.component.html'
})
export class NounDeleteDialogComponent {
  noun: INoun;

  constructor(protected nounService: NounService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.nounService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'nounListModification',
        content: 'Deleted an noun'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-noun-delete-popup',
  template: ''
})
export class NounDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ noun }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(NounDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.noun = noun;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/noun', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/noun', { outlets: { popup: null } }]);
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
