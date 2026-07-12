import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: 'order',
    loadComponent: () => import('./order/order.component').then((m) => m.OrderComponent),
  },
  {
    path: '',
    redirectTo: 'order',
    pathMatch: 'full',
  },
  {
    path: '**',
    redirectTo: 'error',
    pathMatch: 'full',
  },
];
