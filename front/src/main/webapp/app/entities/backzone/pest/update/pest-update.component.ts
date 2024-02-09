import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { PestFormService, PestFormGroup } from './pest-form.service';
import { IPest } from '../pest.model';
import { PestService } from '../service/pest.service';

@Component({
  selector: 'jhi-pest-update',
  templateUrl: './pest-update.component.html',
})
export class PestUpdateComponent implements OnInit {
  isSaving = false;
  pest: IPest | null = null;

  editForm: PestFormGroup = this.pestFormService.createPestFormGroup();

  constructor(protected pestService: PestService, protected pestFormService: PestFormService, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pest }) => {
      this.pest = pest;
      if (pest) {
        this.updateForm(pest);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pest = this.pestFormService.getPest(this.editForm);
    if (pest.id !== null) {
      this.subscribeToSaveResponse(this.pestService.update(pest));
    } else {
      this.subscribeToSaveResponse(this.pestService.create(pest));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPest>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(pest: IPest): void {
    this.pest = pest;
    this.pestFormService.resetForm(this.editForm, pest);
  }
}
