/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.player.Player
 */
package net.blay09.mods.waystones.core;

import java.util.List;
import net.blay09.mods.waystones.api.IWaystone;
import net.minecraft.world.entity.player.Player;

public interface IPlayerWaystoneData {
    public void activateWaystone(Player var1, IWaystone var2);

    public boolean isWaystoneActivated(Player var1, IWaystone var2);

    public void deactivateWaystone(Player var1, IWaystone var2);

    public long getWarpStoneCooldownUntil(Player var1);

    public void setWarpStoneCooldownUntil(Player var1, long var2);

    public long getInventoryButtonCooldownUntil(Player var1);

    public void setInventoryButtonCooldownUntil(Player var1, long var2);

    public List<IWaystone> getWaystones(Player var1);

    public void swapWaystoneSorting(Player var1, int var2, int var3);
}

