/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.BlockItem
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.balm.api.block;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.Nullable;

public interface BalmBlocks {
    public static BlockBehaviour.Properties blockProperties(ResourceLocation identifier) {
        return BlockBehaviour.Properties.m_284310_();
    }

    public static ResourceKey<Block> blockId(ResourceLocation identifier) {
        return ResourceKey.m_135785_((ResourceKey)Registries.f_256747_, (ResourceLocation)identifier);
    }

    public DeferredObject<Block> registerBlock(Function<ResourceLocation, Block> var1, ResourceLocation var2);

    public DeferredObject<Item> registerBlockItem(Function<ResourceLocation, BlockItem> var1, ResourceLocation var2, @Nullable ResourceLocation var3);

    public void register(Function<ResourceLocation, Block> var1, BiFunction<Block, ResourceLocation, BlockItem> var2, ResourceLocation var3, @Nullable ResourceLocation var4);

    default public DeferredObject<Item> registerBlockItem(Function<ResourceLocation, BlockItem> supplier, ResourceLocation identifier) {
        return this.registerBlockItem(supplier, identifier, identifier.m_247449_(identifier.m_135827_()));
    }

    default public void register(Function<ResourceLocation, Block> blockSupplier, BiFunction<Block, ResourceLocation, BlockItem> blockItemSupplier, ResourceLocation identifier) {
        this.register(blockSupplier, blockItemSupplier, identifier, identifier.m_247449_(identifier.m_135827_()));
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    default public BlockBehaviour.Properties blockProperties() {
        return BlockBehaviour.Properties.m_284310_();
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    default public DeferredObject<Block> registerBlock(Supplier<Block> supplier, ResourceLocation identifier) {
        return this.registerBlock((ResourceLocation id) -> (Block)supplier.get(), identifier);
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    default public DeferredObject<Item> registerBlockItem(Supplier<BlockItem> supplier, ResourceLocation identifier) {
        return this.registerBlockItem(supplier, identifier, identifier.m_247449_(identifier.m_135827_()));
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    default public DeferredObject<Item> registerBlockItem(Supplier<BlockItem> supplier, ResourceLocation identifier, @Nullable ResourceLocation creativeTab) {
        return this.registerBlockItem((ResourceLocation id) -> (BlockItem)supplier.get(), identifier, creativeTab);
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    default public void register(Supplier<Block> blockSupplier, Supplier<BlockItem> blockItemSupplier, ResourceLocation identifier) {
        this.register(blockSupplier, blockItemSupplier, identifier, identifier.m_247449_(identifier.m_135827_()));
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    default public void register(Supplier<Block> blockSupplier, Supplier<BlockItem> blockItemSupplier, ResourceLocation identifier, @Nullable ResourceLocation creativeTab) {
        this.registerBlock(blockSupplier, identifier);
        this.registerBlockItem((ResourceLocation id) -> (BlockItem)blockItemSupplier.get(), identifier, creativeTab);
    }

    public BalmBlocks scoped(String var1);
}

