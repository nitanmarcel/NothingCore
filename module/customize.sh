sdk_version=$(getprop ro.build.version.sdk)

cp -af "$MODPATH/$sdk_version"/* "$MODPATH/"

touch $MODPATH/a

find $MODPATH/
