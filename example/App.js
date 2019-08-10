/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, { Fragment, useState } from "react";
import {
  SafeAreaView,
  StyleSheet,
  ScrollView,
  View,
  Text,
  StatusBar,
  Button,
  NativeModules
} from "react-native";

import {
  Header,
  LearnMoreLinks,
  Colors
} from "react-native/Libraries/NewAppScreen";
import RNAndroidLocationSettingEnabler from "react-native-android-location-setting-enabler";

const App = () => {
  const [locationSettingEnabled, setLocationSettingEnabled] = useState(
    "unknown"
  );

  return (
    <Fragment>
      <StatusBar barStyle="dark-content" />
      <SafeAreaView>
        <ScrollView
          contentInsetAdjustmentBehavior="automatic"
          style={styles.scrollView}
        >
          <Header />
          {global.HermesInternal == null ? null : (
            <View style={styles.engine}>
              <Text style={styles.footer}>Engine: Hermes</Text>
            </View>
          )}
          <View style={styles.body}>
            <View style={styles.sectionContainer}>
              <Text style={styles.sectionTitle}>Location Setting Status</Text>
              <Text style={styles.sectionDescription}>
                {locationSettingEnabled}
              </Text>
            </View>
            <View style={styles.sectionContainer}>
              <Text style={styles.sectionTitle}>
                Check Location Setting Status
              </Text>

              <Button
                title="Check Location Setting"
                onPress={() => {
                  RNAndroidLocationSettingEnabler.checkLocationSettingStatus({
                    priorities: [
                      RNAndroidLocationSettingEnabler.PRIORITY_BALANCED_POWER_ACCURACY
                    ],
                    needBle: false
                  })
                    .then(() => setLocationSettingEnabled("Enabled"))
                    .catch(error => setLocationSettingEnabled(error.message));
                }}
              />
            </View>
            <LearnMoreLinks />
          </View>
        </ScrollView>
      </SafeAreaView>
    </Fragment>
  );
};

const styles = StyleSheet.create({
  scrollView: {
    backgroundColor: Colors.lighter
  },
  engine: {
    position: "absolute",
    right: 0
  },
  body: {
    backgroundColor: Colors.white
  },
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: "600",
    color: Colors.black
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: "400",
    color: Colors.dark
  },
  highlight: {
    fontWeight: "700"
  },
  footer: {
    color: Colors.dark,
    fontSize: 12,
    fontWeight: "600",
    padding: 4,
    paddingRight: 12,
    textAlign: "right"
  }
});

export default App;
