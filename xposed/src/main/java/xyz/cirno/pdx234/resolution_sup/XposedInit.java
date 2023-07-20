package xyz.cirno.pdx234.resolution_sup;

import android.content.Context;
import android.os.Binder;
import android.provider.Settings;
import android.view.Display;
import android.view.DisplayInfo;

import java.util.Arrays;
import java.util.Comparator;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class XposedInit implements IXposedHookLoadPackage{
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if ("android".equals(lpparam.packageName)) {
            handleLoadSystemServer(lpparam);
        } else if ("com.android.settings".equals(lpparam.packageName)) {
            handleLoadSettings(lpparam);
        }
    }

    private void handleLoadSettings(XC_LoadPackage.LoadPackageParam lpparam) {
        var screenResolutionControllerClass = XposedHelpers.findClass("com.android.settings.display.ScreenResolutionController", lpparam.classLoader);
        XposedHelpers.findAndHookMethod(screenResolutionControllerClass, "getAvailabilityStatus", new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                // com.android.settings.core.BasePreferenceController.AVAILABLE
                return 0;
            }
        });
        XposedHelpers.findAndHookMethod(screenResolutionControllerClass, "getDisplayWidth", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (param.hasThrowable()) return;
                final var width = (int) param.getResult();
                if (width == 1644) {
                    param.setResult(1440);
                } else if (width == 1096) {
                    param.setResult(1080);
                }
            }
        });

        final var screenResolutionFragmentClass = XposedHelpers.findClass("com.android.settings.display.ScreenResolutionFragment", lpparam.classLoader);
        final var getPreferModeMethod = XposedHelpers.findMethodExact(screenResolutionFragmentClass, "getPreferMode", int.class);
        XposedHelpers.findAndHookMethod(screenResolutionFragmentClass, "getKeyForResolution", int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                final var width = (int) param.args[0];
                if (width == 1096) {
                    param.args[0] = 1080;
                } else if (width == 1644) {
                    param.args[0] = 1440;
                }
            }
        });
        XposedHelpers.findAndHookMethod(screenResolutionFragmentClass, "setDisplayMode", int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                final var width = (int) param.args[0];
                if (width == 1080) {
                    param.args[0] = 1096;
                } else if (width == 1440) {
                    param.args[0] = 1644;
                }
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                final var context = (Context) XposedHelpers.callMethod(param.thisObject, "getContext");
                final var resolver = context.getContentResolver();
                final var mode = (Display.Mode) getPreferModeMethod.invoke(param.thisObject, param.args[0]);
                Settings.Global.putInt(resolver, "user_preferred_resolution_width", mode.getPhysicalWidth());
                Settings.Global.putInt(resolver, "user_preferred_resolution_height", mode.getPhysicalHeight());
                Settings.Global.putFloat(resolver, "user_preferred_refresh_rate", mode.getRefreshRate());
            }
        });
    }

    private void handleLoadSystemServer(XC_LoadPackage.LoadPackageParam lpparam) {
        final var dmsBinderServiceClass = XposedHelpers.findClass("com.android.server.display.DisplayManagerService$BinderService", lpparam.classLoader);

        XposedHelpers.findAndHookMethod(dmsBinderServiceClass, "getDisplayInfo", int.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                final var displayId = (int) param.args[0];
                final var callingUid = Binder.getCallingUid();
                if (displayId == 0 && callingUid >= 10000) {
                    // calling from user app for internal display
                    // filter inactive modes to workaround buggy apps that assumes the first mode is the active mode
                    var info = (DisplayInfo) param.getResult();
                    if (info == null) return;
                    final var modeId = info.modeId;
                    final var supportedModes = info.supportedModes;
                    var activeMode = Arrays.stream(supportedModes).filter(m -> m.getModeId() == modeId).findFirst().orElse(null);
                    var firstMode = supportedModes.length > 0 ? supportedModes[0] : null;
                    if (firstMode == null || activeMode == null) return;
                    final var activeWidth = activeMode.getPhysicalWidth();
                    final var activeHeight = activeMode.getPhysicalHeight();
                    if (firstMode.getPhysicalWidth() != activeWidth || firstMode.getPhysicalHeight() != activeHeight) {
                        var newinfo = new DisplayInfo(info);
                        newinfo.supportedModes = Arrays.stream(supportedModes)
                                .sorted(Comparator.comparing(m -> (m.getModeId() == modeId) ? 0 : 1))
                                .toArray(Display.Mode[]::new);
                        param.setResult(newinfo);
                    }
                }
            }
        });
    }
}
