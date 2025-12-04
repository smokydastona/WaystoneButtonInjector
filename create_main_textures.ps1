# PowerShell script to generate main waystone GUI background textures
Add-Type -AssemblyName System.Drawing

$outputPath = "src\main\resources\assets\waystoneinjector\textures\gui"

# Define waystone types with their RGB colors
$waystoneColors = @{
    'waystone_regular' = @(139, 115, 85)      # Sandy brown
    'waystone_mossy' = @(85, 107, 47)          # Dark olive green
    'waystone_blackstone' = @(42, 42, 48)      # Dark gray
    'waystone_deepslate' = @(60, 60, 60)       # Dark slate gray
    'waystone_endstone' = @(221, 223, 165)     # Pale yellow
    'sharestone' = @(138, 43, 226)             # Blue violet/purple
}

foreach ($waystoneType in $waystoneColors.Keys) {
    $rgb = $waystoneColors[$waystoneType]
    
    # Create 256x256 bitmap for main GUI
    $bitmap = New-Object System.Drawing.Bitmap(256, 256)
    $graphics = [System.Drawing.Graphics]::FromImage($bitmap)
    $graphics.SmoothingMode = [System.Drawing.Drawing2D.SmoothingMode]::AntiAlias
    
    # Create colors with varying opacity for depth
    $baseColor = [System.Drawing.Color]::FromArgb(220, $rgb[0], $rgb[1], $rgb[2])
    $darkColor = [System.Drawing.Color]::FromArgb(200, [int]($rgb[0] * 0.6), [int]($rgb[1] * 0.6), [int]($rgb[2] * 0.6))
    $lightColor = [System.Drawing.Color]::FromArgb(180, [Math]::Min(255, $rgb[0] + 40), [Math]::Min(255, $rgb[1] + 40), [Math]::Min(255, $rgb[2] + 40))
    $highlightColor = [System.Drawing.Color]::FromArgb(100, 255, 255, 255)
    
    # Create brushes
    $baseBrush = New-Object System.Drawing.SolidBrush($baseColor)
    $darkBrush = New-Object System.Drawing.SolidBrush($darkColor)
    $lightBrush = New-Object System.Drawing.SolidBrush($lightColor)
    $highlightBrush = New-Object System.Drawing.SolidBrush($highlightColor)
    
    # Fill base
    $graphics.FillRectangle($baseBrush, 0, 0, 256, 256)
    
    # Add texture pattern (stone-like tiles)
    for ($y = 0; $y -lt 256; $y += 32) {
        for ($x = 0; $x -lt 256; $x += 32) {
            # Alternate dark/light tiles
            if ((($x + $y) / 32) % 2 -eq 0) {
                $graphics.FillRectangle($darkBrush, $x, $y, 32, 32)
            } else {
                $graphics.FillRectangle($lightBrush, $x, $y, 32, 32)
            }
            
            # Add border to tile
            $tilePen = New-Object System.Drawing.Pen($darkColor, 1)
            $graphics.DrawRectangle($tilePen, $x, $y, 31, 31)
            $tilePen.Dispose()
        }
    }
    
    # Add GUI frame border
    $framePen = New-Object System.Drawing.Pen($darkColor, 4)
    $graphics.DrawRectangle($framePen, 2, 2, 252, 252)
    
    # Add inner highlight
    $graphics.FillRectangle($highlightBrush, 8, 8, 240, 4)
    
    # Save as PNG
    $filename = "$outputPath\$waystoneType.png"
    $bitmap.Save($filename, [System.Drawing.Imaging.ImageFormat]::Png)
    
    Write-Host "Created $waystoneType.png"
    
    # Cleanup
    $graphics.Dispose()
    $bitmap.Dispose()
    $baseBrush.Dispose()
    $darkBrush.Dispose()
    $lightBrush.Dispose()
    $highlightBrush.Dispose()
    $framePen.Dispose()
}

Write-Host "`nAll 6 main GUI textures created successfully!" -ForegroundColor Green
