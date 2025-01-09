import { Component } from '@angular/core';
import { OrderComponent } from '@app/order/order.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  imports: [OrderComponent],
})
export class AppComponent {}
