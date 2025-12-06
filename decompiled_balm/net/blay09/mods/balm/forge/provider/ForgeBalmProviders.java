/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Direction
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraftforge.common.capabilities.Capability
 *  net.minecraftforge.common.capabilities.CapabilityManager
 *  net.minecraftforge.common.capabilities.CapabilityToken
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.balm.forge.provider;

import java.util.HashMap;
import java.util.Map;
import net.blay09.mods.balm.api.provider.BalmProviders;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import org.jetbrains.annotations.Nullable;

public class ForgeBalmProviders
implements BalmProviders {
    private final Map<Class<?>, Capability<?>> capabilities = new HashMap();

    @Override
    public <T> T getProvider(BlockEntity blockEntity, Class<T> clazz) {
        return this.getProvider(blockEntity, null, clazz);
    }

    @Override
    public <T> T getProvider(BlockEntity blockEntity, @Nullable Direction direction, Class<T> clazz) {
        Capability<T> capability = this.getCapability(clazz);
        return blockEntity.getCapability(capability, direction).resolve().orElse(null);
    }

    @Override
    public <T> T getProvider(Entity entity, Class<T> clazz) {
        Capability<T> capability = this.getCapability(clazz);
        return entity.getCapability(capability).resolve().orElse(null);
    }

    public <T> void register(Class<T> clazz, CapabilityToken<T> token) {
        this.capabilities.put(clazz, CapabilityManager.get(token));
    }

    public <T> Capability<T> getCapability(Class<T> clazz) {
        return this.capabilities.get(clazz);
    }
}

