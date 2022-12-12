import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PadreComponent } from './list/padre.component';
import { PadreDetailComponent } from './detail/padre-detail.component';
import { PadreUpdateComponent } from './update/padre-update.component';
import { PadreDeleteDialogComponent } from './delete/padre-delete-dialog.component';
import { PadreRoutingModule } from './route/padre-routing.module';

@NgModule({
  imports: [SharedModule, PadreRoutingModule],
  declarations: [PadreComponent, PadreDetailComponent, PadreUpdateComponent, PadreDeleteDialogComponent],
})
export class PadreModule {}
