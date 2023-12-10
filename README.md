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

## Licensing

The core source code, labeled as NothingCore, alongside specific components of the module, is released under the permissive MIT License. However, it's important to note that applications (apps), XML files, and fonts included in this project are property of their respective owners and are not covered by the MIT License. Each of these files may be subject to their own individual licenses or copyright terms.

The owners of these non-MIT-licensed files—apps, XML files, and fonts—retain full rights to their intellectual property. If you are an owner of any such file and object to its inclusion in this project, or if you believe the file should not be distributed within this project framework for any reason, you have the right to request its removal. We respect intellectual property rights and are committed to addressing such concerns promptly and in accordance with legal and ethical practices. To request the removal of your files, please reach out to us with valid proof of ownership and specific details about the files in question.
