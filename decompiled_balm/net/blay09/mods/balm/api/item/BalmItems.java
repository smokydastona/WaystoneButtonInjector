/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.BlockItem
 *  net.minecraft.world.item.CreativeModeTab
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.block.Block
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.balm.api.item;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Supplier;
import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public interface BalmItems {
    public static Item.Properties itemProperties(ResourceLocation identifier) {
        return new Item.Properties();
    }

    public static ResourceKey<Item> itemId(ResourceLocation identifier) {
        return ResourceKey.m_135785_((ResourceKey)Registries.f_256913_, (ResourceLocation)identifier);
    }

    public static BlockItem blockItem(Block block, ResourceLocation identifier) {
        return new BlockItem(block, BalmItems.itemProperties(identifier));
    }

    default public DeferredObject<Item> registerItem(Function<ResourceLocation, Item> supplier, ResourceLocation identifier) {
        return this.registerItem(supplier, identifier, identifier.m_247449_(identifier.m_135827_()));
    }

    public DeferredObject<Item> registerItem(Function<ResourceLocation, Item> var1, ResourceLocation var2, @Nullable ResourceLocation var3);

    public DeferredObject<CreativeModeTab> registerCreativeModeTab(Supplier<ItemStack> var1, ResourceLocation var2);

    @Deprecated
    default public DeferredObject<CreativeModeTab> registerCreativeModeTab(ResourceLocation identifier, Supplier<ItemStack> iconSupplier) {
        return this.registerCreativeModeTab(iconSupplier, identifier);
    }

    public void addToCreativeModeTab(ResourceLocation var1, Supplier<ItemLike[]> var2);

    public void setCreativeModeTabSorting(ResourceLocation var1, Comparator<ItemLike> var2);

    @Deprecated(forRemoval=true, since="1.21.5")
    default public Item.Properties itemProperties() {
        return new Item.Properties();
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    default public DeferredObject<Item> registerItem(Supplier<Item> supplier, ResourceLocation identifier) {
        return this.registerItem(supplier, identifier, identifier.m_247449_(identifier.m_135827_()));
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    default public DeferredObject<Item> registerItem(Supplier<Item> supplier, ResourceLocation identifier, @Nullable ResourceLocation creativeTab) {
        return this.registerItem((ResourceLocation id) -> (Item)supplier.get(), identifier, creativeTab);
    }

    public BalmItems scoped(String var1);
}

