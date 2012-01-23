package net.minecraft.src;

import java.util.Map;

import net.minecraft.client.Minecraft;

public class mod_example extends BaseMod
{
        
        
    public mod_example()
    {
    	ModLoader.SetInGameHook(this, true, false); //sets the hook to use OnTickInGame
                
    	ModLoader.RegisterEntityID(MyMob.class, "MyMob", ModLoader.getUniqueEntityId());
    	ModLoader.RegisterEntityID(MyAnimalMob.class, "MyAnimalMob", ModLoader.getUniqueEntityId());
    	ModLoader.RegisterEntityID(MyAquaticMob.class, "MyAquaticMob", ModLoader.getUniqueEntityId());
                
    	myCustomSpawner = new CustomSpawner(); //initializes the instance of the spawner
                
    	myCustomSpawner.setMaxMobs(50); //sets the maximum number of custom mobs
    	myCustomSpawner.setMaxAnimals(40); //sets the maximum number of custom peaceful mobs
    	myCustomSpawner.setMaxAquatic(30); //sets the maximum number of custom aquatic mobs             
   
    	int freq = 10;
    	//freq is a weighted random choice. It depends on the other freqs of the list. 
    	//The higher this number, the higher the chance of that particular mob to spawn
        
    	//adds MyAnimalMob to the three specified biomes
    	myCustomSpawner.AddCustomSpawn(MyAnimalMob.class, freq, EnumCreatureType.creature, new BiomeGenBase[] {
         BiomeGenBase.forest, BiomeGenBase.plains, BiomeGenBase.taiga  });

    	//adds 'MyAnimalMob' to all the biomes except Hell and Sky
    	myCustomSpawner.AddCustomSpawn(MyAnimalMob.class, freq, EnumCreatureType.creature);

    	//adds 'MyMob' to Hell
    	myCustomSpawner.AddCustomSpawn(MyMob.class, freq, EnumCreatureType.monster, new BiomeGenBase[] { BiomeGenBase.hell });
        
    	//adds 'MyMob' to all biomes except Hell and Sky
    	myCustomSpawner.AddCustomSpawn(MyMob.class, freq, EnumCreatureType.monster);

    	//adds 'MyAquaticMob' to the Ocean and River biomes
    	myCustomSpawner.AddCustomSpawn(MyAquaticMob.class, freq, EnumCreatureType.waterCreature, new BiomeGenBase[] {
            BiomeGenBase.ocean, BiomeGenBase.river });
    	
    	//this overwrites the frequency of the MyAquaticMob in the river biome only:
    	myCustomSpawner.AddCustomSpawn(MyAquaticMob.class, 5, EnumCreatureType.waterCreature, new BiomeGenBase[] {
    		BiomeGenBase.river });
    	
    	//this removes MyAnimalMob from the biome plains
    	myCustomSpawner.RemoveCustomSpawn(MyAnimalMob.class, EnumCreatureType.creature, new BiomeGenBase[] {
    		BiomeGenBase.plains });
    	
    	//and this removes MyAnimalMob from all the biomes
    	myCustomSpawner.RemoveCustomSpawn(MyAnimalMob.class, EnumCreatureType.creature);
        	
    }

        
    public boolean OnTickInGame(float f, Minecraft minecraft)
    {
    	if (mc.theWorld.worldInfo.getWorldTime() % 100L == 0L)
        //how often to perform this code? change 100L. the higher the number, the less frequent
        {
    		//this code randomly spawns custom mobs based on the lists and maxNumbers populated above
    		//this is what you would normally need to make your mod behave like it did on Minecraft 1.7
    		myCustomSpawner.doCustomSpawning(mc.theWorld, mc.gameSettings.difficulty > 0, true);
            //the settings here are: the current world, spawns mobs, spawns peaceful creatures
    		
    		//this code will randomly spawn MyAnimalMob up to 15 times
    		myCustomSpawner.doSpecificSpawning(mc.theWorld, MyAnimalMob.class, 15, EnumCreatureType.creature);
    		
    		//There are also methods to despawn vanilla creatures:
    		//this code reactivates the despawning of vanilla creatures as in Minecraft 1.7
    		int despawned = myCustomSpawner.despawnMob(mc.theWorld);
    		System.out.println("despawned " + despawned + " vanilla mobs");
    		
    		//the despawnMob method, checks the distance of the mob to the player or the 'age' of the mob
    		//and will despawn vanilla animals that are far away from the player or old enough
    		
    		//this one despawns wolves, it will ignore tamed wolves, the ones who are near the player and the ones who are not 'old' enough
    		myCustomSpawner.despawnMob(mc.theWorld, EntityWolf.class);
    		
    		//or you can provide a list:
    		myCustomSpawner.despawnMob(mc.theWorld, EntityChicken.class, EntitySheep.class);
    		
    		//you can also count the number of a particular entity, and despawn but keeping a minimum number of creatures:
    		if(mc.theWorld.countEntities(EntitySheep.class) >10)
			{
				int y = myCustomSpawner.despawnMobWithMinimum(mc.theWorld, EntitySheep.class, 10);
				System.out.println("despawning " + y + " sheep");
			}
    		//the above code counts the number of sheep, if the number is greater than 10, it will 
    		//despawn sheep while leaving 10 alive.
        }
        
    	
    	
    	return true;
    }
        
    public void AddRenderer(Map map)
    {
        map.put(MyMob.class, new RenderMyMob(new ModelMyMob(), 0.5F));
        map.put(MyAnimalMob.class, new RenderMyAnimalMob(new ModelMyAnimalMob(), 0.5F));
        map.put(MyAquaticMob.class, new RenderMyAquaticMob(new ModelMyAquaticMob(), 0.5F));
    }

    public String Version() 
    {
        return "1.0";
    }
        
    private static CustomSpawner myCustomSpawner; 
    public static Minecraft mc = ModLoader.getMinecraftInstance();
}
