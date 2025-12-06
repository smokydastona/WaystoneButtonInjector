/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.resources.ResourceLocation
 */
package net.blay09.mods.balm.api.config.schema.builder;

import com.mojang.serialization.Codec;
import net.blay09.mods.balm.api.config.schema.ConfiguredResourceLocation;
import net.blay09.mods.balm.api.config.schema.builder.AbstractConfigProperty;
import net.blay09.mods.balm.api.config.schema.builder.ConfigPropertyBuilder;
import net.blay09.mods.balm.common.codec.ByteBufCodecs;
import net.blay09.mods.balm.common.codec.StreamCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class ResourceLocationConfigProperty
extends AbstractConfigProperty<ResourceLocation>
implements ConfiguredResourceLocation {
    private final ResourceLocation defaultValue;

    public ResourceLocationConfigProperty(ConfigPropertyBuilder parent, ResourceLocation defaultValue) {
        super(parent);
        this.defaultValue = defaultValue;
    }

    @Override
    public Class<ResourceLocation> type() {
        return ResourceLocation.class;
    }

    @Override
    public Codec<ResourceLocation> codec() {
        return ResourceLocation.f_135803_;
    }

    @Override
    public StreamCodec<FriendlyByteBuf, ResourceLocation> streamCodec() {
        return ByteBufCodecs.RESOURCE_LOCATION;
    }

    @Override
    public ResourceLocation defaultValue() {
        return this.defaultValue;
    }
}

