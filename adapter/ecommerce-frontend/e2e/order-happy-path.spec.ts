import { expect, test } from '@playwright/test';

test('order happy path: login and place order', async ({ page }) => {
  await page.goto('/');
  await expect(page).toHaveURL(/login/);

  await page.getByTestId('login-username').fill('order-admin');
  await page.getByTestId('login-password').fill('password');
  await page.getByTestId('login-submit').click();

  await expect(page).toHaveURL(/order/);

  await page
    .getByTestId('order-remarks')
    .fill('Integration test order remarks');
  await page.getByTestId('order-submit').click();

  await expect(page.getByTestId('order-number')).toBeVisible({
    timeout: 10_000,
  });
});
