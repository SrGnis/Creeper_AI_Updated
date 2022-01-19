# Creeper AI Updated developer guide (Fabric)
Welcome to the Creeper AI Updated developper guide.

Here you will find information on what this project does and how it does it.

With the help of this guide you can understand and collaborate in this project more easily.

## Overview

This mod modifies and adds different Creeper goals in order for them to be able to detect players through walls and destroy these walls to breach the player defenses.

Other modifications that this mods adds are:

- Creeper explosions create fire.
- Adjustable viewing distance.
- Creepers leap at targets like spiders do.
- Adds probability of creepers spawning powered.

## Project architecture

All the modifications are done using mixins and new goals, the main class is only used to load the user configuration.

There are two important mixins which do all the work, the rest are just Acessors wich grant acces to protected values/metods.

### WorldMixin

Checks if the creeper explosion should create fire.

It just changes the createfire bool in the method createExplosion of World if the entity which created the explosion is a creeper and if the config states that.

### MobEntityMixin

Detects when a mob spawns, checks if is a creeper and applies the modifications to it. More info [here](MobEntityMixin.md).

### XrayFollowTargetGoal

Responsible of allowing creepers to see trough walls.

It just creates a FollowTargetGoal and add the .ignoreVisibility() predicate to it to allow the mob to see trough walls.

### BreachCreeperIgniteGoal

Has all the logic that allows creepers breach walls. More info [here](BreachCreeperIgniteGoal.md).
