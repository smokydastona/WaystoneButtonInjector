/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.InputConstants
 *  com.mojang.blaze3d.platform.InputConstants$Key
 *  com.mojang.blaze3d.platform.InputConstants$Type
 *  net.minecraft.client.KeyMapping
 */
package net.blay09.mods.balm.api.client.keymappings;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.Optional;
import net.blay09.mods.balm.api.client.keymappings.KeyConflictContext;
import net.blay09.mods.balm.api.client.keymappings.KeyModifier;
import net.blay09.mods.balm.api.client.keymappings.KeyModifiers;
import net.minecraft.client.KeyMapping;

public interface BalmKeyMappings {
    default public KeyMapping registerKeyMapping(String name, int keyCode, String category) {
        return this.registerKeyMapping(name, InputConstants.Type.KEYSYM, keyCode, category);
    }

    public KeyMapping registerKeyMapping(String var1, InputConstants.Type var2, int var3, String var4);

    @Deprecated(forRemoval=true, since="1.21.5")
    public KeyMapping registerKeyMapping(String var1, KeyConflictContext var2, KeyModifier var3, int var4, String var5);

    @Deprecated(forRemoval=true, since="1.21.5")
    public KeyMapping registerKeyMapping(String var1, KeyConflictContext var2, KeyModifiers var3, int var4, String var5);

    @Deprecated(forRemoval=true, since="1.21.5")
    public KeyMapping registerKeyMapping(String var1, KeyConflictContext var2, KeyModifier var3, InputConstants.Type var4, int var5, String var6);

    @Deprecated(forRemoval=true, since="1.21.5")
    public KeyMapping registerKeyMapping(String var1, KeyConflictContext var2, KeyModifiers var3, InputConstants.Type var4, int var5, String var6);

    @Deprecated(forRemoval=true, since="1.21.5")
    default public boolean isActiveAndMatches(KeyMapping keyMapping, int keyCode, int scanCode) {
        return this.isActiveAndMatches(keyMapping, InputConstants.m_84827_((int)keyCode, (int)scanCode));
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    default public boolean isActiveAndMatches(KeyMapping keyMapping, InputConstants.Type type, int keyCode, int scanCode) {
        return this.isActiveAndMatches(keyMapping, type.m_84895_(type == InputConstants.Type.SCANCODE ? scanCode : keyCode));
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    public boolean isActiveAndMatches(KeyMapping var1, InputConstants.Key var2);

    @Deprecated(forRemoval=true, since="1.21.5")
    public boolean isActiveAndWasPressed(KeyMapping var1);

    @Deprecated(forRemoval=true, since="1.21.5")
    public boolean isKeyDownIgnoreContext(KeyMapping var1);

    @Deprecated(forRemoval=true, since="1.21.5")
    public boolean isActiveAndKeyDown(KeyMapping var1);

    @Deprecated(forRemoval=true, since="1.21.5")
    public Optional<Boolean> conflictsWith(KeyMapping var1, KeyMapping var2);

    @Deprecated(forRemoval=true, since="1.21.5")
    public void ignoreConflicts(KeyMapping var1);

    @Deprecated(forRemoval=true, since="1.21.5")
    public boolean shouldIgnoreConflicts(KeyMapping var1);

    public BalmKeyMappings scoped(String var1);
}

