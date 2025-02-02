package xyz.ryhon.replanterplus;

import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import net.fabricmc.loader.api.FabricLoader;

public class Config {
    private final Logger logger;
    private Path configDir;
	private Path configFile;
    @Expose
    private boolean enabled;
    @Expose
	private boolean sneakToggle;
    @Expose
	private int useDelay;
    @Expose
	private boolean missingItemNotifications;
    @Expose
	private boolean autoSwitch;
    @Expose
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

			String json = Files.readString(configFile);
            Gson gson = new Gson();
            Config loadedConfig = gson.fromJson(json, Config.class);
            setConfig(loadedConfig);

		} catch (Exception e) {
			logger.error("Failed to load config", e);
		}
	}

    void save() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String json = gson.toJson(this);

        try {
            Files.createDirectories(configDir);
            Files.writeString(configFile, json);
        } catch (Exception e) {
            logger.error("Failed to save config", e);
        }
    }

    public void setConfig(Config config){
        setEnabled(config.isEnabled());
        setSneakToggle(config.isSneakToggle());
        setUseDelay(config.getUseDelay());
        setMissingItemNotifications(config.isMissingItemNotifications());
        setAutoSwitch(config.isAutoSwitch());
        setRequireSeedHeld(config.isRequireSeedHeld());
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
