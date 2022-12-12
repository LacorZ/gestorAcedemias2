import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITutor } from '../tutor.model';

@Component({
  selector: 'jhi-tutor-detail',
  templateUrl: './tutor-detail.component.html',
})
export class TutorDetailComponent implements OnInit {
  tutor: ITutor | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tutor }) => {
      this.tutor = tutor;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
