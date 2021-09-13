package nl.devpieter.autoelytra.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import nl.devpieter.autoelytra.Utils.LogUtils;

public class ConfigManager {

	private Properties properties = new Properties();
	private File configFile = new File("./config/AutoElytra.config");

	public ConfigManager() {
		createConfig();
		try {
			properties.load(new FileInputStream(configFile));

			for (Settings setting : Settings.values())
				setValue(setting, properties.get(setting.key).toString(), false);

			saveConfig();
		} catch (Exception e) {
			LogUtils.logError("An error occurred while loading the config file.");
		}
	}

	public void saveConfig() {
		createConfig();
		try {
			for (Settings setting : Settings.values())
				properties.setProperty(setting.key, setting.value.toString());

			properties.store(new FileOutputStream(configFile), "Auto Elytra By DevPieter");
		} catch (Exception e) {
			LogUtils.logError("An error occurred while saving the config file.");
		}
	}

	private void createConfig() {
		try {
			if (configFile.createNewFile()) {
				System.out.println("Config file created: " + configFile.getName());
				saveConfig();
			} else
				System.out.println("Config file already exists.");
		} catch (Exception e) {
			LogUtils.logError("An error occurred while creating the config file.");
		}
	}

	public void setValue(Settings settings, String value, boolean autoSave) {
		properties.setProperty(settings.key, value);
		settings.value = value;

		if (autoSave)
			saveConfig();
	}
}