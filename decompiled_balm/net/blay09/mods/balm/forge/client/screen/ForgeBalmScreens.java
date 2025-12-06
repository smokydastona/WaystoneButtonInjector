/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.util.Pair
 *  net.minecraft.client.gui.components.AbstractWidget
 *  net.minecraft.client.gui.components.Renderable
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.narration.NarratableEntry
 *  net.minecraft.client.gui.screens.MenuScreens
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.MenuType
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
 */
package net.blay09.mods.balm.forge.client.screen;

import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.blay09.mods.balm.api.client.screen.BalmScreenFactory;
import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.blay09.mods.balm.common.NamespaceResolver;
import net.blay09.mods.balm.common.StaticNamespaceResolver;
import net.blay09.mods.balm.forge.ModBusEventRegisters;
import net.blay09.mods.balm.mixin.ScreenAccessor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public record ForgeBalmScreens(NamespaceResolver namespaceResolver) implements BalmScreens
{
    private static <T extends AbstractContainerMenu, S extends Screen> void registerScreenImmediate(Supplier<MenuType<? extends T>> type, BalmScreenFactory<T, S> screenFactory) {
        MenuScreens.m_96206_(type.get(), screenFactory::create);
    }

    @Override
    public <T extends AbstractContainerMenu, S extends Screen> void registerScreen(Supplier<MenuType<? extends T>> type, BalmScreenFactory<T, S> screenFactory) {
        this.getActiveRegistrations().menuTypes.add(Pair.of(type::get, screenFactory));
    }

    @Override
    public BalmScreens scoped(String modId) {
        return new ForgeBalmScreens(new StaticNamespaceResolver(modId));
    }

    @Override
    public AbstractWidget addRenderableWidget(Screen screen, AbstractWidget widget) {
        ScreenAccessor accessor = (ScreenAccessor)screen;
        accessor.balm_getChildren().add((GuiEventListener)widget);
        accessor.balm_getRenderables().add((Renderable)widget);
        accessor.balm_getNarratables().add((NarratableEntry)widget);
        return widget;
    }

    private Registrations getActiveRegistrations() {
        return ModBusEventRegisters.getRegistrations(this.namespaceResolver.getDefaultNamespace(), Registrations.class);
    }

    public static class Registrations {
        public final List<Pair<Supplier<MenuType<?>>, BalmScreenFactory<?, ?>>> menuTypes = new ArrayList();

        @SubscribeEvent
        public void setupClient(FMLClientSetupEvent event) {
            for (Pair<Supplier<MenuType<?>>, BalmScreenFactory<?, ?>> entry : this.menuTypes) {
                ForgeBalmScreens.registerScreenImmediate(((Supplier)((Supplier)entry.getFirst()))::get, (BalmScreenFactory)entry.getSecond());
            }
        }
    }
}

