import { ChangeDetectionStrategy, Component } from '@angular/core';

import { OrderComponent } from './order/order.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [OrderComponent],
})
export class AppComponent {}
