# PDX234 Screen Resolution Unlock

This project contains Magisk and Xposed modules to unlock the screen resolution on the Sony Xperia 1 V device.

Both module need to be installed and enabled to unlock the screen resolution.

## Background

* Sony has set `config_maxUiWidth` to 1096 that limits maximum UI resolution.
  - Fix shipped in Magisk module.
* Reboot persistence of user preferred display mode is applied before `SettingsProvider` comes up, where the user preferred display mode is stored.
  - Fix shipped in Magisk module, also fixed in AOSP master/main branch.
* The AOSP resolution setting is hardcoded to switch between 1080p and 1440p.
  - Fix shipped in Xposed module and Magisk module.
* Some app assumes `DisplayInfo.mSupportedModes[0]` is the current active mode, which is not always true.
  - Fix shipped in Xposed module.

## Build Magisk module

Run `gradlew :magisk:packageMagisk`
