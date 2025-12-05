# PowerShell script to create semi-transparent sharestone color overlay textures
# These will appear behind the main sharestone.png texture

Add-Type -AssemblyName System.Drawing

# Minecraft dye colors (RGB values)
$colors = @{
    'black' = @(25, 25, 25)
    'blue' = @(51, 76, 178)
    'brown' = @(102, 76, 51)
    'cyan' = @(76, 127, 153)
    'gray' = @(76, 76, 76)
    'green' = @(102, 127, 51)
    'light_blue' = @(102, 153, 216)
    'light_gray' = @(153, 153, 153)
    'lime' = @(127, 204, 25)
    'magenta' = @(178, 76, 216)
    'orange' = @(216, 127, 51)
    'pink' = @(242, 127, 165)
    'purple' = @(127, 63, 178)
    'red' = @(153, 51, 51)
    'white' = @(255, 255, 255)
    'yellow' = @(229, 229, 51)
}

# Create output directory
$outputDir = "src\main\resources\assets\waystoneinjector\textures\gui\sharestone_colors"
New-Item -ItemType Directory -Force -Path $outputDir | Out-Null

foreach ($colorName in $colors.Keys) {
    $rgb = $colors[$colorName]
    $r = $rgb[0]
    $g = $rgb[1]
    $b = $rgb[2]
    
    # Create 256x256 image with semi-transparent color (50% alpha)
    $bitmap = New-Object System.Drawing.Bitmap(256, 256)
    $graphics = [System.Drawing.Graphics]::FromImage($bitmap)
    
    # Fill with semi-transparent color (alpha = 128 for 50% transparency)
    $color = [System.Drawing.Color]::FromArgb(128, $r, $g, $b)
    $brush = New-Object System.Drawing.SolidBrush($color)
    $graphics.FillRectangle($brush, 0, 0, 256, 256)
    
    # Save as PNG
    $outputPath = Join-Path $outputDir "$colorName.png"
    $bitmap.Save($outputPath, [System.Drawing.Imaging.ImageFormat]::Png)
    
    Write-Host "Created: $outputPath (R:$r, G:$g, B:$b, A:128)"
    
    $graphics.Dispose()
    $bitmap.Dispose()
    $brush.Dispose()
}

Write-Host "`nAll sharestone color overlay textures created successfully!"
Write-Host "These will render behind sharestone.png with 50% transparency."
