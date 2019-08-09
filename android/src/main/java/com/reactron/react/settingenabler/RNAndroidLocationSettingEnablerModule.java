package com.reactron.react.settingenabler;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;

import com.facebook.react.bridge.BaseActivityEventListener;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import javax.annotation.Nonnull;

@ReactModule(name = RNAndroidLocationSettingEnablerModule.NAME)
public class RNAndroidLocationSettingEnablerModule extends ReactContextBaseJavaModule {
    private static final int REQUEST_CHECK_SETTINGS = 1;

    private static final String E_ACTIVITY_DOES_NOT_EXIST = "E_ACTIVITY_DOES_NOT_EXIST";
    private static final String E_SETTINGS_CHANGE_UNAVAILABLE = "E_SETTINGS_CHANGE_UNAVAILABLE";
    private static final String E_USER_CANCELED = "E_USER_CANCELED";

    private final ReactApplicationContext mContext;

    private Promise mLocationSettingPromise;

    public RNAndroidLocationSettingEnablerModule(@Nonnull ReactApplicationContext reactContext) {
        super(reactContext);
        mContext = reactContext;
        reactContext.addActivityEventListener(mActivityEventListener);
    }

    private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {

        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
            final LocationSettingsStates states = LocationSettingsStates.fromIntent(intent);
            if (requestCode == REQUEST_CHECK_SETTINGS && mLocationSettingPromise != null) {
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        mLocationSettingPromise.resolve(null);

                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        mLocationSettingPromise.reject(
                                E_USER_CANCELED,
                                "The user was asked to change settings, but chose not to");
                        break;
                    default:
                        break;
                }
                mLocationSettingPromise = null;

            }
        }

        @Override
        public void onNewIntent(Intent intent) {
            super.onNewIntent(intent);
        }
    };

    @ReactMethod()
    public void checkAndEnableGPSAndBLE(Promise promise) {
        Activity currentActivity = getCurrentActivity();

        if (currentActivity == null) {
            promise.reject(E_ACTIVITY_DOES_NOT_EXIST, "Activity doesn't exist");
            return;
        }

        LocationRequest mLocationRequestHighAccuracy = LocationRequest
                .create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationRequest mLocationRequestBalancedPowerAccuracy = LocationRequest
                .create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequestHighAccuracy)
                .addLocationRequest(mLocationRequestBalancedPowerAccuracy)
                .setNeedBle(true);
        Task<LocationSettingsResponse> task =
                LocationServices
                        .getSettingsClient(mContext.getCurrentActivity())
                        .checkLocationSettings(builder.build());
        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    // All location settings are satisfied. The client can initialize location
                    // requests here.
                    promise.resolve(null);
                } catch (ApiException exception) {

                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
                            mLocationSettingPromise = promise;
                            try {
                                // Cast to a resolvable exception.
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(
                                        mContext.getCurrentActivity(),
                                        REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            } catch (ClassCastException e) {
                                // Ignore, should be an impossible error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                            promise.reject(
                                    E_SETTINGS_CHANGE_UNAVAILABLE,
                                    "Location settings are not satisfied. " +
                                            "However, we have no way to fix the settings " +
                                            "so we won't show the dialog.");
                            break;
                    }
                }
            }
        });
    }


    @Nonnull
    @Override
    public String getName() {
        return "RNAndroidLocationSettingEnablerModule";
    }
}
