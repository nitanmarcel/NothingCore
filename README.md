# Nothing Core

Install Nothing apps on any device.

## Requirements

- Magisk/KernelSU.
- LSPosed (or any xposed alternative).

## Installation

- Flash via Magisk/KernelSU.
- Reboot.
- Enable NothingCore module for both Nothing Launcher and Weather application.
- Update all the Nothing apps to the latest version via PlayStore.
  - Clear PlayStore cache if you can't see NothingOS apps.
  - Some apps can't be installed from PlayStore, use apkmirror instead.

What's working:
- Everything except the Camera app.
- If an app is not working try to enable the NothingCore module for that application.

## Compiling

- Build the NothingCore project
  - To use DT2Sleep in Nothing Launcher you need to sign the output apk with the platform [test-keys](https://stackoverflow.com/questions/37586255/signing-my-android-application-as-system-app)
- Copy the output apk to module/system/priv-app/NothingCore/NothingCore.apk
- Copy the output apk to module/system/framework/not-a-framework.jar
- Zip contents inside module folder and follow the installation instructions above
