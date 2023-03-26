package com.posthog.hogql.settings;

import java.util.Properties;

public enum HogQLQueryParam {

    DATABASE("database", null, String.class),

    PASSWORD("password", null, String.class),

    USER("user", null, String.class);

    private final String key;
    private final Object defaultValue;
    private final Class<?> clazz;

    <T> HogQLQueryParam(String key, T defaultValue, Class<T> clazz) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.clazz = clazz;
    }

    public String getKey() {
        return key;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }

    private String[] driverPropertyInfoChoices() {
        return clazz == Boolean.class || clazz == Boolean.TYPE ? new String[]{"true", "false"} : null;
    }

    private String driverPropertyValue(Properties properties) {
        String value = properties.getProperty(key);
        if (value == null) {
            value = defaultValue == null ? null : defaultValue.toString();
        }
        return value;
    }
}
