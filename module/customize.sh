sdk_version=$(getprop ro.build.version.sdk)

ui_print "SDK: $sdk_version"


ui_print "cp -af $MODPATH/$sdk_version/* $MODPATH/"

cp -af "$MODPATH/$sdk_version"/* "$MODPATH/"

touch $MODPATH/a

find $MODPATH/
