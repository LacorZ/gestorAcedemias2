import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PaymentMethodComponent } from './list/payment-method.component';
import { PaymentMethodDetailComponent } from './detail/payment-method-detail.component';
import { PaymentMethodUpdateComponent } from './update/payment-method-update.component';
import { PaymentMethodDeleteDialogComponent } from './delete/payment-method-delete-dialog.component';
import { PaymentMethodRoutingModule } from './route/payment-method-routing.module';

@NgModule({
  imports: [SharedModule, PaymentMethodRoutingModule],
  declarations: [PaymentMethodComponent, PaymentMethodDetailComponent, PaymentMethodUpdateComponent, PaymentMethodDeleteDialogComponent],
})
export class PaymentMethodModule {}
