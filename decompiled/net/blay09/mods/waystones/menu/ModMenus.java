/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.blay09.mods.balm.api.DeferredObject
 *  net.blay09.mods.balm.api.menu.BalmMenus
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.inventory.MenuType
 *  org.jetbrains.annotations.NotNull
 */
package net.blay09.mods.waystones.menu;

import java.util.ArrayList;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.menu.BalmMenus;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.core.WarpMode;
import net.blay09.mods.waystones.core.Waystone;
import net.blay09.mods.waystones.menu.WarpPlateContainer;
import net.blay09.mods.waystones.menu.WaystoneSelectionMenu;
import net.blay09.mods.waystones.menu.WaystoneSettingsMenu;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.NotNull;

public class ModMenus {
    public static DeferredObject<MenuType<WaystoneSelectionMenu>> waystoneSelection;
    public static DeferredObject<MenuType<WaystoneSelectionMenu>> sharestoneSelection;
    public static DeferredObject<MenuType<WarpPlateContainer>> warpPlate;
    public static DeferredObject<MenuType<WaystoneSettingsMenu>> waystoneSettings;

    public static void initialize(BalmMenus menus) {
        waystoneSelection = menus.registerMenu(ModMenus.id("waystone_selection"), (syncId, inventory, buf) -> {
            WarpMode warpMode = WarpMode.values[buf.readByte()];
            IWaystone fromWaystone = null;
            if (warpMode == WarpMode.WAYSTONE_TO_WAYSTONE) {
                fromWaystone = Waystone.read(buf);
            }
            return WaystoneSelectionMenu.createWaystoneSelection(syncId, inventory.f_35978_, warpMode, fromWaystone);
        });
        sharestoneSelection = menus.registerMenu(ModMenus.id("sharestone_selection"), (syncId, inventory, buf) -> {
            IWaystone fromWaystone = Waystone.read(buf);
            int count = buf.readShort();
            ArrayList<IWaystone> waystones = new ArrayList<IWaystone>(count);
            for (int i = 0; i < count; ++i) {
                waystones.add(Waystone.read(buf));
            }
            return new WaystoneSelectionMenu((MenuType<WaystoneSelectionMenu>)((MenuType)sharestoneSelection.get()), WarpMode.SHARESTONE_TO_SHARESTONE, fromWaystone, syncId, waystones);
        });
        warpPlate = menus.registerMenu(ModMenus.id("warp_plate"), (windowId, inv, data) -> {
            IWaystone waystone = Waystone.read(data);
            return new WarpPlateContainer(windowId, inv, waystone);
        });
        waystoneSettings = menus.registerMenu(ModMenus.id("waystone_settings"), (windowId, inv, data) -> {
            IWaystone waystone = Waystone.read(data);
            return new WaystoneSettingsMenu((MenuType<WaystoneSettingsMenu>)((MenuType)waystoneSettings.get()), waystone, windowId);
        });
    }

    @NotNull
    private static ResourceLocation id(String name) {
        return new ResourceLocation("waystones", name);
    }
}

