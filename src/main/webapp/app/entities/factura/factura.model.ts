import dayjs from 'dayjs/esm';
import { IPago } from 'app/entities/pago/pago.model';

export interface IFactura {
  id: number;
  facturado?: number | null;
  fechaFactura?: dayjs.Dayjs | null;
  observaciones?: string | null;
  pagos?: Pick<IPago, 'id'> | null;
  pago?: Pick<IPago, 'id'> | null;
}

export type NewFactura = Omit<IFactura, 'id'> & { id: null };
