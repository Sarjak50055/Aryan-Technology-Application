#!/bin/bash
mkdir -p LiftApp/app/src/main/java/com/example/myliftapp
mkdir -p LiftApp/app/src/main/res/layout
mkdir -p LiftApp/app/src/main/res/values
mkdir -p LiftApp/app/src/main/assets
mkdir -p LiftApp/gradle/wrapper

cat << 'GRADLE' > LiftApp/build.gradle
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:8.1.1'
    }
}
allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
GRADLE

cat << 'SETTINGS' > LiftApp/settings.gradle
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "LiftApp"
include ':app'
SETTINGS

cat << 'APP_GRADLE' > LiftApp/app/build.gradle
plugins {
    id 'com.android.application'
}

android {
    namespace 'com.example.myliftapp'
    compileSdk 34

    defaultConfig {
        applicationId "com.example.myliftapp"
        minSdk 24
        targetSdk 34
        versionCode 1
        versionName "1.0"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.9.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
}
APP_GRADLE

cat << 'MANIFEST' > LiftApp/app/src/main/AndroidManifest.xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.myliftapp">

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.LiftApp">
        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
</manifest>
MANIFEST

cat << 'STRINGS' > LiftApp/app/src/main/res/values/strings.xml
<resources>
    <string name="app_name">LiftApp</string>
</resources>
STRINGS

cat << 'COLORS' > LiftApp/app/src/main/res/values/colors.xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="black">#FF000000</color>
    <color name="white">#FFFFFFFF</color>
</resources>
COLORS

cat << 'THEMES' > LiftApp/app/src/main/res/values/themes.xml
<resources xmlns:tools="http://schemas.android.com/tools">
    <style name="Theme.LiftApp" parent="Theme.MaterialComponents.DayNight.NoActionBar">
        <!-- Customize your theme here. -->
    </style>
</resources>
THEMES

cat << 'WRAPPER' > LiftApp/gradle/wrapper/gradle-wrapper.properties
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-8.0-bin.zip
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
WRAPPER

echo "Scaffolding complete."
