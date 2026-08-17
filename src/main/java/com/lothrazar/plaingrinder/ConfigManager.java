package com.lothrazar.plaingrinder;

import net.minecraftforge.common.config.Configuration;

public class ConfigManager {

  public static int TIMER_COOLDOWN;
  public static int MAX_STAGE;
  public static boolean BREAKABLE_HANDLE;
  public static boolean AUTOMATION_ALLOWED;

  public static void setup(Configuration config) {
    config.load();
    String cat = "general";
    AUTOMATION_ALLOWED = config.getBoolean("allowAutomation", cat, true,
        "True means automation is allowed as normal; false will disable the capabilities so hoppers, cables, pipes will not connect");
    BREAKABLE_HANDLE = config.getBoolean("breakableHandle", cat, true,
        "Can the handle break if its used too many times while the input is empty");
    TIMER_COOLDOWN = config.getInt("timerCooldown", cat, 5, 1, 80,
        "How many ticks must be in between two player interactions; this is the cooldown in between spins at maximum speed (20 ticks in one second)");
    MAX_STAGE = config.getInt("stagesPerRecipe", cat, 4, 1, 444,
        "How many stages (player uses) trigger a recipe. Each stage is one cardinal direction, meaning 4 stages is one full rotation");
    if (config.hasChanged()) {
      config.save();
    }
  }
}
