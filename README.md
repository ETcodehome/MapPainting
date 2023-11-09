This project produces a plugin for Java Minecraft servers that allows users to "paint" in an immersive way inside Minecraft.

Players buy and mix different coloured paints, then use those paints on special canvases to generate amazing artworks that massively enhance player experiences by allowing them to customise the world around them. Ideal for adding a layer of personalisation to your spaces. 

Common example uses:
- Giant portraits & artworks
- Signage composed of indidivdual letters
- Decorative items such as custom painted bear rugs, pots and pans on walls, dinner plates
- Custom banner designs

# CURRENTLY WIP
The plugin is currently being updated from a local branch that has been in use for a number of years. 
I would recommend waiting for a formal release rather than using it in its current state.

# Configuration
Config.yml in MapPainting plugin folder. Values fairly obvious.  

# Requirements
- Worldguard
- Worldedit
- Optional Vault based economy system hooks

# Commands
TBC

# Permission Nodes
TBC

# Shapeless crafting grid recipes
Multiple Paint Bottles = Average color Paint Bottle result.
Paint Bottle + Milk Bucket = Paint Bucket of Paint Bottle color.
Paint Bottle + Paint Bucket = Paint Bucket of Paint Bottle color averaged with Paint Bottle color.
Multiple Paint Bottles + Water Bucket = Add color value from each Paint Bottle.

# Interactions
Paint Bottle - Right click on a canvas to paint a single pixel of the paint bottle color on a canvas.
Paint Bucket - Flood fills an area of a canvas at the right click location.
Item Frame - When holding in the main hand and right clicking on a painting canvas, frames or deframes the image. Consumes the frame.
Shears - Works like paint bottle but right clicking a canvas trims pixels back to transparent
Water Bottle - Same as shears
Drink Paint Bucket - Recieve a random negative potion effect. Consumes the paint bucket contents.
Nametag named in an anvil - Updates the painting to show the name of the name tag when right clicked. Consumes the tag.

# Localization
All user visible strings should be customizable inside the auto generated config file. 
English default strings are provided.

# Valid Colors
There are 247 valid colors + transparent. This is based on the Minecraft implmentation of map colors and is not something I as a plugin author can extend. The known colors have all been named to make them easily distinguished and provide a fun minigame to find them all. In the RGB color system each channel has 0-255 values for red, green, and blue which defines the intensity of the color. This means that there are 256 x 256 x 256 = 16777216 possible color combinations but ultimately these will convert back to the closest of the supported 247 colors if painted.

# Implementation Notes / FAQ

- Paintings are just bulk standard normal Minecraft filled maps.
Under the hood this plugin works by substituting a framed filled map object for the canvas in a players hand when they place it on a block. This canvas is then filled with a map stored separately inside the plugin. In terms of the real object placed into the world, it is not different to any other normal minecraft placed map, except the server sends a different image (their painted image) rather than the typical map contents. 

- Paint colors are stored in item lore.
Paint works by reading the item lore on items when interacting with filled maps, specifically potions and milk buckets. If the paint lore conforms to the expected pattern it is considered a valid paint color for the purposes of painting on map canvases and the interaction completes.

- Safe to remove.
Because we're using normal, properly supported items this means that if the plugin is ever removed, the maps just show as the normal filled map of the same ID, and the lore ceases to function for painting on map canvases.

# Credit
This plugin wouldn't have been possible without all past contributors, and the original plugin author. 
https://github.com/aegistudio/MapPainting
A lot of people have touched this code over the years, I apologise if you have been missed. 
Please reach out and let me know and I will update the related documentation.

# Licensing
In line with the original bukkit page for the plugin, the plugin builds and all related source code is licensed under GPL v3.
https://www.spigotmc.org/resources/map-painting-scripting-api.19823/
![image](https://github.com/ETcodehome/MapPainting/assets/16591000/f24cc599-d91f-42c2-9a86-d802747ef115)
For more information see https://www.gnu.org/licenses/gpl-3.0.en.html

# Fork Philosophy
Produce a Minecraft plugin for painting on maps inside the game.
Do what it does well and remove feature creep that is distracting from the plugins core promise.
In line with this, a lot of features that distracted from map painting have been stripped from this branch.

# To Build
Add the project to eclipse, update external library refs and javadoc locations to target versions. Export to jar.
