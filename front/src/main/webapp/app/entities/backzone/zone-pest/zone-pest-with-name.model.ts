import dayjs from 'dayjs/esm';
import { IZone } from 'app/entities/backzone/zone/zone.model';
import { IPest } from 'app/entities/backzone/pest/pest.model';

export interface IZonePestWithName {
  id: number;
  zoneId: number;
  zoneName: String;
  pestId: number;
  pestName: String;
}
  