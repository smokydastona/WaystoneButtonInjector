/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.common;

import java.util.function.Supplier;
import net.blay09.mods.balm.common.NamespaceResolver;

public record LegacyNamespaceResolver(Supplier<String> defaultProvider) implements NamespaceResolver
{
    @Override
    public String getDefaultNamespace() {
        return this.defaultProvider.get();
    }
}

