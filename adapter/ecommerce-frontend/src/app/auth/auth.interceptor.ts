import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';

import { environment } from '@environments/environment';
import { AuthService } from '@app/auth/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getAccessToken();
  if (token && req.url.includes(environment.apiPrefix)) {
    return next(
      req.clone({
        headers: req.headers.set('Authorization', `Bearer ${token}`),
      }),
    );
  }
  return next(req);
};
