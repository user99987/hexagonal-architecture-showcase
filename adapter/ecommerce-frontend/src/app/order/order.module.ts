import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { OrderService } from '@app/order/order.service';
import { OrderComponent } from '@app/order/order.component';
import { OrderRoutingModule } from '@app/order/order-routing.module';

@NgModule({
  declarations: [OrderComponent],
  imports: [CommonModule, ReactiveFormsModule, OrderRoutingModule],
  providers: [OrderService],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  exports: [OrderComponent],
})
export class OrderModule {}
