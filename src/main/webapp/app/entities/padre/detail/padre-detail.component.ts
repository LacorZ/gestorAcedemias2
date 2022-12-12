import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPadre } from '../padre.model';

@Component({
  selector: 'jhi-padre-detail',
  templateUrl: './padre-detail.component.html',
})
export class PadreDetailComponent implements OnInit {
  padre: IPadre | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ padre }) => {
      this.padre = padre;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
