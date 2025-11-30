# Waystone Button Injector

Forge 1.20.1 mod that adds custom configurable buttons to the Waystones selection screen.

## Features
- Adds custom buttons to the Waystones menu
- Fully configurable via config file
- Supports unlimited buttons
- Works alongside normal Waystone functionality
- **Client-side command execution** - No OP permissions required!
- Compatible with ServerRedirect mod for server transfers

## How It Works

This mod uses a client-server packet system with **built-in server redirection**:
1. Player clicks a button in the Waystones GUI
2. Client sends a packet to the server indicating which button was pressed
3. Server validates the request and sends the command back to the client
4. **Client directly connects to the new server** (no commands, no OP needed!)
5. Player is seamlessly transferred to the new server

This approach means:
- **No OP permissions required** - Everything happens client-side
- **No dependency on ServerRedirect mod** - Built-in redirection functionality
- **Works with any Minecraft server** - No special server-side setup needed (besides having this mod installed)

## Configuration

After first run, edit `config/waystoneinjector-common.toml`. By default, no buttons are configured:

```toml
[buttons]
    # Button labels (displayed on buttons)
    labels = []
    
    # Commands to execute (without leading /)
    commands = []
```

### Example Configuration

Add your own server buttons:

```toml
[buttons]
    labels = ["Server 1", "Server 2"]
    commands = [
        "redirect @s server1.example.com",
        "redirect @s server2.example.com"
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
3. Configure buttons in `config/waystoneinjector-common.toml` on both client and server (configs should match)
4. **No other dependencies needed!** ServerRedirect is NOT required - this mod handles server transfers directly
