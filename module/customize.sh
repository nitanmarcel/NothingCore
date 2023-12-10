sdk_version=$(getprop ro.build.version.sdk)

ui_print "Installing for $sdk_version"

cp -af "$MODPATH/$sdk_version"/* "$MODPATH/"

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
cp -af $MODPATH/$sdk_version/overlay/* $MODPATH/$OVERLAY_DIR/ || true


ui_print "Installing nothing fonts"

FONTS_DIR="$MODPATH/system/fonts"

if [ -d "/system/product/fonts" ]; then
	FONTS_DIR="$MODPATH/system/product/fonts"
elif [ -d "/system/fonts" ]; then
	FONTS_DIR="$MODPATH/system/fonts"
elif [ -d "/vendor/fonts" ]; then
	FONTS_DIR="$MODPATH/system/vendor/fonts"
fi

mkdir -p $FONTS_DIR
cp -af $MODPATH/fonts/* $FONTS_DIR

read -r -d '' FONTS_XML <<"EOF"

    <!-- Nothing Fonts -->
    <family name="NDot55">
        <font weight="400" style="normal" postScriptName="Ndot-55">NDot55Caps.otf</font>
    </family>
    <family name="NDot57">
        <font weight="400" style="normal" postScriptName="Ndot-57">NDot57Caps.otf</font>
    </family>
    <family name="NDot57-Aligned">
        <font weight="400" style="normal" postScriptName="Ndot-57-Aligned">Ndot-57-Aligned.otf</font>
    </family>
    <alias name="ndot" to="NDot55" />
    <alias name="ndot57" to="NDot57" />
    <alias name="ndot57-align" to="NDot57-Aligned" />
    <family name="Lettera">
        <font weight="400" style="normal" postScriptName="LetteraMonoLL-Regular">LetteraMonoLL-Regular.otf</font>
        <font weight="400" style="italic">LetteraMonoLL-Italic.otf</font>
        <font weight="300" style="normal">LetteraMonoLL-Light.otf</font>
        <font weight="300" style="italic">LetteraMonoLL-LightItalic.otf</font>
        <font weight="500" style="normal">LetteraMonoLL-Medium.otf</font>
        <font weight="500" style="italic">LetteraMonoLL-MediumItalic.otf</font>
    </family>
    <alias name="Lettera-light" to="Lettera" weight="300" />
    <alias name="Lettera-medium" to="Lettera" weight="500" />
EOF

if [ -f "/system/etc/fonts.xml" ]; then
    mkdir -p "$MODPATH/system/etc/"

    XML="$MODPATH/fonts.xml"
    if [ ! -f "$XML" ]; then
	    cp -af "/system/etc/fonts.xml" "$XML"
    fi

    cp -af "$XML" "$MODPATH/system/etc/fonts.xml"
    XML="$MODPATH/system/etc/fonts.xml"
    
    awk -v var="$FONTS_XML" \
        '/<\/familyset>/{print var; next}1' "/system/etc/fonts.xml" \
        > "$XML"
    echo "</familyset>" >> "$XML"
fi

if [ -f "/product/etc/fonts_customization.xml" ]; then
    mkdir -p "$MODPATH/system/product/etc"
    
    XML="$MODPATH/fonts_customization.xml"
    if [ ! -f "$XML" ]; then            
            cp -af "/product/etc/fonts_customization.xml" "$XML"
    fi

    cp -af "$XML" "$MODPATH/system/product/etc/fonts_customization.xml"
    XML="$MODPATH/system/product/etc/fonts_customization.xml"

    awk -v var="$FONTS_XML" \
        '/<\/fonts-modification>/{print var; next}1' "/product/etc/fonts_customization.xml" \
        > "$XML"
    echo "</fonts-modification>" >> "$XML"
fi

