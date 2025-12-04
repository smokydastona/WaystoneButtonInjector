package com.example.waystoneinjector.client.gui.widget;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

/**
 * Enhanced text field for waystone search
 * Phase 4: Search box with right-click clear and real-time filtering
 */
@SuppressWarnings("null")
public class WaystoneSearchField extends EditBox {
    
    private Consumer<String> onSearchChanged;
    
    public WaystoneSearchField(Font font, int x, int y, int width, int height) {
        super(font, x, y, width, height, Component.literal("Search waystones..."));
        this.setHint(Component.literal("Search waystones..."));
    }
    
    public void setOnSearchChanged(Consumer<String> callback) {
        this.onSearchChanged = callback;
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Right-click to clear
        if (button == 1 && this.isMouseOver(mouseX, mouseY)) {
            this.setValue("");
            if (onSearchChanged != null) {
                onSearchChanged.accept("");
            }
            return true;
        }
        
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    @Override
    public void insertText(String text) {
        super.insertText(text);
        notifySearchChanged();
    }
    
    @Override
    public void deleteChars(int numChars) {
        super.deleteChars(numChars);
        notifySearchChanged();
    }
    
    @Override
    public void setValue(String value) {
        super.setValue(value);
        notifySearchChanged();
    }
    
    private void notifySearchChanged() {
        if (onSearchChanged != null) {
            onSearchChanged.accept(this.getValue());
        }
    }
}
