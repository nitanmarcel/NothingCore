# Nothing Core

Install Nothing apps on any device.

## Requirements

- Magisk/KernelSU.
- LSPosed (or any xposed alternative).

## Installation

- AOSP based ROM. OEM ROMs might not be able to run the launcher. Same goes for a few custom ROMs.
- Flash via Magisk/KernelSU.
   - Official Magisk or KernelSU is recommended
- Reboot.
- Enable NothingCore module for Nothing Launcher, Weather and System framework.
- Reboot one more time to fix launcher permissions.
- Update all the Nothing apps to the latest version via PlayStore.
  - Clear PlayStore cache if you can't see NothingOS apps.
  - Some apps can't be installed from PlayStore, use apkmirror instead.

What's working:
- Everything except the Camera app.
- If an app is not working try to enable the NothingCore module for that application.
- This module doesn't replace/add any new fonts. Some apps might use your default system font.

## Compiling

- Build the NothingCore project
  - To use DT2Sleep in Nothing Launcher you need to sign the output apk with the platform [test-keys](https://stackoverflow.com/questions/37586255/signing-my-android-application-as-system-app)
- Copy the output apk to module/system/priv-app/NothingCore/NothingCore.apk
- Copy the output apk to module/system/framework/not-a-framework.jar
- Zip contents inside module folder and follow the installation instructions above
