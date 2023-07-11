import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-usage-stats-manager' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const UsageStatsManager = NativeModules.UsageStatsManager
  ? NativeModules.UsageStatsManager
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function queryUsageStats(
  interval: number,
  startTime: number,
  endTime: number
): Promise<any> {
  return UsageStatsManager.queryUsageStats(interval, startTime, endTime);
}

export function showUsageAccessSettings(packageName: string) {
  return UsageStatsManager.showUsageAccessSettings(packageName);
}

export function checkForPermission(): Promise<any> {
  return UsageStatsManager.checkForPermission();
}

export function getAppDataUsage(
  packageName: string,
  networkType: number,
  startTime: number,
  endTime: number
): Promise<any> {
  return UsageStatsManager.getAppDataUsage(
    packageName,
    networkType,
    startTime,
    endTime
  );
}

export enum EventFrequency {
  INTERVAL_DAILY = 0,
  INTERVAL_WEEKLY = 1,
  INTERVAL_MONTHLY = 2,
  INTERVAL_YEARLY = 3,
  INTERVAL_BEST = 4,
  INTERVAL_COUNT = 4,
}
