import dayjs from 'dayjs/esm';

export interface IZone {
  id: number;
  name?: string | null;
  description?: string | null;
  palmsQuantity?: number | null;
  isAffected?: string | null;
  createdat?: dayjs.Dayjs | null;
  updatedat?: dayjs.Dayjs | null;
}

export type NewZone = Omit<IZone, 'id'> & { id: null };
