/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.blay09.mods.balm.api.Balm
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.StringTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.world.entity.player.Player
 */
package net.blay09.mods.waystones.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.core.IPlayerWaystoneData;
import net.blay09.mods.waystones.core.WaystoneProxy;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;

public class PersistentPlayerWaystoneData
implements IPlayerWaystoneData {
    private static final String TAG_NAME = "WaystonesData";
    private static final String ACTIVATED_WAYSTONES = "Waystones";
    private static final String INVENTORY_BUTTON_COOLDOWN_UNTIL = "InventoryButtonCooldownUntilUnix";
    private static final String WARP_STONE_COOLDOWN_UNTIL = "WarpStoneCooldownUntilUnix";

    @Override
    public void activateWaystone(Player player, IWaystone waystone) {
        ListTag activatedWaystonesData = PersistentPlayerWaystoneData.getActivatedWaystonesData(PersistentPlayerWaystoneData.getWaystonesData(player));
        activatedWaystonesData.add((Object)StringTag.m_129297_((String)waystone.getWaystoneUid().toString()));
    }

    @Override
    public boolean isWaystoneActivated(Player player, IWaystone waystone) {
        ListTag activatedWaystones = PersistentPlayerWaystoneData.getActivatedWaystonesData(PersistentPlayerWaystoneData.getWaystonesData(player));
        String waystoneUid = waystone.getWaystoneUid().toString();
        for (Tag activatedWaystone : activatedWaystones) {
            if (!waystoneUid.equals(activatedWaystone.m_7916_())) continue;
            return true;
        }
        return false;
    }

    @Override
    public List<IWaystone> getWaystones(Player player) {
        ListTag activatedWaystones = PersistentPlayerWaystoneData.getActivatedWaystonesData(PersistentPlayerWaystoneData.getWaystonesData(player));
        ArrayList<IWaystone> waystones = new ArrayList<IWaystone>();
        Iterator iterator = activatedWaystones.iterator();
        while (iterator.hasNext()) {
            Tag activatedWaystone = (Tag)iterator.next();
            WaystoneProxy proxy = new WaystoneProxy(player.m_20194_(), UUID.fromString(activatedWaystone.m_7916_()));
            if (proxy.isValid()) {
                waystones.add(proxy);
                continue;
            }
            iterator.remove();
        }
        return waystones;
    }

    @Override
    public void swapWaystoneSorting(Player player, int index, int otherIndex) {
        ListTag activatedWaystones = PersistentPlayerWaystoneData.getActivatedWaystonesData(PersistentPlayerWaystoneData.getWaystonesData(player));
        if (otherIndex == -1) {
            Tag waystone = activatedWaystones.remove(index);
            activatedWaystones.add(0, waystone);
        } else if (otherIndex == activatedWaystones.size()) {
            Tag waystone = activatedWaystones.remove(index);
            activatedWaystones.add((Object)waystone);
        } else {
            Collections.swap(activatedWaystones, index, otherIndex);
        }
    }

    @Override
    public void deactivateWaystone(Player player, IWaystone waystone) {
        CompoundTag data = PersistentPlayerWaystoneData.getWaystonesData(player);
        ListTag activatedWaystones = PersistentPlayerWaystoneData.getActivatedWaystonesData(data);
        String waystoneUid = waystone.getWaystoneUid().toString();
        for (int i = activatedWaystones.size() - 1; i >= 0; --i) {
            Tag activatedWaystone = activatedWaystones.get(i);
            if (!waystoneUid.equals(activatedWaystone.m_7916_())) continue;
            activatedWaystones.remove(i);
            break;
        }
    }

    @Override
    public long getWarpStoneCooldownUntil(Player player) {
        return PersistentPlayerWaystoneData.getWaystonesData(player).m_128454_(WARP_STONE_COOLDOWN_UNTIL);
    }

    @Override
    public void setWarpStoneCooldownUntil(Player player, long timeStamp) {
        PersistentPlayerWaystoneData.getWaystonesData(player).m_128356_(WARP_STONE_COOLDOWN_UNTIL, timeStamp);
    }

    @Override
    public long getInventoryButtonCooldownUntil(Player player) {
        return PersistentPlayerWaystoneData.getWaystonesData(player).m_128454_(INVENTORY_BUTTON_COOLDOWN_UNTIL);
    }

    @Override
    public void setInventoryButtonCooldownUntil(Player player, long timeStamp) {
        PersistentPlayerWaystoneData.getWaystonesData(player).m_128356_(INVENTORY_BUTTON_COOLDOWN_UNTIL, timeStamp);
    }

    private static ListTag getActivatedWaystonesData(CompoundTag data) {
        ListTag list = data.m_128437_(ACTIVATED_WAYSTONES, 8);
        data.m_128365_(ACTIVATED_WAYSTONES, (Tag)list);
        return list;
    }

    private static CompoundTag getWaystonesData(Player player) {
        CompoundTag persistedData = Balm.getHooks().getPersistentData(player);
        CompoundTag compound = persistedData.m_128469_(TAG_NAME);
        persistedData.m_128365_(TAG_NAME, (Tag)compound);
        return compound;
    }
}

