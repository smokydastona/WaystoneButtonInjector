/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.InputConstants$Key
 *  com.mojang.blaze3d.platform.InputConstants$Type
 */
package net.blay09.mods.balm.api.client.keymappings;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import net.blay09.mods.balm.api.client.keymappings.KeyModifier;

public class KeyModifiers {
    private final EnumSet<KeyModifier> modifiers = EnumSet.noneOf(KeyModifier.class);
    private final List<InputConstants.Key> customModifiers = new ArrayList<InputConstants.Key>();

    private KeyModifiers(KeyModifier ... modifiers) {
        this.modifiers.addAll(Arrays.asList(modifiers));
    }

    public static KeyModifiers of(KeyModifier ... modifiers) {
        return new KeyModifiers(modifiers);
    }

    public static KeyModifiers ofCustom(InputConstants.Key ... modifiers) {
        KeyModifiers keyModifiers = new KeyModifiers(new KeyModifier[0]);
        for (InputConstants.Key modifier : modifiers) {
            keyModifiers.addCustomModifier(modifier);
        }
        return keyModifiers;
    }

    public boolean contains(KeyModifier keyModifier) {
        return this.modifiers.contains((Object)keyModifier);
    }

    public int size() {
        return this.modifiers.size();
    }

    public boolean isEmpty() {
        return this.modifiers.isEmpty();
    }

    public KeyModifiers addCustomModifier(InputConstants.Key key) {
        this.customModifiers.add(key);
        return this;
    }

    public KeyModifiers addCustomModifier(int keyCode) {
        this.customModifiers.add(InputConstants.Type.KEYSYM.m_84895_(keyCode));
        return this;
    }

    public List<InputConstants.Key> getCustomModifiers() {
        return this.customModifiers;
    }

    public List<KeyModifier> asList() {
        return new ArrayList<KeyModifier>(this.modifiers);
    }

    public boolean hasCustomModifiers() {
        return !this.customModifiers.isEmpty();
    }
}

