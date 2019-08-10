declare module "react-native-android-location-setting-enabler" {
  export const PRIORITY_BALANCED_POWER_ACCURACY: number;
  export const PRIORITY_HIGH_ACCURACY: number;
  export const PRIORITY_LOW_POWER: number;
  export const PRIORITY_NO_POWER: number;

  export interface LocationSettingRequest {
    priorities: number[];
    needBle: boolean;
  }

  /**
   * 
   * @param options Options for the location setting request
   * @returns Promise, the promise resolve if the location setting satisfies,
   * reject otherwises
   */
  export function checkLocationSettingStatus(options: LocationSettingRequest): Promise<void> {}

  /**
   * 
   * @param options Options for the location setting request
   * @returns Promise, the promise resolve if the location setting satisfies,
   * reject otherwises
   */
  export function checkAndEnableLocationSetting(options: LocationSettingRequest): Promise<void> {}
}
