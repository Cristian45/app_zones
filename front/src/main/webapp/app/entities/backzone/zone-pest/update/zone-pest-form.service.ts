import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IZonePest, NewZonePest } from '../zone-pest.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IZonePest for edit and NewZonePestFormGroupInput for create.
 */
type ZonePestFormGroupInput = IZonePest | PartialWithRequiredKeyOf<NewZonePest>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IZonePest | NewZonePest> = Omit<T, 'createdat' | 'updatedat'> & {
  createdat?: string | null;
  updatedat?: string | null;
};

type ZonePestFormRawValue = FormValueOf<IZonePest>;

type NewZonePestFormRawValue = FormValueOf<NewZonePest>;

type ZonePestFormDefaults = Pick<NewZonePest, 'id' | 'createdat' | 'updatedat'>;

type ZonePestFormGroupContent = {
  id: FormControl<ZonePestFormRawValue['id'] | NewZonePest['id']>;
  createdat: FormControl<ZonePestFormRawValue['createdat']>;
  updatedat: FormControl<ZonePestFormRawValue['updatedat']>;
  zoneId: FormControl<ZonePestFormRawValue['zoneId']>;
  pestId: FormControl<ZonePestFormRawValue['pestId']>;
};

export type ZonePestFormGroup = FormGroup<ZonePestFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ZonePestFormService {
  createZonePestFormGroup(zonePest: ZonePestFormGroupInput = { id: null }): ZonePestFormGroup {
    const zonePestRawValue = this.convertZonePestToZonePestRawValue({
      ...this.getFormDefaults(),
      ...zonePest,
    });
    return new FormGroup<ZonePestFormGroupContent>({
      id: new FormControl(
        { value: zonePestRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      createdat: new FormControl(zonePestRawValue.createdat),
      updatedat: new FormControl(zonePestRawValue.updatedat),
      zoneId: new FormControl(zonePestRawValue.zoneId),
      pestId: new FormControl(zonePestRawValue.pestId),
    });
  }

  getZonePest(form: ZonePestFormGroup): IZonePest | NewZonePest {
    return this.convertZonePestRawValueToZonePest(form.getRawValue() as ZonePestFormRawValue | NewZonePestFormRawValue);
  }

  resetForm(form: ZonePestFormGroup, zonePest: ZonePestFormGroupInput): void {
    const zonePestRawValue = this.convertZonePestToZonePestRawValue({ ...this.getFormDefaults(), ...zonePest });
    form.reset(
      {
        ...zonePestRawValue,
        id: { value: zonePestRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ZonePestFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdat: currentTime,
      updatedat: currentTime,
    };
  }

  private convertZonePestRawValueToZonePest(rawZonePest: ZonePestFormRawValue | NewZonePestFormRawValue): IZonePest | NewZonePest {
    return {
      ...rawZonePest,
      createdat: dayjs(rawZonePest.createdat, DATE_TIME_FORMAT),
      updatedat: dayjs(rawZonePest.updatedat, DATE_TIME_FORMAT),
    };
  }

  private convertZonePestToZonePestRawValue(
    zonePest: IZonePest | (Partial<NewZonePest> & ZonePestFormDefaults)
  ): ZonePestFormRawValue | PartialWithRequiredKeyOf<NewZonePestFormRawValue> {
    return {
      ...zonePest,
      createdat: zonePest.createdat ? zonePest.createdat.format(DATE_TIME_FORMAT) : undefined,
      updatedat: zonePest.updatedat ? zonePest.updatedat.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
