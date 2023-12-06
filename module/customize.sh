sdk_version=$(getprop ro.build.version.sdk)

ui_print "Installing for $sdk_version"

cp -af "$MODPATH/$sdk_version"/* "$MODPATH/"
