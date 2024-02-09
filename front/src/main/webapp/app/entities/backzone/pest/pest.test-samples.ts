import dayjs from 'dayjs/esm';

import { IPest, NewPest } from './pest.model';

export const sampleWithRequiredData: IPest = {
  id: 46170,
};

export const sampleWithPartialData: IPest = {
  id: 34465,
  description: 'transmit Distrito architectures',
  createdat: dayjs('2024-02-05T11:21'),
  updatedat: dayjs('2024-02-06T03:14'),
};

export const sampleWithFullData: IPest = {
  id: 34174,
  name: 'Verde',
  description: 'Guapa Estratega',
  createdat: dayjs('2024-02-05T21:43'),
  updatedat: dayjs('2024-02-05T15:38'),
};

export const sampleWithNewData: NewPest = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
