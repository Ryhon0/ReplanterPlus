package xyz.ryhon.replanterplus;

import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import net.fabricmc.loader.api.FabricLoader;

public class Config {
    private final Logger logger;
    private Path configDir;
	private Path configFile;
    private boolean enabled;
	private boolean sneakToggle;
	private int useDelay;
	private boolean missingItemNotifications;
	private boolean autoSwitch;
	private boolean requireSeedHeld;

    public Config() {
        this.enabled = true;
        this.sneakToggle = true;
        this.useDelay = 4;
        this.missingItemNotifications = true;
        this.autoSwitch = true;
        this.requireSeedHeld = false;
        this.configDir = FabricLoader.getInstance().getConfigDir().resolve("replanterplus");
        this.configFile = configDir.resolve("config.json");
        this.logger = LoggerFactory.getLogger("Replanter");
    }

    void load() {
		try {
			Files.createDirectories(configDir);
			if (!Files.exists(configFile))
				return;

			String str = Files.readString(configFile);
			JsonObject jo = (JsonObject) JsonParser.parseString(str);

			if (jo.has("enabled"))
				this.setEnabled(jo.get("enabled").getAsBoolean());
			if (jo.has("sneakToggle"))
                this.setSneakToggle(jo.get("sneakToggle").getAsBoolean());
			if (jo.has("useDelay"))
                this.setUseDelay(jo.get("useDelay").getAsInt());
			if (jo.has("missingItemNotifications"))
                this.setMissingItemNotifications(jo.get("missingItemNotifications").getAsBoolean());
			if (jo.has("autoSwitch"))
                this.setAutoSwitch(jo.get("autoSwitch").getAsBoolean());
			if (jo.has("requireSeedHeld"))
                this.setRequireSeedHeld(jo.get("requireSeedHeld").getAsBoolean());
		} catch (Exception e) {
			logger.error("Failed to load config", e);
		}
	}

    void save() {
        JsonObject jo = new JsonObject();

        jo.add("enabled", new JsonPrimitive(isEnabled()));
        jo.add("sneakToggle", new JsonPrimitive(isSneakToggle()));
        jo.add("useDelay", new JsonPrimitive(getUseDelay()));
        jo.add("missingItemNotifications", new JsonPrimitive(isMissingItemNotifications()));
        jo.add("autoSwitch", new JsonPrimitive(isAutoSwitch()));
        jo.add("requireSeedHeld", new JsonPrimitive(isRequireSeedHeld()));

        try {
            Files.createDirectories(configDir);
            Files.writeString(configFile, new Gson().toJson(jo));
        } catch (Exception e) {
            logger.error("Failed to save config", e);
        }
    }

    public boolean isEnabled(){
        return this.enabled;
    }

    public void setEnabled(boolean enabled){
        this.enabled = enabled;
    }

    public void toggleEnabled(){
        this.enabled = !enabled;
    }

    public boolean isSneakToggle(){
        return this.sneakToggle;
    }

    public void setSneakToggle(boolean sneakToggle){
        this.sneakToggle = sneakToggle;
    }

    public int getUseDelay(){
        return this.useDelay;
    }

    public void setUseDelay(int useDelay){
        this.useDelay = useDelay;
    }

    public boolean isMissingItemNotifications(){
        return this.missingItemNotifications;
    }

    public void setMissingItemNotifications(boolean missingItemNotifications){
        this.missingItemNotifications = missingItemNotifications;
    }

    public boolean isAutoSwitch(){
        return this.autoSwitch;
    }

    public void setAutoSwitch(boolean autoSwitch){
        this.autoSwitch = autoSwitch;
    }

    public boolean isRequireSeedHeld(){
        return this.requireSeedHeld;
    }
    
    public void setRequireSeedHeld(boolean requireSeedHeld){
        this.requireSeedHeld = requireSeedHeld;
    }
}
