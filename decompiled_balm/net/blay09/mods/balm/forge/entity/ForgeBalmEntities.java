/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.EntityType$Builder
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraftforge.event.entity.EntityAttributeCreationEvent
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.registries.DeferredRegister
 *  net.minecraftforge.registries.ForgeRegistries
 *  net.minecraftforge.registries.RegistryObject
 */
package net.blay09.mods.balm.forge.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.entity.BalmEntities;
import net.blay09.mods.balm.common.NamespaceResolver;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.blay09.mods.balm.forge.ModBusEventRegisters;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public record ForgeBalmEntities(NamespaceResolver namespaceResolver) implements BalmEntities
{
    @Override
    public <T extends Entity> DeferredObject<EntityType<T>> registerEntity(ResourceLocation identifier, EntityType.Builder<T> typeBuilder) {
        DeferredRegister register = DeferredRegisters.get(ForgeRegistries.ENTITY_TYPES, identifier.m_135827_());
        RegistryObject registryObject = register.register(identifier.m_135815_(), () -> typeBuilder.m_20712_(identifier.toString()));
        return new DeferredObject<EntityType<T>>(identifier, registryObject, () -> ((RegistryObject)registryObject).isPresent());
    }

    @Override
    public <T extends LivingEntity> DeferredObject<EntityType<T>> registerEntity(ResourceLocation identifier, EntityType.Builder<T> typeBuilder, Supplier<AttributeSupplier.Builder> attributeBuilder) {
        DeferredRegister register = DeferredRegisters.get(ForgeRegistries.ENTITY_TYPES, identifier.m_135827_());
        Registrations registrations = this.getActiveRegistrations();
        RegistryObject registryObject = register.register(identifier.m_135815_(), () -> {
            EntityType entityType = typeBuilder.m_20712_(identifier.toString());
            registrations.attributeSuppliers.put((EntityType<? extends LivingEntity>)entityType, ((AttributeSupplier.Builder)attributeBuilder.get()).m_22265_());
            return entityType;
        });
        return new DeferredObject<EntityType<T>>(identifier, registryObject, () -> ((RegistryObject)registryObject).isPresent());
    }

    private Registrations getActiveRegistrations() {
        return ModBusEventRegisters.getRegistrations(this.namespaceResolver.getDefaultNamespace(), Registrations.class);
    }

    public static class Registrations {
        public final Map<EntityType<? extends LivingEntity>, AttributeSupplier> attributeSuppliers = new HashMap<EntityType<? extends LivingEntity>, AttributeSupplier>();

        @SubscribeEvent
        public void registerAttributes(EntityAttributeCreationEvent event) {
            for (Map.Entry<EntityType<? extends LivingEntity>, AttributeSupplier> entry : this.attributeSuppliers.entrySet()) {
                event.put(entry.getKey(), entry.getValue());
            }
        }
    }
}

