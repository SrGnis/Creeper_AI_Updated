# BreachCreeperIgniteGoal

This new goal is wich allow creepers to brach walls.

A creeper can execute the goal if it:

1. Is not exploding already:

> `this.creeper.getFuseSpeed() > 0`

2. Has a target:

> `livingEntity != null`

3. Is exploding the the normal way:

> `normalExpl = this.creeper.squaredDistanceTo(livingEntity) < 9.0D`

4. or should breach a wall:

> `creeper.age > 60 && !creeper.isNavigating() && creeper.squaredDistanceTo(livingEntity) < CreeperAIUpdated.config.breachingRadius`

With the last conditions some times when a creeper sees a player starts to explode though there is a path to the player.

This happens because sometimes when the creeper gets a target and executes the goal it has not yet calculated the path to the target.

This is solved on the method `preBreakWall` where a path from the creeper to the target is calculated and if the path is not ended, forces the creeper to follow it.

The creeper only would stop exploding if:

1. Losses his target

> `this.target == null`

2. Was exploding the normal way and the target gets away

> `this.normalExpl && this.creeper.squaredDistanceTo(this.target) > 49.0D`

