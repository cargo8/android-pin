Android-Pin
===========

An easy drop-in PIN controller for Android

![demo_gif](https://github.com/venmo/android-pin/blob/master/demo/pindemo.gif)

Usage
=====
[`PinFragment`](https://github.com/venmo/android-pin/blob/master/library/src/main/java/com/venmo/android/pin/PinFragment.java) is the primary class provided by this library. A `PinFragment` should be instantiated either for a PIN creation, or for PIN validation. 

```java
Fragment toShow = PinHelper.hasDefaultPinSaved(this) ?
                PinFragment.newInstanceForVerification() : 
                PinFragment.newInstanceForCreation();

getFragmentManager().beginTransaction()
                .replace(android.R.id.container, toShow)
                .commit();
```

A hosting `Activity` should implement `PinFragment.Listener` to perform actions when a PIN has been created or validated. 

```java
public interface Listener {
    public void onValidated();
    public void onPinCreated();
}
```

By default, a user's PIN is saved in the default `SharedPreference` store, and validated against that store. To override this behavior, instantiate your `PinFragment` with a `PinFragmentConfiguration` that provides custom saving and validating logic. An example of a configuration might be

```java
PinFragmentConfiguration config = 
    new PinFragmentConfiguration(context)
      .pinSaver(new PinSaver(){
          public void onSave(String pin) {
              // ...do some saving
          }
      }).validator(new Validator(){
          public boolean isValid(String submission){
              boolean valid = // ...check against where you saved the pin
              return valid;
          }
        });
```

Then, instantiation of your `PinFragment` might look like this

```java
Fragment toShow = doesHavePinSavedSomewhere() ?
                PinFragment.newInstanceForVerification(config) :
                PinFragment.newInstanceForCreation(config);

getFragmentManager().beginTransaction()
                .replace(android.R.id.container, toShow)
                .commit();
```

In general, any time a custom `PinSaver` is defined, it should follow that a custom `Validator` is also defined. This may be more strictly enforced in the future.

Asynchronous Handling
=====================
A very common use case for providing an alternative `Validator` and `PinSaver` is if you persist a user's PIN remotely and validate by making a request to your server. In this case, `PinFragment` can execute your saving and checking on a background thread and show a `ProgressBar` while executing. To utilize this, pass implementations of `AsyncSaver` and `AsyncValidator` to your configuration.

```java
PinFragmentConfiguration config = 
  new PinFragmentConfiguration(context)
    .pinSaver(new AsyncPinSaver(){
        public void onSave(String pin){
            MyApi api = //...
            api.savePin(pin);
        }
    }).validator(new AsyncValidator(){
        public boolean isValid(String submission){
            MyApi api = ...
            boolean valid = api.comparePin(submission);
            return valid;
      }
    });
```

This configuration will instruct your `PinFragment` instance to run `onSave()` and `isValid()` in the background and post to your `PinFragment.Listener` only when a PIN has been successfully created or verified, meaning you don't need to think about scheduling things to happen in the background.

Including in your project
=========================

This library is hosted on Maven Central. For gradle builds, add the following to your `build.gradle`

```groovy
repositories {
    mavenCentral()
}

dependencies {
    compile 'com.venmo.android.pin:library:0.1@aar'
}
```

Contributing
=============
Contributions are encouraged! If you would like to contribute, fork this repository and send a pull request. Please make sure to follow the project code style if possible. The project file `.idea/codeStyleSettings.xml` is provided for your convenience in Android Studio/IntelliJ.

