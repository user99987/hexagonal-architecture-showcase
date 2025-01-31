import { TestBed } from '@angular/core/testing';

import {
  HttpTestingController,
  provideHttpClientTesting,
} from '@angular/common/http/testing';
import { OrderService } from '@app/order/order.service';
import { OrderResponseModel } from '@app/order/order-response.model';
import { environment } from '@environments/environment';
import {
  provideHttpClient,
  withInterceptorsFromDi,
} from '@angular/common/http';

describe('OrderService', () => {
  let orderService: OrderService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [],
      providers: [
        OrderService,
        provideHttpClient(withInterceptorsFromDi()),
        provideHttpClientTesting(),
      ],
    }).compileComponents();
    orderService = TestBed.inject(OrderService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(orderService).toBeTruthy();
  });

  it('should call backend order service and get response', () => {
    //given
    const orderResponse = {
      orderNumber: '20220915123015',
    } as OrderResponseModel;

    //when
    orderService
      .placeOrder()
      .subscribe((data) => expect(data).toBe(orderResponse));

    //then
    const req = httpTestingController.expectOne(
      `${environment.apiPrefix}/order`
    );
    expect(req.request.method).toBe('POST');

    req.flush(orderResponse);
  });
});
