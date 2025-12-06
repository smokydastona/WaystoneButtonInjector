/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.common.codec;

import java.util.function.BiConsumer;
import java.util.function.Function;

public interface StreamCodec<TBuffer, TData> {
    public static <TBuffer, TData> StreamCodec<TBuffer, TData> of(final BiConsumer<TBuffer, TData> encoder, final Function<TBuffer, TData> decoder) {
        return new StreamCodec<TBuffer, TData>(){

            @Override
            public void encode(TBuffer buf, TData value) {
                encoder.accept(buf, value);
            }

            @Override
            public TData decode(TBuffer buf) {
                return decoder.apply(buf);
            }
        };
    }

    public void encode(TBuffer var1, TData var2);

    public TData decode(TBuffer var1);

    default public <TMappedData> StreamCodec<TBuffer, TMappedData> map(final Function<? super TData, ? extends TMappedData> mapper, final Function<? super TMappedData, ? extends TData> reverseMapper) {
        return new StreamCodec<TBuffer, TMappedData>(){

            @Override
            public TMappedData decode(TBuffer buffer) {
                return mapper.apply(StreamCodec.this.decode(buffer));
            }

            @Override
            public void encode(TBuffer buffer, TMappedData data) {
                StreamCodec.this.encode(buffer, reverseMapper.apply(data));
            }
        };
    }
}

