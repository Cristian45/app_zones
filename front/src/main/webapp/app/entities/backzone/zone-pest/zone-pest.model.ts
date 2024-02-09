import dayjs from 'dayjs/esm';
import { IZone } from 'app/entities/backzone/zone/zone.model';
import { IPest } from 'app/entities/backzone/pest/pest.model';

export interface IZonePest {
  id: number;
  createdat?: dayjs.Dayjs | null;
  updatedat?: dayjs.Dayjs | null;
  zoneId?: Pick<IZone, 'id'> | null;
  pestId?: Pick<IPest, 'id'> | null;
}

export type NewZonePest = Omit<IZonePest, 'id'> & { id: null };
