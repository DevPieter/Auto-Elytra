package nl.devpieter.autoelytra.Config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.LinkedHashMap;

import org.jetbrains.annotations.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ConfigManager {

	private Gson gson;
	private File configFile;

	private LinkedHashMap<String, Object> defaultConfigSettings;
	private LinkedHashMap<String, Object> configSettings;

	public ConfigManager(String configName) {
		this.gson = new GsonBuilder().setPrettyPrinting().create();
		this.defaultConfigSettings = new LinkedHashMap<String, Object>();
		this.configSettings = new LinkedHashMap<String, Object>();
		this.configFile = new File(String.format("./config/%s.json", configName));
	}

	public void addConfigSetting(String key, Object value) {
		if (!defaultConfigSettings.containsKey(key))
			defaultConfigSettings.put(key, value);
	}

	public void setValue(String key, Object value, boolean saveConfig) {
		try {
			if (configSettings.containsKey(key))
				configSettings.computeIfPresent(key, (n, v) -> v = value);
			else
				configSettings.put(key, value);

			if (saveConfig)
				saveConfig();
		} catch (Exception e) {
			// TODO: Add logger
		}
	}
	
	public void setEnum(String key, Enum<?> value, boolean saveConfig) {
		try {
			if (configSettings.containsKey(key))
				configSettings.computeIfPresent(key, (n, v) -> v = value.toString());
			else
				configSettings.put(key, value.toString());

			if (saveConfig)
				saveConfig();
		} catch (Exception e) {
			// TODO: Add logger
		}
	}

	@SuppressWarnings("unchecked")
	public boolean loadConfg() {
		try (Reader reader = new FileReader(configFile)) {
			configSettings = gson.fromJson(reader, LinkedHashMap.class);
			return true;
		} catch (Exception e) {
			return false;
			// TODO: Add logger
		}
	}

	public boolean saveConfig() {
		try (Writer writer = new FileWriter(configFile)) {
			gson.toJson(configSettings, writer);
			return true;
		} catch (Exception e) {
			return false;
			// TODO: Add logger
		}
	}

	public boolean saveDefaultConfig() {
		try (Writer writer = new FileWriter(configFile)) {
			gson.toJson(defaultConfigSettings, writer);
			return true;
		} catch (Exception e) {
			return false;
			// TODO: Add logger
		}
	}

	public boolean createConfigFile(boolean saveDefaultConfig) {
		try {
			configFile.getParentFile().mkdir();
			if (configFile.createNewFile() && saveDefaultConfig)
				saveDefaultConfig();
			return true;
		} catch (Exception e) {
			return false;
			// TODO: Add logger
		}
	}

	@Nullable
	public String getString(String key, boolean fromDefault) {
		try {
			Object value = getValue(key, fromDefault);

			if (value instanceof String)
				return (String) value;

			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public int getInteger(String key, boolean fromDefault) {
		try {
			Object value = getValue(key, fromDefault);

			if (value instanceof Integer)
				return (int) value;

			else if (value instanceof Double)
				return (int) ((double) value);

			else if (value instanceof Float)
				return (int) ((float) value);

			return 0;
		} catch (Exception e) {
			return 0;
		}
	}

	public boolean getBoolean(String key, boolean fromDefault) {
		try {
			Object value = getValue(key, fromDefault);

			if (value instanceof Boolean)
				return (boolean) value;

			return false;
		} catch (Exception e) {
			return false;
		}
	}

	@Nullable
	public <T extends Enum<T>> Enum<?> getEnum(String key, Class<T> aEnum, boolean fromDefault) {
		try {
			return Enum.valueOf(aEnum, getString(key, fromDefault));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Nullable
	private Object getValue(String key, boolean fromDefault) {
		try {
			Object value = configSettings.get(key);
			if (fromDefault)
				value = defaultConfigSettings.get(key);

			if (value == null)
				value = defaultConfigSettings.get(key);
			return value;
		} catch (Exception e) {
			return null;
			// TODO: Add logger
		}
	}
}