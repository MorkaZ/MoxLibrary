# MoxLibrary
MoxLibrary is a utility library for spigot/bukkit minecraft server plugins. To use it, you need to add dependency into your project.

Please, if you want to use it in multiple plugins or use other MoxPlugins, download and install MoxCore and use it as dependency because this plugin extends MoxLibrary and is created only for that. Class loader will load all classes of MoxLibrary just once, so MoxLibrary can be used once per server - please, keep it in mind. If server will have 2 plugins that have exported MoxLibrary into own source and if MoxLibrary has different version in those plugins, then it may produce errors with no existing methods in some classes on their usage by relatively later loaded plugin.
