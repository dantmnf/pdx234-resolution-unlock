/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.view;

import android.hardware.display.DeviceProductInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.DisplayMetrics;


/**
 * Describes the characteristics of a particular logical display.
 */
public final class DisplayInfo implements Parcelable {
    /**
     * The surface flinger layer stack associated with this logical display.
     */
    public int layerStack;

    /**
     * Display flags.
     */
    public int flags;

    /**
     * Display type.
     */
    public int type;

    /**
     * Logical display identifier.
     */
    public int displayId;

    /**
     * Display Group identifier.
     */
    public int displayGroupId;

    /**
     * Product-specific information about the display or the directly connected device on the
     * display chain. For example, if the display is transitively connected, this field may contain
     * product information about the intermediate device.
     */
    public DeviceProductInfo deviceProductInfo;

    /**
     * The human-readable name of the display.
     */
    public String name;

    /**
     * Unique identifier for the display. Shouldn't be displayed to the user.
     */
    public String uniqueId;

    /**
     * The width of the portion of the display that is available to applications, in pixels.
     * Represents the size of the display minus any system decorations.
     */
    public int appWidth;

    /**
     * The height of the portion of the display that is available to applications, in pixels.
     * Represents the size of the display minus any system decorations.
     */
    public int appHeight;

    /**
     * The smallest value of {@link #appWidth} that an application is likely to encounter,
     * in pixels, excepting cases where the width may be even smaller due to the presence
     * of a soft keyboard, for example.
     */
    public int smallestNominalAppWidth;

    /**
     * The smallest value of {@link #appHeight} that an application is likely to encounter,
     * in pixels, excepting cases where the height may be even smaller due to the presence
     * of a soft keyboard, for example.
     */
    public int smallestNominalAppHeight;

    /**
     * The largest value of {@link #appWidth} that an application is likely to encounter,
     * in pixels, excepting cases where the width may be even larger due to system decorations
     * such as the status bar being hidden, for example.
     */
    public int largestNominalAppWidth;

    /**
     * The largest value of {@link #appHeight} that an application is likely to encounter,
     * in pixels, excepting cases where the height may be even larger due to system decorations
     * such as the status bar being hidden, for example.
     */
    public int largestNominalAppHeight;

    /**
     * The logical width of the display, in pixels.
     * Represents the usable size of the display which may be smaller than the
     * physical size when the system is emulating a smaller display.
     */
    public int logicalWidth;

    /**
     * The logical height of the display, in pixels.
     * Represents the usable size of the display which may be smaller than the
     * physical size when the system is emulating a smaller display.
     */
    public int logicalHeight;

    /**
     * The {@link DisplayCutout} if present, otherwise {@code null}.
     *
     * @hide
     */
    // Remark on @UnsupportedAppUsage: Display.getCutout should be used instead
    public DisplayCutout displayCutout;

    /**
     * The rotation of the display relative to its natural orientation.
     * May be one of {@link android.view.Surface#ROTATION_0},
     * {@link android.view.Surface#ROTATION_90}, {@link android.view.Surface#ROTATION_180},
     * {@link android.view.Surface#ROTATION_270}.
     * <p>
     * The value of this field is indeterminate if the logical display is presented on
     * more than one physical display.
     * </p>
     */
    public int rotation;

    /**
     * The active display mode.
     */
    public int modeId;

    /**
     * The default display mode.
     */
    public int defaultModeId;

    /**
     * The supported modes of this display.
     */
    public Display.Mode[] supportedModes;

    /** The active color mode. */
    public int colorMode;

    /** The list of supported color modes */
    public int[] supportedColorModes;

    /** The display's HDR capabilities */
    public Display.HdrCapabilities hdrCapabilities;

    /** The formats disabled by user **/
    public int[] userDisabledHdrTypes;

    /**
     * Indicates whether the display can be switched into a mode with minimal post
     * processing.
     *
     * @see android.view.Display#isMinimalPostProcessingSupported
     */
    public boolean minimalPostProcessingSupported;

    /**
     * The logical display density which is the basis for density-independent
     * pixels.
     */
    public int logicalDensityDpi;

    /**
     * The exact physical pixels per inch of the screen in the X dimension.
     * <p>
     * The value of this field is indeterminate if the logical display is presented on
     * more than one physical display.
     * </p>
     */
    public float physicalXDpi;

