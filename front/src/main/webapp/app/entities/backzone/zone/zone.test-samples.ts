import dayjs from 'dayjs/esm';

import { IZone, NewZone } from './zone.model';

export const sampleWithRequiredData: IZone = {
  id: 81853,
};

export const sampleWithPartialData: IZone = {
  id: 56672,
  name: 'configurable La Deportes',
  description: 'Cataluña Grupo',
  isAffected: 'cero',
  createdat: dayjs('2024-02-05T18:19'),
};

export const sampleWithFullData: IZone = {
  id: 7781,
  name: 'Calle Extramuros International',
  description: 'Creativo Lado',
  palmsQuantity: 400,
  isAffected: 'Cambridgeshire servidor parsing',
  createdat: dayjs('2024-02-05T04:36'),
  updatedat: dayjs('2024-02-05T08:28'),
};

export const sampleWithNewData: NewZone = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
