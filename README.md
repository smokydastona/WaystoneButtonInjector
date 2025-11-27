# Waystone Button Injector

Forge 1.20.1 mod that adds custom configurable buttons to the Waystones selection screen.

## Features
- Adds custom buttons to the Waystones menu
- Fully configurable via config file
- Supports unlimited buttons
- Works alongside normal Waystone functionality

## Configuration

After first run, edit `config/waystoneinjector-common.toml`:

```toml
[buttons]
    # Button labels (displayed on buttons)
    labels = ["Chaos Town", "The Undergrown"]
    
    # Commands to execute (without leading /)
    commands = [
        "redirect @s chaostowntest.modrinth.gg",
        "redirect @s 51.222.244.61:10020"
    ]
```

Add as many buttons as needed - just ensure labels and commands arrays have the same length.

## Build
1. Install Java 17
2. Run `gradlew.bat build` (Windows) or `./gradlew build` (Linux/Mac)
3. Find jar in `build/libs/`

## Installation
1. Install on both client and server
2. Requires Waystones mod installed
3. Server needs `redirect` command (ServerRedirect or similar mod)

## Server Addresses
- Chaos Town: `chaostowntest.modrinth.gg`
- The Undergrown: `51.222.244.61:10020`

Edit `WaystoneButtonHandler.java` to change addresses.
