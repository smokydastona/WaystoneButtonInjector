/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.item.ItemStack
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.waystones.api;

import java.util.List;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.api.TeleportDestination;
import net.blay09.mods.waystones.core.WarpMode;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface IWaystoneTeleportContext {
    public Entity getEntity();

    public IWaystone getTargetWaystone();

    public TeleportDestination getDestination();

    public void setDestination(TeleportDestination var1);

    public List<Mob> getLeashedEntities();

    public List<Entity> getAdditionalEntities();

    public void addAdditionalEntity(Entity var1);

    @Nullable
    public IWaystone getFromWaystone();

    public void setFromWaystone(@Nullable IWaystone var1);

    public ItemStack getWarpItem();

    public void setWarpItem(ItemStack var1);

    public boolean isDimensionalTeleport();

    public int getXpCost();

    public void setXpCost(int var1);

    public void setCooldown(int var1);

    public int getCooldown();

    public WarpMode getWarpMode();

    public void setWarpMode(WarpMode var1);

    public boolean playsSound();

    public void setPlaysSound(boolean var1);

    public boolean playsEffect();

    public void setPlaysEffect(boolean var1);

    default public boolean consumesWarpItem() {
        return this.getWarpMode() != null && this.getWarpMode().consumesItem();
    }

    default public void setConsumesWarpItem(boolean consumesWarpItem) {
    }
}

