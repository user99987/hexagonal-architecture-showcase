import { HttpErrorResponse } from '@angular/common/http';
import {
  ComponentFixture,
  TestBed,
  fakeAsync,
  tick,
} from '@angular/core/testing';
import { of, throwError } from 'rxjs';

import { OrderComponent } from '@app/order/order.component';
import { OrderService } from '@app/order/order.service';

describe('OrderComponent', () => {
  let fixture: ComponentFixture<OrderComponent>;
  let component: OrderComponent;
  let placeOrderSpy: jasmine.Spy;

  function setup(): void {
    placeOrderSpy = jasmine.createSpy('placeOrder');
    TestBed.configureTestingModule({
      imports: [OrderComponent],
      providers: [
        {
          provide: OrderService,
          useValue: { placeOrder: placeOrderSpy },
        },
      ],
    });
    fixture = TestBed.createComponent(OrderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }

  afterEach(() => {
    TestBed.resetTestingModule();
  });

  it('should create the component', () => {
    setup();
    expect(component).toBeTruthy();
  });

  it('should not submit when form is invalid', () => {
    setup();
    component.placeOrder();
    expect(placeOrderSpy).not.toHaveBeenCalled();
  });

  it('should not submit when already submitting', () => {
    setup();
    component.orderForm.setValue({ remarks: 'valid remarks' });
    component.submitting.set(true);
    component.placeOrder();
    expect(placeOrderSpy).not.toHaveBeenCalled();
  });

  it('should call orderService.placeOrder() with remarks value', fakeAsync(() => {
    setup();
    placeOrderSpy.and.returnValue(of({ orderNumber: '123' }));
    component.orderForm.setValue({ remarks: 'my remarks' });
    component.placeOrder();
    tick();
    expect(placeOrderSpy).toHaveBeenCalledWith('my remarks');
  }));

  it('on success with orderNumber: sets orderNumber signal and shows it', fakeAsync(() => {
    setup();
    placeOrderSpy.and.returnValue(of({ orderNumber: 'ORD-001' }));
    component.orderForm.setValue({ remarks: 'test' });
    component.placeOrder();
    tick();
    fixture.detectChanges();
    expect(component.orderNumber()).toBe('ORD-001');
    const compiled = fixture.nativeElement as HTMLElement;
    const orderNumberEl = compiled.querySelector(
      '[data-testid="order-number"]',
    );
    expect(orderNumberEl?.textContent).toContain('ORD-001');
  }));

  it('on success with empty orderNumber: shows "You already have an order." message', fakeAsync(() => {
    setup();
    placeOrderSpy.and.returnValue(of({ orderNumber: '' }));
    component.orderForm.setValue({ remarks: 'test' });
    component.placeOrder();
    tick();
    fixture.detectChanges();
    expect(component.errorMessage()).toBe('You already have an order.');
    const compiled = fixture.nativeElement as HTMLElement;
    const alert = compiled.querySelector('[role="alert"]');
    expect(alert?.textContent).toContain('You already have an order.');
  }));

  it('on HTTP error: shows "Failed to place order." message', fakeAsync(() => {
    setup();
    placeOrderSpy.and.returnValue(
      throwError(() => new HttpErrorResponse({ status: 500 })),
    );
    component.orderForm.setValue({ remarks: 'test' });
    component.placeOrder();
    tick();
    fixture.detectChanges();
    expect(component.errorMessage()).toBe(
      'Failed to place order. Please try again.',
    );
    const compiled = fixture.nativeElement as HTMLElement;
    const alert = compiled.querySelector('[role="alert"]');
    expect(alert?.textContent).toContain(
      'Failed to place order. Please try again.',
    );
  }));

  it('shows loading indicator while submitting', () => {
    setup();
    component.orderForm.setValue({ remarks: 'test' });
    component.submitting.set(true);
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    const loading = compiled.querySelector('.loading');
    expect(loading).toBeTruthy();
  });

  it('shows validation message when remarks is touched and empty', () => {
    setup();
    component.remarksControl.markAsTouched();
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('.error')?.textContent).toContain(
      'Remarks are required.',
    );
  });

  it('shows maxlength validation message when remarks exceeds 800 chars', () => {
    setup();
    component.remarksControl.setValue('a'.repeat(801));
    component.remarksControl.markAsTouched();
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('.error')?.textContent).toContain(
      'Remarks must be at most 800 characters.',
    );
  });
});
