import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPest, NewPest } from '../pest.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPest for edit and NewPestFormGroupInput for create.
 */
type PestFormGroupInput = IPest | PartialWithRequiredKeyOf<NewPest>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPest | NewPest> = Omit<T, 'createdat' | 'updatedat'> & {
  createdat?: string | null;
  updatedat?: string | null;
};

type PestFormRawValue = FormValueOf<IPest>;

type NewPestFormRawValue = FormValueOf<NewPest>;

type PestFormDefaults = Pick<NewPest, 'id' | 'createdat' | 'updatedat'>;

type PestFormGroupContent = {
  id: FormControl<PestFormRawValue['id'] | NewPest['id']>;
  name: FormControl<PestFormRawValue['name']>;
  description: FormControl<PestFormRawValue['description']>;
  createdat: FormControl<PestFormRawValue['createdat']>;
  updatedat: FormControl<PestFormRawValue['updatedat']>;
};

export type PestFormGroup = FormGroup<PestFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PestFormService {
  createPestFormGroup(pest: PestFormGroupInput = { id: null }): PestFormGroup {
    const pestRawValue = this.convertPestToPestRawValue({
      ...this.getFormDefaults(),
      ...pest,
    });
    return new FormGroup<PestFormGroupContent>({
      id: new FormControl(
        { value: pestRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(pestRawValue.name),
      description: new FormControl(pestRawValue.description),
      createdat: new FormControl(pestRawValue.createdat),
      updatedat: new FormControl(pestRawValue.updatedat),
    });
  }

  getPest(form: PestFormGroup): IPest | NewPest {
    return this.convertPestRawValueToPest(form.getRawValue() as PestFormRawValue | NewPestFormRawValue);
  }

  resetForm(form: PestFormGroup, pest: PestFormGroupInput): void {
    const pestRawValue = this.convertPestToPestRawValue({ ...this.getFormDefaults(), ...pest });
    form.reset(
      {
        ...pestRawValue,
        id: { value: pestRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PestFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdat: currentTime,
      updatedat: currentTime,
    };
  }

  private convertPestRawValueToPest(rawPest: PestFormRawValue | NewPestFormRawValue): IPest | NewPest {
    return {
      ...rawPest,
      createdat: dayjs(rawPest.createdat, DATE_TIME_FORMAT),
      updatedat: dayjs(rawPest.updatedat, DATE_TIME_FORMAT),
    };
  }

  private convertPestToPestRawValue(
    pest: IPest | (Partial<NewPest> & PestFormDefaults)
  ): PestFormRawValue | PartialWithRequiredKeyOf<NewPestFormRawValue> {
    return {
      ...pest,
      createdat: pest.createdat ? pest.createdat.format(DATE_TIME_FORMAT) : undefined,
      updatedat: pest.updatedat ? pest.updatedat.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
