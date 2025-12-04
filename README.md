# Waystone Button Injector

Forge 1.20.1 **client-side mod** that adds custom configurable buttons to the Waystones selection screen and listens for server redirect packets.

## Features
- Adds custom buttons to the Waystones menu
- Fully configurable via config file
- Supports unlimited buttons
- Works alongside normal Waystone functionality
- **Client-side only** - No server installation required!
- **Built-in server redirection** - No OP permissions needed
- **Feverdream Respawn compatibility** - Listens for automatic redirect packets from server

## How It Works

### Manual Redirection (Waystone Buttons)
1. Player clicks a button in the Waystones GUI
2. **Client directly connects to the new server** (no commands, no OP needed!)
3. Player is seamlessly transferred to the new server

### Automatic Redirection (Feverdream Integration)
1. Server-side Feverdream mod sends a redirect packet when player respawns
2. WaystoneButtonInjector receives the packet on channel `feverdreamrespawn:main`
3. Packet can contain either:
   - A server address (e.g., "feverdream" or "hub.example.com")
   - A secret button ID (6-11) to trigger one of the configured buttons
4. Client automatically connects to the specified server
5. Perfect for death-based server transfers!

**Secret Buttons (IDs 6-11):**
- Button 6 mirrors the command from visible Button 1
- Button 7 mirrors the command from visible Button 2
- Button 8 mirrors the command from visible Button 3
- Button 9 mirrors the command from visible Button 4
- Button 10 mirrors the command from visible Button 5
- Button 11 mirrors the command from visible Button 6

The Feverdream mod can send packet with value "6" through "11" to trigger the corresponding configured button without the player needing to open the Waystone menu.

This approach means:
- **No OP permissions required** - Everything happens client-side
- **No server-side installation needed** - Purely client-side mod
- **Works with any Minecraft server** - No special setup needed

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
1. **Client:** Install WaystoneButtonInjector in your client's `mods/` folder
2. **Server (optional):** If using Feverdream auto-redirect, install the Feverdream server mod
3. Configure buttons in `config/waystoneinjector-client.toml`
4. **That's it!** No server-side installation of WaystoneButtonInjector needed

## Feverdream Respawn Integration
This mod is compatible with the [FeverDream_WSBI-Compat](https://github.com/smokydastona/FeverDream_WSBI-Compat) server-side mod, which automatically redirects players to another server when they respawn after death. No additional configuration needed - just install both mods!
