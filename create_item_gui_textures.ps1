# PowerShell script to create placeholder GUI textures for Waystones items

Add-Type -AssemblyName System.Drawing

$outputDir = "src\main\resources\assets\waystoneinjector\textures\gui"
New-Item -ItemType Directory -Force -Path $outputDir | Out-Null

# Item colors (different from waystone blocks for distinction)
$items = @{
    'warp_scroll' = @(138, 43, 226)      # Blue violet (scroll magic)
    'bound_scroll' = @(255, 215, 0)      # Gold (bound/special)
    'warp_stone' = @(70, 130, 180)       # Steel blue (stone-like)
    'return_scroll' = @(50, 205, 50)     # Lime green (return/home)
    'warp_plate' = @(128, 128, 128)      # Gray (metal plate)
    'portstone' = @(186, 85, 211)        # Medium orchid (portal magic)
}

foreach ($itemName in $items.Keys) {
    $rgb = $items[$itemName]
    $r = $rgb[0]
    $g = $rgb[1]
    $b = $rgb[2]
    
    # Create 256x256 image
    $bitmap = New-Object System.Drawing.Bitmap(256, 256)
    $graphics = [System.Drawing.Graphics]::FromImage($bitmap)
    
    # Fill background
    $bgColor = [System.Drawing.Color]::FromArgb(200, 64, 64, 64)
    $bgBrush = New-Object System.Drawing.SolidBrush($bgColor)
    $graphics.FillRectangle($bgBrush, 0, 0, 256, 256)
    
    # Add colored border
    $borderColor = [System.Drawing.Color]::FromArgb(255, $r, $g, $b)
    $borderPen = New-Object System.Drawing.Pen($borderColor, 8)
    $graphics.DrawRectangle($borderPen, 4, 4, 248, 248)
    
    # Add item name text
    $font = New-Object System.Drawing.Font("Arial", 16, [System.Drawing.FontStyle]::Bold)
    $textBrush = New-Object System.Drawing.SolidBrush($borderColor)
    $text = $itemName.ToUpper().Replace('_', ' ')
    $textSize = $graphics.MeasureString($text, $font)
    $textX = (256 - $textSize.Width) / 2
    $textY = (256 - $textSize.Height) / 2
    $graphics.DrawString($text, $font, $textBrush, $textX, $textY)
    
    # Save as PNG
    $outputPath = Join-Path $outputDir "$itemName.png"
    $bitmap.Save($outputPath, [System.Drawing.Imaging.ImageFormat]::Png)
    
    Write-Host "Created: $outputPath (R:$r, G:$g, B:$b)"
    
    $graphics.Dispose()
    $bitmap.Dispose()
    $bgBrush.Dispose()
    $borderPen.Dispose()
    $textBrush.Dispose()
    $font.Dispose()
}

Write-Host "`nAll item GUI placeholder textures created!"
Write-Host "Replace these with your custom designs."
