# Android Device Names

[![Maven Central](https://img.shields.io/maven-central/v/org.rbsoft/android-device-names)](https://central.sonatype.com/artifact/org.rbsoft/android-device-names) [![License](http://img.shields.io/:license-apache-blue.svg)](LICENSE.txt) [![API](https://img.shields.io/badge/API-16%2B-blue.svg?style=flat)](https://android-arsenal.com/api?level=16) [![Update device database](https://github.com/ravibpatel/AndroidDeviceNames/actions/workflows/update-database.yml/badge.svg)](https://github.com/ravibpatel/AndroidDeviceNames/actions/workflows/update-database.yml)

A small Android library to get the market name of an Android device.

On many popular devices the market name of the device is not available. For example, on the Samsung Galaxy S7 the value of [`Build.MODEL`](http://developer.android.com/reference/android/os/Build.html#MODEL) could be `"SAMSUNG-SM-G930A"`, `"SM-G930F"`, `"SM-G930K"`, `"SM-G930L"`, etc.

This small library gets the market (consumer-friendly) name of a device.

Usage
-----

**Set up the library**

```java
DeviceName.init(this);
```

**Get the name of the current device:**

```java
String deviceName = DeviceName.getDeviceName();
```

The above code will get the correct device name for the top 600 Android devices. If the device is unrecognized, then [`Build.MODEL`](http://developer.android.com/reference/android/os/Build.html#MODEL) is returned. This can be executed from the UI thread.

**Get the name of a device using the device's codename:**

```java
// Returns "Moto X Style"
DeviceName.getDeviceName("clark", "Unknown device");
```

**Get information about the device:**

```java
DeviceName.with(context).request(new DeviceName.Callback() {

  @Override public void onFinished(DeviceName.DeviceInfo info, Exception error) {
    String manufacturer = info.manufacturer;  // "Samsung"
    String name = info.marketName;            // "Galaxy S8+"
    String model = info.model;                // "SM-G955W"
    String codename = info.codename;          // "dream2qltecan"
    String deviceName = info.getName();       // "Galaxy S8+"
    // FYI: We are on the UI thread.
  }
});
 ```

The above code queries a database included in the library based on [Google's maintained list
](https://support.google.com/googleplay/answer/1727131?hl=en). This supports *over 50,000* devices.

Download
--------

Download [the latest AAR](https://repo1.maven.org/maven2/org/rbsoft/android-device-names/2.2.0/android-device-names-2.2.0.aar) or grab via Gradle:

```groovy
implementation('org.rbsoft:android-device-names:2.2.0')
```

Database updates
----------------

The bundled device database is refreshed automatically. On the 1st of every month the
[Update device database](.github/workflows/update-database.yml) workflow downloads Google's
[supported devices list](https://storage.googleapis.com/play_public/supported_devices.html),
regenerates `database/android-devices.db`, and, if anything changed, bumps `VERSION_NAME` in
[gradle.properties](gradle.properties), tags the release and publishes the new version to
Maven Central through the [Publish](.github/workflows/publish.yml) workflow.

To regenerate the database locally:

```
./gradlew :generator:generateDatabase
```


License
--------

    Copyright (C) 2015 Jared Rummler

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
