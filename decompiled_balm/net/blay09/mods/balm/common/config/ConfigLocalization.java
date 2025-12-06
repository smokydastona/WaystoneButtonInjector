/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.common.config;

import java.util.HashSet;
import java.util.Set;
import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;
import net.blay09.mods.balm.api.config.schema.builder.ConfigCategory;

public class ConfigLocalization {
    private static final Set<String> modernTranslationKeyMods = new HashSet<String>();

    public static void enableModernTranslationKeys(String modId) {
        modernTranslationKeyMods.add(modId);
    }

    private static boolean usesLegacyTranslationKeys(String modId) {
        return !modernTranslationKeyMods.contains(modId);
    }

    private static boolean usesLegacyTranslationKeys(BalmConfigSchema schema) {
        return !modernTranslationKeyMods.contains(schema.identifier().m_135827_());
    }

    public static String forTitle(BalmConfigSchema schema) {
        String modId = schema.identifier().m_135827_();
        if (ConfigLocalization.usesLegacyTranslationKeys(schema)) {
            return "config." + modId + "." + schema.identifier().m_135815_() + ".title";
        }
        return modId + ".configuration." + schema.identifier().m_135815_() + ".title";
    }

    public static String forTitle(String modId) {
        if (ConfigLocalization.usesLegacyTranslationKeys(modId)) {
            return "config." + modId + ".title";
        }
        return modId + ".configuration.title";
    }

    public static String forRootCategory(BalmConfigSchema schema) {
        String modId = schema.identifier().m_135827_();
        if (ConfigLocalization.usesLegacyTranslationKeys(modId)) {
            return "config." + modId;
        }
        return modId + ".configuration";
    }

    public static String forCategory(ConfigCategory category) {
        String modId = category.parentSchema().identifier().m_135827_();
        if (ConfigLocalization.usesLegacyTranslationKeys(modId)) {
            return "config." + modId + "." + category.name();
        }
        return modId + ".configuration." + category.name();
    }

    public static String forProperty(ConfiguredProperty<?> property) {
        String modId = property.parentSchema().identifier().m_135827_();
        if (ConfigLocalization.usesLegacyTranslationKeys(modId)) {
            if (property.category().isEmpty()) {
                return "config." + modId + "." + property.name();
            }
            return "config." + modId + "." + property.category() + "." + property.name();
        }
        if (property.category().isEmpty()) {
            return modId + ".configuration." + property.name();
        }
        return modId + ".configuration." + property.category() + "." + property.name();
    }

    public static String forPropertyTooltip(ConfiguredProperty<?> property) {
        return ConfigLocalization.forProperty(property) + ".tooltip";
    }
}

