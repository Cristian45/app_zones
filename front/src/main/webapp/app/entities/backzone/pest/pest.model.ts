import dayjs from 'dayjs/esm';

export interface IPest {
  id: number;
  name?: string | null;
  description?: string | null;
  createdat?: dayjs.Dayjs | null;
  updatedat?: dayjs.Dayjs | null;
}

export type NewPest = Omit<IPest, 'id'> & { id: null };
