import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITutor } from '../tutor.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-tutor-detail',
  templateUrl: './tutor-detail.component.html',
})
export class TutorDetailComponent implements OnInit {
  tutor: ITutor | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tutor }) => {
      this.tutor = tutor;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
