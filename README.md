# react-native-android-location-setting-enabler

## Getting started

`$ npm install react-native-android-location-setting-enabler --save`

### Mostly automatic installation

`$ react-native link react-native-android-location-setting-enabler`

### Manual installation

1. Open up `android/app/src/main/java/[...]/MainApplication.java`
  - Add `import com.reactron.react.settingenabler.RNAndroidLocationSettingEnablerPackage;` to the imports at the top of the file
  - Add `new RNAndroidLocationSettingEnablerPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-android-location-setting-enabler'
  	project(':react-native-android-location-setting-enabler').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-android-location-setting-enabler/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-android-location-setting-enabler')
  	```


## Usage
```javascript
import RNAndroidLocationSettingEnabler from 'react-native-android-location-setting-enabler';

// TODO: What to do with the module?
RNAndroidLocationSettingEnabler;
```
