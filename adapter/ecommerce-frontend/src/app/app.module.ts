import { BrowserModule, Title } from '@angular/platform-browser';
import { CUSTOM_ELEMENTS_SCHEMA, ErrorHandler, NgModule } from '@angular/core';
import {
  provideHttpClient,
  withInterceptorsFromDi,
} from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { OrderModule } from '@app/order/order.module';

@NgModule({
  declarations: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  bootstrap: [AppComponent],
  imports: [BrowserModule, NoopAnimationsModule, AppRoutingModule, OrderModule],
  providers: [
    Title,
    {
      provide: ErrorHandler,
    },
    provideHttpClient(withInterceptorsFromDi()),
  ],
})
export class AppModule {}
