/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ArrayListMultimap
 *  com.google.common.collect.Multimap
 *  net.minecraft.core.Registry
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.CreativeModeTab
 *  net.minecraft.world.item.CreativeModeTab$Output
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  net.minecraftforge.event.BuildCreativeModeTabContentsEvent
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.registries.DeferredRegister
 *  net.minecraftforge.registries.RegistryObject
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.balm.forge.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.balm.common.NamespaceResolver;
import net.blay09.mods.balm.common.StaticNamespaceResolver;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.blay09.mods.balm.forge.ModBusEventRegisters;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

public record ForgeBalmItems(NamespaceResolver namespaceResolver) implements BalmItems
{
    private static final Set<ResourceLocation> managedCreativeTabs = new HashSet<ResourceLocation>();

    @Override
    public DeferredObject<Item> registerItem(Function<ResourceLocation, Item> supplier, ResourceLocation identifier, @Nullable ResourceLocation creativeTab) {
        DeferredRegister register = DeferredRegisters.get(Registries.f_256913_, identifier.m_135827_());
        RegistryObject registryObject = register.register(identifier.m_135815_(), () -> (Item)supplier.apply(identifier));
        if (creativeTab != null) {
            this.getActiveRegistrations().creativeTabContents.put((Object)creativeTab, () -> new ItemLike[]{(ItemLike)registryObject.get()});
        }
        return new DeferredObject<Item>(identifier, (Supplier<Item>)registryObject, () -> ((RegistryObject)registryObject).isPresent());
    }

    @Override
    public DeferredObject<CreativeModeTab> registerCreativeModeTab(Supplier<ItemStack> iconSupplier, ResourceLocation identifier) {
        managedCreativeTabs.add(identifier);
        DeferredRegister register = DeferredRegisters.get(Registries.f_279569_, identifier.m_135827_());
        RegistryObject registryObject = register.register(identifier.m_135815_(), () -> {
            MutableComponent displayName = Component.m_237115_((String)("itemGroup." + identifier.toString().replace(':', '.')));
            Registrations registrations = this.getActiveRegistrations();
            return (CreativeModeTab)Registry.m_122965_((Registry)BuiltInRegistries.f_279662_, (ResourceLocation)identifier, (Object)CreativeModeTab.builder().m_257941_((Component)displayName).m_257737_(iconSupplier).m_257501_((enabledFeatures, entries) -> registrations.buildCreativeTabContents(identifier, entries)).m_257652_());
        });
        return new DeferredObject<CreativeModeTab>(identifier, (Supplier<CreativeModeTab>)registryObject, () -> ((RegistryObject)registryObject).isPresent());
    }

    @Override
    public void addToCreativeModeTab(ResourceLocation tabIdentifier, Supplier<ItemLike[]> itemsSupplier) {
        this.getActiveRegistrations().creativeTabContents.put((Object)tabIdentifier, itemsSupplier);
    }

    @Override
    public void setCreativeModeTabSorting(ResourceLocation tabIdentifier, Comparator<ItemLike> comparator) {
        this.getActiveRegistrations().creativeTabSorting.put(tabIdentifier, comparator);
    }

    private Registrations getActiveRegistrations() {
        return ModBusEventRegisters.getRegistrations(this.namespaceResolver.getDefaultNamespace(), Registrations.class);
    }

    @Override
    public BalmItems scoped(String modId) {
        return new ForgeBalmItems(new StaticNamespaceResolver(modId));
    }

    public static class Registrations {
        public final Multimap<ResourceLocation, Supplier<ItemLike[]>> creativeTabContents = ArrayListMultimap.create();
        private final Map<ResourceLocation, Comparator<ItemLike>> creativeTabSorting = new HashMap<ResourceLocation, Comparator<ItemLike>>();

        public void buildCreativeTabContents(ResourceLocation tabIdentifier, CreativeModeTab.Output entries) {
            Collection itemStackArraySuppliers = this.creativeTabContents.get((Object)tabIdentifier);
            Comparator<ItemLike> comparator = this.creativeTabSorting.get(tabIdentifier);
            if (!itemStackArraySuppliers.isEmpty()) {
                itemStackArraySuppliers.forEach(it -> {
                    List<ItemLike> itemStacks = Arrays.asList((ItemLike[])it.get());
                    List<ItemLike> sortedItemStacks = comparator != null ? itemStacks.stream().sorted(comparator).toList() : itemStacks;
                    for (ItemLike itemStack : sortedItemStacks) {
                        entries.m_246326_(itemStack);
                    }
                });
            }
        }

        @SubscribeEvent
        public void buildOtherCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
            ResourceLocation creativeModeTabId = BuiltInRegistries.f_279662_.m_7981_((Object)event.getTab());
            if (creativeModeTabId != null && !managedCreativeTabs.contains(creativeModeTabId)) {
                this.buildCreativeTabContents(creativeModeTabId, (CreativeModeTab.Output)event);
            }
        }
    }
}

