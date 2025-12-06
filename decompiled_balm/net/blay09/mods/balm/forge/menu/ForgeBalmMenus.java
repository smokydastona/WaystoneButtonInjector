/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.flag.FeatureFlag
 *  net.minecraft.world.flag.FeatureFlagSet
 *  net.minecraft.world.flag.FeatureFlags
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.MenuType
 *  net.minecraft.world.inventory.MenuType$MenuSupplier
 *  net.minecraftforge.network.IContainerFactory
 *  net.minecraftforge.registries.DeferredRegister
 *  net.minecraftforge.registries.ForgeRegistries
 *  net.minecraftforge.registries.RegistryObject
 */
package net.blay09.mods.balm.forge.menu;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.menu.BalmMenuFactory;
import net.blay09.mods.balm.api.menu.BalmMenus;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ForgeBalmMenus
implements BalmMenus {
    @Override
    public <T extends AbstractContainerMenu> DeferredObject<MenuType<T>> registerMenu(ResourceLocation identifier, BalmMenuFactory<T> factory) {
        DeferredRegister register = DeferredRegisters.get(ForgeRegistries.MENU_TYPES, identifier.m_135827_());
        RegistryObject registryObject = register.register(identifier.m_135815_(), () -> new MenuType((MenuType.MenuSupplier)((IContainerFactory)factory::create), FeatureFlagSet.m_247091_((FeatureFlag)FeatureFlags.f_244571_)));
        return new DeferredObject<MenuType<T>>(identifier, registryObject, () -> ((RegistryObject)registryObject).isPresent());
    }
}

