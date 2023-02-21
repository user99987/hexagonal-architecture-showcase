import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '@environments/environment';
import { OrderResponseModel } from '@app/order/order-response.model';
import { OrderRequestModel } from '@app/order/order-request.model';

@Injectable()
export class OrderService {
  private readonly httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
    }),
  };

  constructor(private readonly httpClient: HttpClient) {}

  private static buildOrderRequest(): OrderRequestModel {
    return {
      remarks: 'remarks',
      created: new Date(),
    };
  }

  placeOrder(): Observable<OrderResponseModel> {
    return this.httpClient.post<OrderResponseModel>(
      `${environment.apiPrefix}/order`,
      OrderService.buildOrderRequest(),
      this.httpOptions
    );
  }
}
