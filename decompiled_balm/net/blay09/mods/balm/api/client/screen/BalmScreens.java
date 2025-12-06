/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.components.AbstractWidget
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.MenuType
 */
package net.blay09.mods.balm.api.client.screen;

import java.util.function.Supplier;
import net.blay09.mods.balm.api.client.screen.BalmScreenFactory;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public interface BalmScreens {
    default public <T extends AbstractContainerMenu, S extends Screen> void registerScreen(ResourceLocation identifier, Supplier<MenuType<? extends T>> type, BalmScreenFactory<T, S> screenFactory) {
        this.registerScreen(type, screenFactory);
    }

    public AbstractWidget addRenderableWidget(Screen var1, AbstractWidget var2);

    @Deprecated(forRemoval=true, since="1.21.5")
    public <T extends AbstractContainerMenu, S extends Screen> void registerScreen(Supplier<MenuType<? extends T>> var1, BalmScreenFactory<T, S> var2);

    public BalmScreens scoped(String var1);
}

