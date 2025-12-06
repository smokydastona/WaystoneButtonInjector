/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.util.ByIdMap
 *  net.minecraft.util.ByIdMap$OutOfBoundsStrategy
 */
package net.blay09.mods.balm.api.config.schema.builder;

import com.mojang.serialization.Codec;
import java.util.function.IntFunction;
import net.blay09.mods.balm.api.config.LenientEnumCodecs;
import net.blay09.mods.balm.api.config.schema.ConfiguredEnum;
import net.blay09.mods.balm.api.config.schema.builder.AbstractConfigProperty;
import net.blay09.mods.balm.api.config.schema.builder.ConfigPropertyBuilder;
import net.blay09.mods.balm.common.codec.ByteBufCodecs;
import net.blay09.mods.balm.common.codec.StreamCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ByIdMap;

public class EnumConfigProperty<T extends Enum<T>>
extends AbstractConfigProperty<T>
implements ConfiguredEnum<T> {
    private final T defaultValue;
    private final Codec<T> codec;
    private final StreamCodec<FriendlyByteBuf, T> streamCodec;

    public EnumConfigProperty(ConfigPropertyBuilder parent, T defaultValue) {
        super(parent);
        this.defaultValue = defaultValue;
        Class enumClass = ((Enum)defaultValue).getDeclaringClass();
        IntFunction byIdMapper = ByIdMap.m_262839_(Enum::ordinal, (Object[])((Enum[])enumClass.getEnumConstants()), (ByIdMap.OutOfBoundsStrategy)ByIdMap.OutOfBoundsStrategy.ZERO);
        this.codec = LenientEnumCodecs.fromValues(enumClass::getEnumConstants);
        this.streamCodec = ByteBufCodecs.idMapper(byIdMapper, Enum::ordinal);
    }

    @Override
    public Class<T> type() {
        return ((Enum)this.defaultValue).getDeclaringClass();
    }

    @Override
    public Codec<T> codec() {
        return this.codec;
    }

    @Override
    public StreamCodec<FriendlyByteBuf, T> streamCodec() {
        return this.streamCodec;
    }

    @Override
    public T defaultValue() {
        return this.defaultValue;
    }
}

