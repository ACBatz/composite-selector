import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IEnvironmentalEffect } from 'app/shared/model/environmental-effect.model';
import { EnvironmentalEffectService } from './environmental-effect.service';

@Component({
  selector: 'jhi-environmental-effect-delete-dialog',
  templateUrl: './environmental-effect-delete-dialog.component.html'
})
export class EnvironmentalEffectDeleteDialogComponent {
  environmentalEffect: IEnvironmentalEffect;

  constructor(
    protected environmentalEffectService: EnvironmentalEffectService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.environmentalEffectService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'environmentalEffectListModification',
        content: 'Deleted an environmentalEffect'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-environmental-effect-delete-popup',
  template: ''
})
export class EnvironmentalEffectDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ environmentalEffect }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(EnvironmentalEffectDeleteDialogComponent as Component, {
          size: 'lg',
          backdrop: 'static'
        });
        this.ngbModalRef.componentInstance.environmentalEffect = environmentalEffect;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/environmental-effect', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/environmental-effect', { outlets: { popup: null } }]);
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
