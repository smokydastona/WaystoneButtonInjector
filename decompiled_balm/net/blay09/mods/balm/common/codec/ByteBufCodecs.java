/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.resources.ResourceLocation
 */
package net.blay09.mods.balm.common.codec;

import io.netty.buffer.ByteBuf;
import java.util.Collection;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;
import net.blay09.mods.balm.common.codec.StreamCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class ByteBufCodecs {
    public static final StreamCodec<FriendlyByteBuf, Boolean> BOOL = StreamCodec.of(ByteBuf::writeBoolean, ByteBuf::readBoolean);
    public static final StreamCodec<FriendlyByteBuf, Integer> INT = StreamCodec.of(ByteBuf::writeInt, ByteBuf::readInt);
    public static final StreamCodec<FriendlyByteBuf, Double> DOUBLE = StreamCodec.of(ByteBuf::writeDouble, ByteBuf::readDouble);
    public static final StreamCodec<FriendlyByteBuf, Float> FLOAT = StreamCodec.of(ByteBuf::writeFloat, ByteBuf::readFloat);
    public static final StreamCodec<FriendlyByteBuf, Long> LONG = StreamCodec.of(ByteBuf::writeLong, ByteBuf::readLong);
    public static final StreamCodec<FriendlyByteBuf, String> STRING_UTF8 = StreamCodec.of(FriendlyByteBuf::m_130070_, FriendlyByteBuf::m_130277_);
    public static final StreamCodec<FriendlyByteBuf, ResourceLocation> RESOURCE_LOCATION = StreamCodec.of(FriendlyByteBuf::m_130085_, FriendlyByteBuf::m_130281_);

    public static <T extends Enum<T>> StreamCodec<FriendlyByteBuf, T> idMapper(IntFunction<T> idLookup, ToIntFunction<T> idGetter) {
        return INT.map(idLookup::apply, idGetter::applyAsInt);
    }

    public static <TBuffer extends ByteBuf, TItem, TCollection extends Collection<TItem>> StreamCodec<TBuffer, TCollection> collection(final IntFunction<TCollection> factory, final StreamCodec<TBuffer, TItem> codec) {
        return new StreamCodec<TBuffer, TCollection>(){

            @Override
            public TCollection decode(TBuffer buffer) {
                int count = buffer.readInt();
                Collection collection = (Collection)factory.apply(count);
                for (int i = 0; i < count; ++i) {
                    collection.add(codec.decode(buffer));
                }
                return collection;
            }

            @Override
            public void encode(TBuffer buffer, TCollection collection) {
                buffer.writeInt(collection.size());
                for (Object item : collection) {
                    codec.encode(buffer, item);
                }
            }
        };
    }
}

