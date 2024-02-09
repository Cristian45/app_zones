import dayjs from 'dayjs/esm';

import { IZonePest, NewZonePest } from './zone-pest.model';

export const sampleWithRequiredData: IZonePest = {
  id: 56405,
};

export const sampleWithPartialData: IZonePest = {
  id: 31848,
  createdat: dayjs('2024-02-05T14:51'),
  updatedat: dayjs('2024-02-05T11:09'),
};

export const sampleWithFullData: IZonePest = {
  id: 62577,
  createdat: dayjs('2024-02-05T08:27'),
  updatedat: dayjs('2024-02-05T05:11'),
};

export const sampleWithNewData: NewZonePest = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