    /**
     * The exact physical pixels per inch of the screen in the Y dimension.
     * <p>
     * The value of this field is indeterminate if the logical display is presented on
     * more than one physical display.
     * </p>
     */
    public float physicalYDpi;

    /**
     * This is a positive value indicating the phase offset of the VSYNC events provided by
     * Choreographer relative to the display refresh.  For example, if Choreographer reports
     * that the refresh occurred at time N, it actually occurred at (N - appVsyncOffsetNanos).
     */
    public long appVsyncOffsetNanos;

    /**
     * This is how far in advance a buffer must be queued for presentation at
     * a given time.  If you want a buffer to appear on the screen at
     * time N, you must submit the buffer before (N - bufferDeadlineNanos).
     */
    public long presentationDeadlineNanos;

    /**
     * The state of the display, such as {@link android.view.Display#STATE_ON}.
     */
    public int state;

    /**
     * The UID of the application that owns this display, or zero if it is owned by the system.
     * <p>
     * If the display is private, then only the owner can use it.
     * </p>
     */
    public int ownerUid;

    /**
     * The package name of the application that owns this display, or null if it is
     * owned by the system.
     * <p>
     * If the display is private, then only the owner can use it.
     * </p>
     */
    public String ownerPackageName;

    /**
     * The refresh rate override for this app. 0 means no override.
     */
    public float refreshRateOverride;

    /**
     * Get current remove mode of the display - what actions should be performed with the display's
     * content when it is removed.
     */
    // TODO (b/114338689): Remove the flag and use IWindowManager#getRemoveContentMode
    public int removeMode;

    /**
     * The current minimum brightness constraint of the display. Value between 0.0 and 1.0,
     * derived from the config constraints of the display device of this logical display.
     */
    public float brightnessMinimum;

    /**
     * @hide
     * The current maximum brightness constraint of the display. Value between 0.0 and 1.0,
     * derived from the config constraints of the display device of this logical display.
     */
    public float brightnessMaximum;

    /**
     * @hide
     * The current default brightness of the display. Value between 0.0 and 1.0,
     * derived from the configuration of the display device of this logical display.
     */
    public float brightnessDefault;

    /**
     * Install orientation of the display relative to its natural orientation.
     */
    public int installOrientation;

    public static final Creator<DisplayInfo> CREATOR = new Creator<DisplayInfo>() {
        @Override
        public DisplayInfo createFromParcel(Parcel source) {
            return null;
        }

        @Override
        public DisplayInfo[] newArray(int size) {
            return new DisplayInfo[0];
        }
    };

    public DisplayInfo() {
        throw new RuntimeException("Stub!");
    }

    public DisplayInfo(DisplayInfo other) {
        throw new RuntimeException("Stub!");
    }

    public void copyFrom(DisplayInfo other) {
        throw new RuntimeException("Stub!");
    }

    public void readFromParcel(Parcel source) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public int describeContents() {
        throw new RuntimeException("Stub!");
    }

    /**
     * Returns the refresh rate the application would experience.
     */
    public float getRefreshRate() {
        throw new RuntimeException("Stub!");
    }

    public Display.Mode getMode() {
        throw new RuntimeException("Stub!");
    }

    public Display.Mode getDefaultMode() {
        throw new RuntimeException("Stub!");
    }

    /**
     * Returns the id of the "default" mode with the given refresh rate, or {@code 0} if no suitable
     * mode could be found.
     */
    public Display.Mode findDefaultModeByRefreshRate(float refreshRate) {
        throw new RuntimeException("Stub!");
    }

    /**
     * Returns the list of supported refresh rates in the default mode.
     */
    public float[] getDefaultRefreshRates() {
        throw new RuntimeException("Stub!");
    }

    public void getAppMetrics(DisplayMetrics outMetrics) {
        throw new RuntimeException("Stub!");
    }

    public int getNaturalWidth() {
        throw new RuntimeException("Stub!");
    }

    public int getNaturalHeight() {
        throw new RuntimeException("Stub!");
    }

    public boolean isHdr() {
        throw new RuntimeException("Stub!");
    }

    public boolean isWideColorGamut() {
        throw new RuntimeException("Stub!");
    }

    /**
     * Returns true if the specified UID has access to this display.
     */
    public boolean hasAccess(int uid) {
        throw new RuntimeException("Stub!");
    }
}
