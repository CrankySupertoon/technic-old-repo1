Xie's Mod SMP 0.2 [Client] (for Minecraft v1.0.0, requires Risugami's ModLoader)

This is the core of my farming mod, plus the other mods that are to follow. It's comprised of a number of flexible classes that extend the functionality of vanilla blocks and items, and a JSON parser that reads in one or more configuration files and turns them into in-game content. The benefit of doing things this way should be apparent - modifying content no longer requires me to recompile and re-release the mod, all you need to do is download a new configuration file, or even edit it yourself. Better yet, additional content may also be added with extra configuration files and images!

Installation Instructions:

1. Download and install Risugami's ModLoader, if you haven't already.
2. Copy the "Xie" directory found in this same zip file, and all of its contents, into your .minecraft/mods directory. (Where ".minecraft" refers to your Minecraft home directory, the one that contains the directories bin,resources,saves etc).

This mod supports both single and multiplayer.
For SSP support only, copy the file "/Xie/wrappers/XieBaseMod.class.SSP.bkp" into /Xie and rename it to XieBaseMod.class (replacing the existing file if necessary).
For SMP and SSP support, copyp the file "/Xie/wrappers/XieBaseMod.class.SMP.bkp" into /Xie and rename it to XieBaseMod.class (replacing the existing file if necessary), though this will require ModLoaderMp to also be installed (see below).

For Risugami's ModLoader:
http://www.minecraftforum.net/topic/75440-v181-risugamis-mods-everything-updated/

For ModLoaderMP:
http://www.minecraftforum.net/topic/86765-181-sdks-mods-v1-finally-updated/

Xie's Mod is compatible with (but does not require) the Minecraft Forge API v1.1.2.

For more information on Xie's Mod please visit my forum thread:

http://www.minecraftforum.net/topic/477267-v18-xies-mods/