ui_print "Installing NothingOSConfigOverlay"

OVERLAY_DIR="/system/vendor/overlay"

if [ -d "/system/vendor/overlay" ]; then
    OVERLAY_DIR="/system/vendor/overlay"
elif [ -d "/system/product/overlay" ]; then
    OVERLAY_DIR="/system/product/overlay"
elif [ -d "/system/system_ext/overlay" ]; then
    OVERLAY_DIR="/system/system_ext/overlay"
else
    echo "Could not find a suitable overlay directory."
    exit 1
fi

ui_print "Installing overlay in $OVERLAY_DIR"

mkdir -p $MODPATH/$OVERLAY_DIR
cp -af $MODPATH/overlay/* $MODPATH/$OVERLAY_DIR/

