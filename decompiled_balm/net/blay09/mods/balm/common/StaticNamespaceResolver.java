/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.common;

import net.blay09.mods.balm.common.NamespaceResolver;

public record StaticNamespaceResolver(String modId) implements NamespaceResolver
{
    @Override
    public String getDefaultNamespace() {
        return this.modId;
    }
}

