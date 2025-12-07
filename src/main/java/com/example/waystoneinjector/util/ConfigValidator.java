package com.example.waystoneinjector.util;

import com.example.waystoneinjector.config.WaystoneConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Validates configuration values to prevent crashes and misconfigurations
 * Ensures all button configs are valid before use
 */
public class ConfigValidator {
    
    // Regex patterns for validation
    private static final Pattern SERVER_ADDRESS_PATTERN = Pattern.compile(
        "^([a-zA-Z0-9.-]+)(:\\d{1,5})?$"
    );
    private static final Pattern WAYSTONE_NAME_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_ ]+$"
    );
    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile(
        "^0x[0-9A-Fa-f]{6}$"
    );
    
    /**
     * Validation result container
     */
    public static class ValidationResult {
        public final boolean isValid;
        public final List<String> errors;
        public final List<String> warnings;
        
        public ValidationResult() {
            this.isValid = true;
            this.errors = new ArrayList<>();
            this.warnings = new ArrayList<>();
        }
        
        private ValidationResult(boolean isValid, List<String> errors, List<String> warnings) {
            this.isValid = isValid;
            this.errors = errors;
            this.warnings = warnings;
        }
        
        public static ValidationResult invalid(List<String> errors, List<String> warnings) {
            return new ValidationResult(false, errors, warnings);
        }
        
        public boolean hasErrors() {
            return !errors.isEmpty();
        }
        
        public boolean hasWarnings() {
            return !warnings.isEmpty();
        }
    }
    
    /**
     * Button configuration container
     */
    public static class ButtonConfig {
        public final boolean enabled;
        public final String label;
        public final String command;
        public final int width;
        public final int height;
        public final int xOffset;
        public final int yOffset;
        public final String textColor;
        public final String side;
        public final String serverAddress;
        public final String deathRedirect;
        public final String sleepRedirect;
        public final int sleepChance;
        
        public ButtonConfig(boolean enabled, String label, String command, int width, int height,
                           int xOffset, int yOffset, String textColor, String side,
                           String serverAddress, String deathRedirect, String sleepRedirect, int sleepChance) {
            this.enabled = enabled;
            this.label = label;
            this.command = command;
            this.width = width;
            this.height = height;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.textColor = textColor;
            this.side = side;
            this.serverAddress = serverAddress;
            this.deathRedirect = deathRedirect;
            this.sleepRedirect = sleepRedirect;
            this.sleepChance = sleepChance;
        }
    }
    
    /**
     * Validate a single button configuration
     */
    public static ValidationResult validateButton(int buttonNum, ButtonConfig config) {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        
        if (!config.enabled) {
            // Skip validation for disabled buttons
            return new ValidationResult();
        }
        
        String prefix = "Button " + buttonNum;
        
        // Validate label
        if (config.label == null || config.label.trim().isEmpty()) {
            errors.add(prefix + ": Label is empty");
        } else if (config.label.length() > 50) {
            warnings.add(prefix + ": Label is very long (" + config.label.length() + " chars) - may overflow");
        }
        
        // Validate command
        if (config.command == null || config.command.trim().isEmpty()) {
            errors.add(prefix + ": Command is empty");
        } else {
            String cmd = config.command.trim();
            
            // Check if it's a redirect command
            if (cmd.startsWith("redirect ")) {
                String[] parts = cmd.split("\\s+");
                if (parts.length < 3) {
                    errors.add(prefix + ": Invalid redirect command format. Expected: 'redirect @s server:port'");
                } else {
                    String target = parts[1];
                    String address = parts[2];
                    
                    if (!target.equals("@s")) {
                        warnings.add(prefix + ": Redirect target '" + target + "' should typically be '@s'");
                    }
                    
                    if (!SERVER_ADDRESS_PATTERN.matcher(address).matches()) {
                        errors.add(prefix + ": Invalid server address '" + address + "'. Expected format: 'domain.com:25565'");
                    }
                }
            } else if (cmd.contains("/")) {
                warnings.add(prefix + ": Command starts with '/' which may cause issues. Remove the leading slash.");
            }
        }
        
        // Validate dimensions
        if (config.width < 20 || config.width > 200) {
            errors.add(prefix + ": Width (" + config.width + ") out of range (20-200)");
        }
        if (config.height < 15 || config.height > 100) {
            errors.add(prefix + ": Height (" + config.height + ") out of range (15-100)");
        }
        
        // Validate offsets
        if (Math.abs(config.xOffset) > 500) {
            warnings.add(prefix + ": X offset (" + config.xOffset + ") is very large - button may be off-screen");
        }
        if (Math.abs(config.yOffset) > 500) {
            warnings.add(prefix + ": Y offset (" + config.yOffset + ") is very large - button may be off-screen");
        }
        
        // Validate text color
        if (config.textColor != null && !config.textColor.trim().isEmpty()) {
            if (!HEX_COLOR_PATTERN.matcher(config.textColor).matches()) {
                errors.add(prefix + ": Invalid text color '" + config.textColor + "'. Expected format: '0xFFFFFF'");
            }
        }
        
        // Validate side
        if (!config.side.equals("auto") && !config.side.equals("left") && !config.side.equals("right")) {
            errors.add(prefix + ": Invalid side '" + config.side + "'. Must be 'auto', 'left', or 'right'");
        }
        
        // Validate server address (if set)
        if (config.serverAddress != null && !config.serverAddress.trim().isEmpty()) {
            if (!SERVER_ADDRESS_PATTERN.matcher(config.serverAddress).matches()) {
                errors.add(prefix + ": Invalid server address '" + config.serverAddress + "'");
            }
        }
        
        // Validate death redirect
        if (config.deathRedirect != null && !config.deathRedirect.trim().isEmpty()) {
            if (config.serverAddress == null || config.serverAddress.trim().isEmpty()) {
                warnings.add(prefix + ": Death redirect set but no server address specified - will apply globally");
            }
        }
        
        // Validate sleep redirect
        if (config.sleepRedirect != null && !config.sleepRedirect.trim().isEmpty()) {
            if (config.serverAddress == null || config.serverAddress.trim().isEmpty()) {
                warnings.add(prefix + ": Sleep redirect set but no server address specified - will apply globally");
            }
            
            if (config.sleepChance < 0 || config.sleepChance > 100) {
                errors.add(prefix + ": Sleep chance (" + config.sleepChance + "%) out of range (0-100)");
            }
        }
        
        // Log validation results
        for (String error : errors) {
            DebugLogger.configValidation(prefix, false, error);
        }
        for (String warning : warnings) {
            DebugLogger.warn("Config validation warning: " + warning);
        }
        
        if (errors.isEmpty()) {
            DebugLogger.configValidation(prefix, true, "All checks passed");
            return new ValidationResult();
        } else {
            return ValidationResult.invalid(errors, warnings);
        }
    }
    
    /**
     * Validate all button configurations
     */
    public static ValidationResult validateAllButtons() {
        DebugLogger.section("CONFIG VALIDATION");
        
        List<String> allErrors = new ArrayList<>();
        List<String> allWarnings = new ArrayList<>();
        int validButtons = 0;
        int enabledButtons = 0;
        
        // Validate all 6 buttons
        for (int i = 1; i <= 6; i++) {
            ButtonConfig config = getButtonConfig(i);
            if (config.enabled) {
                enabledButtons++;
                ValidationResult result = validateButton(i, config);
                allErrors.addAll(result.errors);
                allWarnings.addAll(result.warnings);
                if (!result.hasErrors()) {
                    validButtons++;
                }
            }
        }
        
        DebugLogger.info("Enabled buttons: " + enabledButtons + ", Valid buttons: " + validButtons);
        
        if (allErrors.isEmpty()) {
            DebugLogger.success("All enabled buttons passed validation");
            return new ValidationResult();
        } else {
            DebugLogger.failure("Config validation found " + allErrors.size() + " error(s)");
            return ValidationResult.invalid(allErrors, allWarnings);
        }
    }
    
    /**
     * Get button configuration from ForgeConfigSpec
     */
    private static ButtonConfig getButtonConfig(int buttonNum) {
        switch (buttonNum) {
            case 1:
                return new ButtonConfig(
                    WaystoneConfig.BUTTON1_ENABLED.get(),
                    WaystoneConfig.BUTTON1_LABEL.get(),
                    WaystoneConfig.BUTTON1_COMMAND.get(),
                    WaystoneConfig.BUTTON1_WIDTH.get(),
                    WaystoneConfig.BUTTON1_HEIGHT.get(),
                    WaystoneConfig.BUTTON1_X_OFFSET.get(),
                    WaystoneConfig.BUTTON1_Y_OFFSET.get(),
                    WaystoneConfig.BUTTON1_TEXT_COLOR.get(),
                    WaystoneConfig.BUTTON1_SIDE.get(),
                    WaystoneConfig.BUTTON1_SERVER_ADDRESS.get(),
                    WaystoneConfig.BUTTON1_DEATH_REDIRECT.get(),
                    WaystoneConfig.BUTTON1_SLEEP_REDIRECT.get(),
                    WaystoneConfig.BUTTON1_SLEEP_CHANCE.get()
                );
            case 2:
                return new ButtonConfig(
                    WaystoneConfig.BUTTON2_ENABLED.get(),
                    WaystoneConfig.BUTTON2_LABEL.get(),
                    WaystoneConfig.BUTTON2_COMMAND.get(),
                    WaystoneConfig.BUTTON2_WIDTH.get(),
                    WaystoneConfig.BUTTON2_HEIGHT.get(),
                    WaystoneConfig.BUTTON2_X_OFFSET.get(),
                    WaystoneConfig.BUTTON2_Y_OFFSET.get(),
                    WaystoneConfig.BUTTON2_TEXT_COLOR.get(),
                    WaystoneConfig.BUTTON2_SIDE.get(),
                    WaystoneConfig.BUTTON2_SERVER_ADDRESS.get(),
                    WaystoneConfig.BUTTON2_DEATH_REDIRECT.get(),
                    WaystoneConfig.BUTTON2_SLEEP_REDIRECT.get(),
                    WaystoneConfig.BUTTON2_SLEEP_CHANCE.get()
                );
            case 3:
                return new ButtonConfig(
                    WaystoneConfig.BUTTON3_ENABLED.get(),
                    WaystoneConfig.BUTTON3_LABEL.get(),
                    WaystoneConfig.BUTTON3_COMMAND.get(),
                    WaystoneConfig.BUTTON3_WIDTH.get(),
                    WaystoneConfig.BUTTON3_HEIGHT.get(),
                    WaystoneConfig.BUTTON3_X_OFFSET.get(),
                    WaystoneConfig.BUTTON3_Y_OFFSET.get(),
                    WaystoneConfig.BUTTON3_TEXT_COLOR.get(),
                    WaystoneConfig.BUTTON3_SIDE.get(),
                    WaystoneConfig.BUTTON3_SERVER_ADDRESS.get(),
                    WaystoneConfig.BUTTON3_DEATH_REDIRECT.get(),
                    WaystoneConfig.BUTTON3_SLEEP_REDIRECT.get(),
                    WaystoneConfig.BUTTON3_SLEEP_CHANCE.get()
                );
            case 4:
                return new ButtonConfig(
                    WaystoneConfig.BUTTON4_ENABLED.get(),
                    WaystoneConfig.BUTTON4_LABEL.get(),
                    WaystoneConfig.BUTTON4_COMMAND.get(),
                    WaystoneConfig.BUTTON4_WIDTH.get(),
                    WaystoneConfig.BUTTON4_HEIGHT.get(),
                    WaystoneConfig.BUTTON4_X_OFFSET.get(),
                    WaystoneConfig.BUTTON4_Y_OFFSET.get(),
                    WaystoneConfig.BUTTON4_TEXT_COLOR.get(),
                    WaystoneConfig.BUTTON4_SIDE.get(),
                    WaystoneConfig.BUTTON4_SERVER_ADDRESS.get(),
                    WaystoneConfig.BUTTON4_DEATH_REDIRECT.get(),
                    WaystoneConfig.BUTTON4_SLEEP_REDIRECT.get(),
                    WaystoneConfig.BUTTON4_SLEEP_CHANCE.get()
                );
            case 5:
                return new ButtonConfig(
                    WaystoneConfig.BUTTON5_ENABLED.get(),
                    WaystoneConfig.BUTTON5_LABEL.get(),
                    WaystoneConfig.BUTTON5_COMMAND.get(),
                    WaystoneConfig.BUTTON5_WIDTH.get(),
                    WaystoneConfig.BUTTON5_HEIGHT.get(),
                    WaystoneConfig.BUTTON5_X_OFFSET.get(),
                    WaystoneConfig.BUTTON5_Y_OFFSET.get(),
                    WaystoneConfig.BUTTON5_TEXT_COLOR.get(),
                    WaystoneConfig.BUTTON5_SIDE.get(),
                    WaystoneConfig.BUTTON5_SERVER_ADDRESS.get(),
                    WaystoneConfig.BUTTON5_DEATH_REDIRECT.get(),
                    WaystoneConfig.BUTTON5_SLEEP_REDIRECT.get(),
                    WaystoneConfig.BUTTON5_SLEEP_CHANCE.get()
                );
            case 6:
                return new ButtonConfig(
                    WaystoneConfig.BUTTON6_ENABLED.get(),
                    WaystoneConfig.BUTTON6_LABEL.get(),
                    WaystoneConfig.BUTTON6_COMMAND.get(),
                    WaystoneConfig.BUTTON6_WIDTH.get(),
                    WaystoneConfig.BUTTON6_HEIGHT.get(),
                    WaystoneConfig.BUTTON6_X_OFFSET.get(),
                    WaystoneConfig.BUTTON6_Y_OFFSET.get(),
                    WaystoneConfig.BUTTON6_TEXT_COLOR.get(),
                    WaystoneConfig.BUTTON6_SIDE.get(),
                    WaystoneConfig.BUTTON6_SERVER_ADDRESS.get(),
                    WaystoneConfig.BUTTON6_DEATH_REDIRECT.get(),
                    WaystoneConfig.BUTTON6_SLEEP_REDIRECT.get(),
                    WaystoneConfig.BUTTON6_SLEEP_CHANCE.get()
                );
            default:
                throw new IllegalArgumentException("Invalid button number: " + buttonNum);
        }
    }
}
