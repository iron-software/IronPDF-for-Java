package com.ironsoftware.ironpdf.internal.staticapi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SettingApiTests {

    @Test
    public final void IsArm64DetectsAppleSiliconTest() {
        // JVMs on Apple Silicon report "aarch64"
        Assertions.assertTrue(Setting_Api.isArm64("aarch64"));
        Assertions.assertTrue(Setting_Api.isArm64("AARCH64"));
        Assertions.assertTrue(Setting_Api.isArm64("arm64"));
    }

    @Test
    public final void IsArm64RejectsIntelTest() {
        Assertions.assertFalse(Setting_Api.isArm64("x86_64"));
        Assertions.assertFalse(Setting_Api.isArm64("amd64"));
        Assertions.assertFalse(Setting_Api.isArm64("x86"));
        Assertions.assertFalse(Setting_Api.isArm64(null));
    }

    @Test
    public final void IsArm64Rejects32BitArmTest() {
        Assertions.assertFalse(Setting_Api.isArm64("arm"));
        Assertions.assertFalse(Setting_Api.isArm64("armv7l"));
    }
}
