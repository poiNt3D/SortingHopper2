SortingHopper2
=============

Bukkit plugin that creates item filter from a hopper.

Notes
=====

Modify the config.yml for specific control over program execution. The file also includes commentary on effects of configuration options. For ease, features are listed here:

* Can prevent items from being pulled from Sorting Hopper into other hoppers

* Can prevent dropped hoppers from being picked up

* Can forcibly alter block break event for Sorting Hoppers to alter the lore on drop (note: will not play nice with block break protection systems)

* Enables custom crafting recipe for the Sorting Hopper.

Legal Stuff
===========

Pursuant to GPLv3 (ugh) just a notice that as of June 1, 2015, ProgrammerDan (alias of Daniel Boston) has begun modifying this source code.

GPLv3 still applies to all distributions, and the license as original attached will remain as such.

Merged back from Rycochet (github.com/Rycochet) on 28.11.2018

Version 2.1 changes
===================

* Moved to a separate GitHub repository to preserve simple and clean version (now refered as Sorting Hopper Classic)
* Sorting rules are now being stored in file instead of hopper's own inventory
* Save/Load rules feature via commands
* Inventory menu shows up when a player clicks a sorter
* New ItemGroup system allows sorting of similar items configured in itemgroups.yml (it replaces 2.0 torch/lever switch)
* Configurable inventory menu size
* New BreakListenerGentle added to avoid conflicts with build protection plugins. Doesn't remove hopper drop, but adds a configurable item to it.
* Changed default crafting recipe to Hopper+Comparator to match new break listener drop.
