/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.balm.common.config;

import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.config.schema.ConfiguredEnum;
import net.blay09.mods.balm.api.config.schema.ConfiguredList;
import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;
import net.blay09.mods.balm.api.config.schema.ConfiguredSet;
import net.blay09.mods.balm.api.config.schema.builder.ConfigCategory;
import org.jetbrains.annotations.Nullable;

public class ConfigJsonExport {
    public static ExportedConfig mapToExportData(Collection<BalmConfigSchema> schemas) {
        ArrayList<ConfigProperty> properties = new ArrayList<ConfigProperty>();
        for (BalmConfigSchema schema : schemas) {
            for (ConfiguredProperty<?> property : schema.rootProperties()) {
                properties.add(new ConfigProperty(property));
            }
            for (ConfigCategory category : schema.categories()) {
                for (ConfiguredProperty<?> property : category.properties()) {
                    properties.add(new ConfigProperty(property));
                }
            }
        }
        return new ExportedConfig(properties);
    }

    public static void exportToFile(Collection<BalmConfigSchema> schemas, File file) throws IOException {
        File parentFile = file.getParentFile();
        if (!parentFile.exists() && !parentFile.mkdirs()) {
            throw new IOException("Failed to create parent directories for file: " + String.valueOf(file));
        }
        Files.writeString(file.toPath(), (CharSequence)new Gson().toJson((Object)ConfigJsonExport.mapToExportData(schemas)), new OpenOption[0]);
    }

    @Nullable
    private static String[] getValidValues(ConfiguredProperty<?> property) {
        ConfiguredSet setProperty;
        ConfiguredList listProperty;
        Class<Object> enumType = null;
        if (property instanceof ConfiguredEnum) {
            ConfiguredEnum enumProperty = (ConfiguredEnum)property;
            enumType = enumProperty.type();
        } else if (property instanceof ConfiguredList && (listProperty = (ConfiguredList)property).nestedType().isEnum()) {
            enumType = listProperty.nestedType();
        } else if (property instanceof ConfiguredSet && (setProperty = (ConfiguredSet)property).nestedType().isEnum()) {
            enumType = setProperty.nestedType();
        }
        if (enumType != null) {
            return (String[])Arrays.stream(enumType.getEnumConstants()).map(Object::toString).toArray(String[]::new);
        }
        return null;
    }

    public record ConfigProperty(String configType, String category, String name, String type, String description, String defaultValue, @Nullable String[] validValues) {
        public ConfigProperty(ConfiguredProperty<?> property) {
            this(property.parentSchema().identifier().m_135815_(), property.category(), property.name(), property.type().getSimpleName(), property.comment(), Objects.toString(property.defaultValue()), ConfigJsonExport.getValidValues(property));
        }
    }

    public record ExportedConfig(List<ConfigProperty> properties) {
    }
}

