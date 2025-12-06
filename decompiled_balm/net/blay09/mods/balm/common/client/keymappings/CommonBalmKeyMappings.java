/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  com.mojang.blaze3d.platform.InputConstants
 *  com.mojang.blaze3d.platform.InputConstants$Key
 *  com.mojang.blaze3d.platform.InputConstants$Type
 *  net.minecraft.client.KeyMapping
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screens.Screen
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.balm.common.client.keymappings;

import com.google.common.collect.Sets;
import com.mojang.blaze3d.platform.InputConstants;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.keymappings.KeyConflictContext;
import net.blay09.mods.balm.api.client.keymappings.KeyModifier;
import net.blay09.mods.balm.api.client.keymappings.KeyModifiers;
import net.blay09.mods.balm.common.NamespaceResolver;
import net.blay09.mods.balm.mixin.KeyMappingAccessor;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public abstract class CommonBalmKeyMappings
implements BalmKeyMappings {
    protected final NamespaceResolver namespaceResolver;
    private static final Set<KeyMapping> ignoreConflicts = Sets.newConcurrentHashSet();
    private static final Map<KeyMapping, Set<KeyMapping>> multiModifierKeyMappings = new ConcurrentHashMap<KeyMapping, Set<KeyMapping>>();

    public CommonBalmKeyMappings(NamespaceResolver namespaceResolver) {
        this.namespaceResolver = namespaceResolver;
    }

    @Override
    public KeyMapping registerKeyMapping(String name, int keyCode, String category) {
        return this.registerKeyMapping(name, KeyConflictContext.UNIVERSAL, KeyModifier.NONE, InputConstants.Type.KEYSYM, keyCode, category);
    }

    @Override
    public KeyMapping registerKeyMapping(String name, InputConstants.Type type, int keyCode, String category) {
        return this.registerKeyMapping(name, KeyConflictContext.UNIVERSAL, KeyModifier.NONE, type, keyCode, category);
    }

    @Override
    public KeyMapping registerKeyMapping(String name, KeyConflictContext conflictContext, KeyModifier modifier, int keyCode, String category) {
        return this.registerKeyMapping(name, conflictContext, modifier, InputConstants.Type.KEYSYM, keyCode, category);
    }

    @Override
    public KeyMapping registerKeyMapping(String name, KeyConflictContext conflictContext, KeyModifiers modifiers, int keyCode, String category) {
        return this.registerKeyMapping(name, conflictContext, modifiers, InputConstants.Type.KEYSYM, keyCode, category);
    }

    protected void registerModifierKeyMappings(KeyMapping baseMapping, KeyConflictContext conflictContext, List<KeyModifier> keyModifiers) {
        for (int i = 0; i < keyModifiers.size(); ++i) {
            String subName = i > 0 ? baseMapping.m_90860_() + "_modifier_" + i : baseMapping.m_90860_() + "_modifier";
            KeyMapping subKeyMapping = this.registerKeyMapping(subName, conflictContext, KeyModifier.NONE, InputConstants.Type.KEYSYM, this.toKeyCode(keyModifiers.get(i)), baseMapping.m_90858_());
            multiModifierKeyMappings.computeIfAbsent(baseMapping, it -> new HashSet()).add(subKeyMapping);
            ignoreConflicts.add(subKeyMapping);
        }
    }

    protected void registerCustomModifierKeyMappings(KeyMapping baseMapping, KeyConflictContext conflictContext, List<InputConstants.Key> keyModifiers) {
        for (int i = 0; i < keyModifiers.size(); ++i) {
            String subName = i > 0 ? baseMapping.m_90860_() + "_modifier_" + i : baseMapping.m_90860_() + "_modifier";
            KeyMapping subKeyMapping = this.registerKeyMapping(subName, conflictContext, KeyModifier.NONE, InputConstants.Type.KEYSYM, keyModifiers.get(i).m_84873_(), baseMapping.m_90858_());
            multiModifierKeyMappings.computeIfAbsent(baseMapping, it -> new HashSet()).add(subKeyMapping);
            ignoreConflicts.add(subKeyMapping);
        }
    }

    private int toKeyCode(KeyModifier keyModifier) {
        return switch (keyModifier) {
            case KeyModifier.SHIFT -> 340;
            case KeyModifier.CONTROL -> 341;
            case KeyModifier.ALT -> 342;
            default -> -1;
        };
    }

    protected boolean areModifiersActive(KeyMapping keyMapping) {
        Set modifierMappings = multiModifierKeyMappings.getOrDefault(keyMapping, Collections.emptySet());
        for (KeyMapping modifierMapping : modifierMappings) {
            if ((modifierMapping.m_90832_(340, 0) || modifierMapping.m_90832_(344, 0)) && Screen.m_96638_() || (modifierMapping.m_90832_(341, 0) || modifierMapping.m_90832_(345, 0)) && Screen.m_96637_() || (modifierMapping.m_90832_(342, 0) || modifierMapping.m_90832_(346, 0)) && Screen.m_96639_() || this.isActiveAndKeyDown(modifierMapping)) continue;
            return false;
        }
        return true;
    }

    @Override
    public boolean isActiveAndKeyDown(@Nullable KeyMapping keyMapping) {
        if (!this.isActive(keyMapping)) {
            return false;
        }
        InputConstants.Key key = ((KeyMappingAccessor)keyMapping).getKey();
        return keyMapping.m_90857_() || key.m_84873_() != -1 && key.m_84868_() == InputConstants.Type.KEYSYM && InputConstants.m_84830_((long)Minecraft.m_91087_().m_91268_().m_85439_(), (int)key.m_84873_());
    }

    @Override
    public boolean isKeyDownIgnoreContext(@Nullable KeyMapping keyMapping) {
        if (!this.isActiveIgnoreContext(keyMapping)) {
            return false;
        }
        InputConstants.Key key = ((KeyMappingAccessor)keyMapping).getKey();
        return keyMapping.m_90857_() || key.m_84873_() != -1 && key.m_84868_() == InputConstants.Type.KEYSYM && InputConstants.m_84830_((long)Minecraft.m_91087_().m_91268_().m_85439_(), (int)key.m_84873_());
    }

    @Override
    public boolean isActiveAndWasPressed(@Nullable KeyMapping keyMapping) {
        return this.isActive(keyMapping) && keyMapping.m_90859_();
    }

    @Contract(value="null -> false")
    protected boolean isActive(@Nullable KeyMapping keyMapping) {
        if (keyMapping == null) {
            return false;
        }
        return this.isContextActive(keyMapping) && this.areModifiersActive(keyMapping);
    }

    @Contract(value="null -> false")
    protected boolean isActiveIgnoreContext(@Nullable KeyMapping keyMapping) {
        if (keyMapping == null) {
            return false;
        }
        return this.areModifiersActive(keyMapping);
    }

    @Override
    public boolean isActiveAndMatches(@Nullable KeyMapping keyMapping, InputConstants.Key input) {
        if (!this.isActive(keyMapping)) {
            return false;
        }
        return input.m_84868_() == InputConstants.Type.MOUSE ? keyMapping.m_90830_(input.m_84873_()) : keyMapping.m_90832_(input.m_84868_() == InputConstants.Type.KEYSYM ? input.m_84873_() : InputConstants.f_84822_.m_84873_(), input.m_84868_() == InputConstants.Type.SCANCODE ? input.m_84873_() : InputConstants.f_84822_.m_84873_());
    }

    @Override
    public boolean isActiveAndMatches(@Nullable KeyMapping keyMapping, int keyCode, int scanCode) {
        return this.isActive(keyMapping) && keyMapping.m_90832_(keyCode, scanCode);
    }

    @Override
    public boolean isActiveAndMatches(@Nullable KeyMapping keyMapping, InputConstants.Type type, int keyCode, int scanCode) {
        return this.isActive(keyMapping) && (type == InputConstants.Type.MOUSE ? keyMapping.m_90830_(keyCode) : keyMapping.m_90832_(keyCode, scanCode));
    }

    @Override
    public Optional<Boolean> conflictsWith(KeyMapping first, KeyMapping second) {
        if (ignoreConflicts.contains(first) || ignoreConflicts.contains(second)) {
            return Optional.of(false);
        }
        return Optional.empty();
    }

    @Override
    public void ignoreConflicts(KeyMapping keyMapping) {
        ignoreConflicts.add(keyMapping);
        ignoreConflicts.addAll(multiModifierKeyMappings.getOrDefault(keyMapping, Collections.emptySet()));
    }

    @Override
    public boolean shouldIgnoreConflicts(KeyMapping keyMapping) {
        return ignoreConflicts.contains(keyMapping);
    }

    protected abstract boolean isContextActive(KeyMapping var1);

    protected boolean isContextActive(KeyConflictContext conflictContext) {
        return switch (conflictContext) {
            case KeyConflictContext.GUI -> {
                if (Minecraft.m_91087_().f_91080_ != null) {
                    yield true;
                }
                yield false;
            }
            case KeyConflictContext.INGAME -> {
                if (Minecraft.m_91087_().f_91080_ == null) {
                    yield true;
                }
                yield false;
            }
            default -> true;
        };
    }
}

