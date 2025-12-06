/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Table
 */
package net.blay09.mods.balm.notoml;

import com.google.common.collect.Table;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import net.blay09.mods.balm.notoml.Notoml;

public class NotomlSerializer {
    public static void serialize(Writer writer, Notoml notoml) throws IOException {
        writer.write(NotomlSerializer.serializeToString(notoml));
    }

    private static String serializeToString(Notoml notoml) {
        StringBuilder sb = new StringBuilder();
        Table<String, String, Object> data = notoml.getProperties();
        Table<String, String, String> comments = notoml.getComments();
        List sortedCategoryKeys = data.rowKeySet().stream().sorted().toList();
        for (String category : sortedCategoryKeys) {
            String categoryComment = (String)comments.get((Object)category, (Object)"");
            if (categoryComment != null && !categoryComment.isEmpty()) {
                sb.append("\n").append("# ").append(categoryComment).append("\n");
            }
            if (!category.isEmpty()) {
                sb.append("[").append(category).append("]").append("\n");
            }
            Map categoryProperties = data.row((Object)category);
            List sortedPropertyKeys = categoryProperties.keySet().stream().sorted().toList();
            for (String property : sortedPropertyKeys) {
                String propertyComment = (String)comments.get((Object)category, (Object)property);
                if (propertyComment != null && !propertyComment.isEmpty()) {
                    sb.append("\n").append("# ").append(propertyComment).append("\n");
                }
                sb.append(property).append(" = ");
                Object value = categoryProperties.get(property);
                if (value instanceof String) {
                    String stringValue = (String)value;
                    if (stringValue.contains("\n")) {
                        sb.append("\"\"\"\n").append(value).append("\n\"\"\"");
                    } else {
                        sb.append("\"").append(stringValue.replace("\"", "\\\"")).append("\"");
                    }
                } else if (value instanceof List) {
                    List listValue = (List)value;
                    NotomlSerializer.serializeList(listValue, sb);
                } else if (value instanceof Enum) {
                    Enum enumValue = (Enum)value;
                    sb.append("\"").append(enumValue.name()).append("\"");
                } else {
                    sb.append(value);
                }
                sb.append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private static String serializeList(List<?> list, StringBuilder sb) {
        boolean newLines;
        sb.append("[ ");
        boolean bl = newLines = list.size() > 3;
        if (newLines) {
            sb.append("\n");
        }
        for (int i = 0; i < list.size(); ++i) {
            if (newLines) {
                sb.append("    ");
            }
            if (list.get(i) instanceof String) {
                sb.append("\"").append(((String)list.get(i)).replace("\"", "\\\"")).append("\"");
            } else if (list.get(i) instanceof Enum) {
                sb.append("\"").append(((Enum)list.get(i)).name()).append("\"");
            } else {
                sb.append(list.get(i));
            }
            if (i != list.size() - 1) {
                sb.append(", ");
            }
            if (!newLines) continue;
            sb.append("\n");
        }
        sb.append(" ]");
        return sb.toString();
    }
}

