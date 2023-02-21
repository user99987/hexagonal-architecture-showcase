import packageInfo from '../../package.json';

export const environmentBase = {
  production: false,
  appVersion: packageInfo.version,
  apiPrefix: '/home/api',
};
