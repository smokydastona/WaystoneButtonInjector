/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.InputConstants
 *  com.mojang.blaze3d.platform.InputConstants$Key
 *  com.mojang.blaze3d.platform.InputConstants$Type
 *  net.minecraft.client.KeyMapping
 *  net.minecraftforge.client.event.RegisterKeyMappingsEvent
 *  net.minecraftforge.client.settings.IKeyConflictContext
 *  net.minecraftforge.client.settings.KeyConflictContext
 *  net.minecraftforge.client.settings.KeyModifier
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.balm.forge.client.keymappings;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.ArrayList;
import java.util.List;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.keymappings.KeyModifiers;
import net.blay09.mods.balm.common.NamespaceResolver;
import net.blay09.mods.balm.common.StaticNamespaceResolver;
import net.blay09.mods.balm.common.client.keymappings.CommonBalmKeyMappings;
import net.blay09.mods.balm.forge.ModBusEventRegisters;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.Nullable;

public class ForgeBalmKeyMappings
extends CommonBalmKeyMappings {
    public ForgeBalmKeyMappings(NamespaceResolver namespaceResolver) {
        super(namespaceResolver);
    }

    private static IKeyConflictContext toForge(net.blay09.mods.balm.api.client.keymappings.KeyConflictContext context) {
        return switch (context) {
            default -> throw new IncompatibleClassChangeError();
            case net.blay09.mods.balm.api.client.keymappings.KeyConflictContext.UNIVERSAL -> KeyConflictContext.UNIVERSAL;
            case net.blay09.mods.balm.api.client.keymappings.KeyConflictContext.GUI -> KeyConflictContext.GUI;
            case net.blay09.mods.balm.api.client.keymappings.KeyConflictContext.INGAME -> KeyConflictContext.IN_GAME;
        };
    }

    private static KeyModifier toForge(net.blay09.mods.balm.api.client.keymappings.KeyModifier modifier) {
        return switch (modifier) {
            case net.blay09.mods.balm.api.client.keymappings.KeyModifier.SHIFT -> KeyModifier.SHIFT;
            case net.blay09.mods.balm.api.client.keymappings.KeyModifier.CONTROL -> KeyModifier.CONTROL;
            case net.blay09.mods.balm.api.client.keymappings.KeyModifier.ALT -> KeyModifier.ALT;
            default -> KeyModifier.NONE;
        };
    }

    @Override
    public KeyMapping registerKeyMapping(String name, net.blay09.mods.balm.api.client.keymappings.KeyConflictContext conflictContext, net.blay09.mods.balm.api.client.keymappings.KeyModifier modifier, InputConstants.Type type, int keyCode, String category) {
        KeyMapping keyMapping = new KeyMapping(name, ForgeBalmKeyMappings.toForge(conflictContext), ForgeBalmKeyMappings.toForge(modifier), type, keyCode, category);
        this.getActiveRegistrations().keyMappings.add(keyMapping);
        return keyMapping;
    }

    @Override
    public KeyMapping registerKeyMapping(String name, net.blay09.mods.balm.api.client.keymappings.KeyConflictContext conflictContext, KeyModifiers modifiers, InputConstants.Type type, int keyCode, String category) {
        List<net.blay09.mods.balm.api.client.keymappings.KeyModifier> keyModifiers = modifiers.asList();
        net.blay09.mods.balm.api.client.keymappings.KeyModifier mainModifier = !keyModifiers.isEmpty() ? keyModifiers.get(0) : net.blay09.mods.balm.api.client.keymappings.KeyModifier.NONE;
        KeyMapping keyMapping = new KeyMapping(name, ForgeBalmKeyMappings.toForge(conflictContext), ForgeBalmKeyMappings.toForge(mainModifier), type, keyCode, category);
        this.getActiveRegistrations().keyMappings.add(keyMapping);
        if (keyModifiers.size() > 1) {
            this.registerModifierKeyMappings(keyMapping, conflictContext, keyModifiers.subList(1, keyModifiers.size()));
        }
        if (modifiers.hasCustomModifiers()) {
            this.registerCustomModifierKeyMappings(keyMapping, conflictContext, modifiers.getCustomModifiers());
        }
        return keyMapping;
    }

    @Override
    public boolean isActiveAndMatches(@Nullable KeyMapping keyMapping, InputConstants.Key input) {
        return this.isActive(keyMapping) && keyMapping.isActiveAndMatches(input);
    }

    @Override
    public BalmKeyMappings scoped(String modId) {
        return new ForgeBalmKeyMappings(new StaticNamespaceResolver(modId));
    }

    @Override
    public boolean isActiveAndMatches(@Nullable KeyMapping keyMapping, int keyCode, int scanCode) {
        return this.isActive(keyMapping) && keyMapping.isActiveAndMatches(InputConstants.m_84827_((int)keyCode, (int)scanCode));
    }

    @Override
    public boolean isActiveAndMatches(@Nullable KeyMapping keyMapping, InputConstants.Type type, int keyCode, int scanCode) {
        if (!this.isActive(keyMapping)) {
            return false;
        }
        return type == InputConstants.Type.MOUSE ? keyMapping.isActiveAndMatches(InputConstants.Type.MOUSE.m_84895_(keyCode)) : keyMapping.isActiveAndMatches(InputConstants.m_84827_((int)keyCode, (int)scanCode));
    }

    private boolean isActiveAndMatchesStrictModifier(@Nullable KeyMapping keyMapping, int keyCode, int scanCode) {
        if (!this.isActive(keyMapping)) {
            return false;
        }
        if (keyMapping.getKeyModifier() == KeyModifier.NONE && (KeyModifier.SHIFT.isActive(keyMapping.getKeyConflictContext()) || KeyModifier.CONTROL.isActive(keyMapping.getKeyConflictContext()) || KeyModifier.ALT.isActive(keyMapping.getKeyConflictContext()))) {
            return false;
        }
        return keyMapping.m_90832_(keyCode, scanCode);
    }

    @Override
    protected boolean isContextActive(KeyMapping keyMapping) {
        return keyMapping.getKeyConflictContext().isActive();
    }

    private Registrations getActiveRegistrations() {
        return ModBusEventRegisters.getRegistrations(this.namespaceResolver.getDefaultNamespace(), Registrations.class);
    }

    public static class Registrations {
        public final List<KeyMapping> keyMappings = new ArrayList<KeyMapping>();

        @SubscribeEvent
        public void registerKeyMappings(RegisterKeyMappingsEvent event) {
            this.keyMappings.forEach(arg_0 -> ((RegisterKeyMappingsEvent)event).register(arg_0));
        }
    }
}

