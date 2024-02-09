import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IZone, NewZone } from '../zone.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IZone for edit and NewZoneFormGroupInput for create.
 */
type ZoneFormGroupInput = IZone | PartialWithRequiredKeyOf<NewZone>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IZone | NewZone> = Omit<T, 'createdat' | 'updatedat'> & {
  createdat?: string | null;
  updatedat?: string | null;
};

type ZoneFormRawValue = FormValueOf<IZone>;

type NewZoneFormRawValue = FormValueOf<NewZone>;

type ZoneFormDefaults = Pick<NewZone, 'id' | 'createdat' | 'updatedat'>;

type ZoneFormGroupContent = {
  id: FormControl<ZoneFormRawValue['id'] | NewZone['id']>;
  name: FormControl<ZoneFormRawValue['name']>;
  description: FormControl<ZoneFormRawValue['description']>;
  palmsQuantity: FormControl<ZoneFormRawValue['palmsQuantity']>;
  isAffected: FormControl<ZoneFormRawValue['isAffected']>;
  createdat: FormControl<ZoneFormRawValue['createdat']>;
  updatedat: FormControl<ZoneFormRawValue['updatedat']>;
};

export type ZoneFormGroup = FormGroup<ZoneFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ZoneFormService {
  createZoneFormGroup(zone: ZoneFormGroupInput = { id: null }): ZoneFormGroup {
    const zoneRawValue = this.convertZoneToZoneRawValue({
      ...this.getFormDefaults(),
      ...zone,
    });
    return new FormGroup<ZoneFormGroupContent>({
      id: new FormControl(
        { value: zoneRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(zoneRawValue.name),
      description: new FormControl(zoneRawValue.description),
      palmsQuantity: new FormControl(zoneRawValue.palmsQuantity),
      isAffected: new FormControl(zoneRawValue.isAffected),
      createdat: new FormControl(zoneRawValue.createdat),
      updatedat: new FormControl(zoneRawValue.updatedat),
    });
  }

  getZone(form: ZoneFormGroup): IZone | NewZone {
    return this.convertZoneRawValueToZone(form.getRawValue() as ZoneFormRawValue | NewZoneFormRawValue);
  }

  resetForm(form: ZoneFormGroup, zone: ZoneFormGroupInput): void {
    const zoneRawValue = this.convertZoneToZoneRawValue({ ...this.getFormDefaults(), ...zone });
    form.reset(
      {
        ...zoneRawValue,
        id: { value: zoneRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ZoneFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdat: currentTime,
      updatedat: currentTime,
    };
  }

  private convertZoneRawValueToZone(rawZone: ZoneFormRawValue | NewZoneFormRawValue): IZone | NewZone {
    return {
      ...rawZone,
      createdat: dayjs(rawZone.createdat, DATE_TIME_FORMAT),
      updatedat: dayjs(rawZone.updatedat, DATE_TIME_FORMAT),
    };
  }

  private convertZoneToZoneRawValue(
    zone: IZone | (Partial<NewZone> & ZoneFormDefaults)
  ): ZoneFormRawValue | PartialWithRequiredKeyOf<NewZoneFormRawValue> {
    return {
      ...zone,
      createdat: zone.createdat ? zone.createdat.format(DATE_TIME_FORMAT) : undefined,
      updatedat: zone.updatedat ? zone.updatedat.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
