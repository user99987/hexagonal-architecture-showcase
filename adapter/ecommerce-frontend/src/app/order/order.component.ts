import {
  ChangeDetectionStrategy,
  Component,
  inject,
  signal,
} from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';

import { OrderService } from '@app/order/order.service';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ReactiveFormsModule],
})
export class OrderComponent {
  private readonly orderService = inject(OrderService);

  readonly submitting = signal(false);
  readonly orderNumber = signal<string | null>(null);
  readonly errorMessage = signal<string | null>(null);

  // Remarks max length mirrors the REMARK column in OrderEntity (length = 800)
  readonly orderForm = new FormGroup({
    remarks: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(800)],
    }),
  });

  get remarksControl() {
    return this.orderForm.controls.remarks;
  }

  placeOrder(): void {
    if (this.orderForm.invalid || this.submitting()) return;
    this.submitting.set(true);
    this.orderNumber.set(null);
    this.errorMessage.set(null);
    this.orderService
      .placeOrder(this.orderForm.getRawValue().remarks)
      .subscribe({
        next: (response) => {
          this.submitting.set(false);
          if (!response.orderNumber) {
            this.errorMessage.set('You already have an order.');
          } else {
            this.orderNumber.set(response.orderNumber);
          }
        },
        error: () => {
          this.submitting.set(false);
          this.errorMessage.set('Failed to place order. Please try again.');
        },
      });
  }
}
