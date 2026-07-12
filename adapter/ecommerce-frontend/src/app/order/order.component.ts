import { ChangeDetectionStrategy, Component, inject } from '@angular/core';

import { OrderService } from '@app/order/order.service';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class OrderComponent {
  private readonly orderService = inject(OrderService);

  placeOrder() {
    this.orderService.placeOrder().subscribe((data) => console.log(data));
    console.log('Order placed');
  }
}
