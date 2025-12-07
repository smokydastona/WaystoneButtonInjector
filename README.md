# Runa Waystone

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

### Automatic Redirection (Built-in Death/Sleep Detection)
1. Mod detects when player dies or sleeps
2. Checks config for matching server redirect mapping
3. Client automatically connects to the configured destination server
4. Perfect for death-based server transfers!

No server-side mod required - all detection happens client-side!

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

**DO NOT build locally!** This project uses GitHub Actions for automated builds.

### To get the latest build:
1. Push your changes to GitHub
2. GitHub Actions will automatically build the mod
3. Download the JAR from:
   - GitHub Actions artifacts (for development builds)
   - GitHub Releases page (for stable releases)

### Manual local builds are NOT supported
Local builds may fail or produce inconsistent results. Always use GitHub Actions.

## Installation
1. **Client:** Install RunaWaystone in your client's `mods/` folder
2. **Server (optional):** If using Feverdream auto-redirect, install the Feverdream server mod
3. Configure buttons in `config/waystoneinjector-client.toml`
4. **That's it!** No server-side installation of RunaWaystone needed

## Death/Sleep Redirection
Configure automatic server redirects when you die or sleep using the `feverdream.redirects` config option. Each server can have separate death and sleep destinations. Example:

```toml
[feverdream]
    redirects = [
        "death:survival.example.com->hub.example.com",
        "sleep:creative.example.com->lobby.example.com"
    ]
    deathCount = 1
```
