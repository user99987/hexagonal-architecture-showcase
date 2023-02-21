import { Component } from '@angular/core';
import { OrderService } from '@app/order/order.service';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss'],
})
export class OrderComponent {
  constructor(private readonly orderService: OrderService) {}

  placeOrder() {
    this.orderService.placeOrder().subscribe((data) => console.log(data));
    console.log('Order placed');
  }
}
