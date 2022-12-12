import dayjs from 'dayjs/esm';
import { MetodoPago } from 'app/entities/enumerations/metodo-pago.model';

export interface IPago {
  id: number;
  cantidad?: number | null;
  fechaPago?: dayjs.Dayjs | null;
  metodoPago?: MetodoPago | null;
  observaciones?: string | null;
}

export type NewPago = Omit<IPago, 'id'> & { id: null };
