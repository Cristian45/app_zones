import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPest } from '../pest.model';

@Component({
  selector: 'jhi-pest-detail',
  templateUrl: './pest-detail.component.html',
})
export class PestDetailComponent implements OnInit {
  pest: IPest | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pest }) => {
      this.pest = pest;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
