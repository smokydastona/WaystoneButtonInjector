/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.ByIdMap
 *  net.minecraft.util.ByIdMap$OutOfBoundsStrategy
 */
package net.blay09.mods.balm.api.config;

import com.mojang.serialization.Codec;
import java.util.function.IntFunction;
import net.blay09.mods.balm.api.config.LenientEnumCodecs;
import net.blay09.mods.balm.api.config.schema.builder.BooleanConfigProperty;
import net.blay09.mods.balm.api.config.schema.builder.DoubleConfigProperty;
import net.blay09.mods.balm.api.config.schema.builder.FloatConfigProperty;
import net.blay09.mods.balm.api.config.schema.builder.IntConfigProperty;
import net.blay09.mods.balm.api.config.schema.builder.LongConfigProperty;
import net.blay09.mods.balm.common.codec.ByteBufCodecs;
import net.blay09.mods.balm.common.codec.StreamCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ByIdMap;

public class PrimitiveConfigCodecs {
    public static <T> Codec<T> codec(Class<T> type) {
        if (type == String.class) {
            return Codec.STRING;
        }
        if (type == Integer.class || type == Integer.TYPE) {
            return IntConfigProperty.CODEC;
        }
        if (type == Long.class || type == Long.TYPE) {
            return LongConfigProperty.CODEC;
        }
        if (type == Float.class || type == Float.TYPE) {
            return FloatConfigProperty.CODEC;
        }
        if (type == Double.class || type == Double.TYPE) {
            return DoubleConfigProperty.CODEC;
        }
        if (type == Boolean.class || type == Boolean.TYPE) {
            return BooleanConfigProperty.CODEC;
        }
        if (type == ResourceLocation.class) {
            return ResourceLocation.f_135803_;
        }
        if (type.isEnum()) {
            return PrimitiveConfigCodecs.enumCodec(type);
        }
        throw new IllegalArgumentException("Unsupported nested type: " + type.getName());
    }

    private static <T extends Enum<T>> Codec<T> enumCodec(Class<T> type) {
        return LenientEnumCodecs.fromValues(type::getEnumConstants);
    }

    public static <T> StreamCodec<FriendlyByteBuf, T> streamCodec(Class<T> type) {
        if (type == String.class) {
            return ByteBufCodecs.STRING_UTF8;
        }
        if (type == Integer.class || type == Integer.TYPE) {
            return ByteBufCodecs.INT;
        }
        if (type == Long.class || type == Long.TYPE) {
            return ByteBufCodecs.LONG;
        }
        if (type == Float.class || type == Float.TYPE) {
            return ByteBufCodecs.FLOAT;
        }
        if (type == Double.class || type == Double.TYPE) {
            return ByteBufCodecs.DOUBLE;
        }
        if (type == Boolean.class || type == Boolean.TYPE) {
            return ByteBufCodecs.BOOL;
        }
        if (type == ResourceLocation.class) {
            return ByteBufCodecs.RESOURCE_LOCATION;
        }
        if (type.isEnum()) {
            return PrimitiveConfigCodecs.enumStreamCodec(type);
        }
        throw new IllegalArgumentException("Unsupported nested type: " + type.getName());
    }

    private static <T extends Enum<T>> StreamCodec<FriendlyByteBuf, T> enumStreamCodec(Class<T> type) {
        IntFunction byIdMapper = ByIdMap.m_262839_(Enum::ordinal, (Object[])((Enum[])type.getEnumConstants()), (ByIdMap.OutOfBoundsStrategy)ByIdMap.OutOfBoundsStrategy.ZERO);
        return ByteBufCodecs.idMapper(byIdMapper, Enum::ordinal);
    }
}

