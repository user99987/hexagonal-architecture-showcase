import { APP_BASE_HREF } from '@angular/common';
import { provideHttpClient, withInterceptorsFromDi, withXhr } from '@angular/common/http';
import { ApplicationConfig, ErrorHandler } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { provideNoopAnimations } from '@angular/platform-browser/animations';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideNoopAnimations(),
    provideHttpClient(withXhr(), withInterceptorsFromDi()),
    Title,
    { provide: APP_BASE_HREF, useValue: '/home' },
    { provide: ErrorHandler },
  ],
};
