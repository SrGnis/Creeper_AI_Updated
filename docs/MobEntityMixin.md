# MobEntityMixin

This mixin is where almost all the creeper modiications are applied.

It injects code into the initialize method of LivingEntity detecting when a creeper spwawns and appling modifications to it.

Here follows a explanation of what it does:

## Spawning powered

The probability of spawning powered is only aplied to creepers that spawn naturally, NBT tags are used to apply the powered state to the creeper.

## Goal manipulation

We need to add and remove goals from the creepers, there is not a straight forward way to remove a especific goal so we use the order in which these goals are declared on the CreeperEntity class to get a hint of the position of these in the array.(A generic method to remove goals is needed)