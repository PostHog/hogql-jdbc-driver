package com.posthog.hogql.settings;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class HogQLProperties {

    // connection settings
    private String host;
//    private String personalApiKey;
//    private int teamId;

    // queries settings
    private String database;
    private String user;
    private String password;

    public HogQLProperties() {
        this(new Properties());
    }

    public HogQLProperties(Properties info) {
        this.database = getSetting(info, HogQLQueryParam.DATABASE);
        this.user = getSetting(info, HogQLQueryParam.USER);
        this.password = getSetting(info, HogQLQueryParam.PASSWORD);
    }

    public Map<HogQLQueryParam, String> buildQueryParams(boolean ignoreDatabase) {
        Map<HogQLQueryParam, String> params = new HashMap<>();

        if (!(database == null || database.isEmpty()) && !ignoreDatabase) {
            params.put(HogQLQueryParam.DATABASE, getDatabase());
        }
        if (user != null) {
            params.put(HogQLQueryParam.USER, user);
        }
        if (password != null) {
            params.put(HogQLQueryParam.PASSWORD, password);
        }
        return params;
    }

    private <T> T getSetting(Properties info, HogQLQueryParam param) {
        return getSetting(info, param.getKey(), param.getDefaultValue(), param.getClazz());
    }

    private <T> T getSetting(Properties info, String key, Object defaultValue, Class clazz) {
        String val = info.getProperty(key);
        if (val == null) {
            return (T) defaultValue;
        }
        if (clazz == int.class || clazz == Integer.class) {
            return (T) clazz.cast(Integer.valueOf(val));
        }
        if (clazz == long.class || clazz == Long.class) {
            return (T) clazz.cast(Long.valueOf(val));
        }
        if (clazz == boolean.class || clazz == Boolean.class) {
            final Boolean boolValue;
            if ("1".equals(val) || "0".equals(val)) {
                boolValue = "1".equals(val);
            } else {
                boolValue = Boolean.valueOf(val);
            }
            return (T) clazz.cast(boolValue);
        }
        return (T) clazz.cast(val);
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
