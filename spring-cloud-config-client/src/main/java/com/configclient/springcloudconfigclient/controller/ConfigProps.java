package com.configclient.springcloudconfigclient.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifetech.magellan.core.config.MagellanConfig;
import com.lifetech.magellan.core.config.VersionDetail;
import com.lifetech.magellan.core.config.yaml.beans.ConfigurationItem;
import com.lifetech.magellan.core.slf4j.MagellanSlf4jLoggerFactory;
import javafx.util.Pair;
import jdk.internal.dynalink.beans.StaticClass;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListener;
import org.springframework.expression.spel.ast.TypeReference;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@ConfigurationProperties(prefix = "", ignoreUnknownFields = true ,ignoreInvalidFields = true)
@RefreshScope
public class ConfigProps extends MagellanConfig implements InitializingBean {

    protected static final Logger logger = MagellanSlf4jLoggerFactory.getLogger(MagellanConfig.class);

    private static MagellanConfig magellan;

    private String name;

    private String url;

    private String magellanConfig;

    private String source;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMagellanConfig() {
        return magellanConfig;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setMagellanConfig(String magellanConfig) {
        this.magellanConfig = magellanConfig;
    }

    public static MagellanConfig getMagellan() {
        return magellan;
    }

    public static void setMagellan(MagellanConfig magellan) {
        ConfigProps.magellan = magellan;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(this.getMagellanConfig()!=null){
             magellan = JSON.parseObject(this.getMagellanConfig(),MagellanConfig.class);
        }
        constructAppliactionConfig();
    }

    /*@EventListener
    public void onRefreshScopeRefreshed(final RefreshScopeRefreshedEvent event) {
      if(this.magellanConfig!=null){
       }
    }*/

    public MagellanConfig getInstance(String str) {
        // We will only try to load a magellan config one time
        if (magellan == null) {
            Throwable ex = null;
            try {
                magellan = new MagellanConfig(new VersionDetail());
            } catch (Throwable e) {
                logger.error("Error creating Magellan config:", e);
                magellan = null;
                ex = e;
            }

            if (magellan == null) {
                magellan = new MagellanConfig();
            }
        }
        return magellan;
    }

    public void constructAppliactionConfig() {
        try {
            for (Field field : this.getClass().getDeclaredFields()) {
                if (field.getType().equals(String.class) && !field.getName().equals("magellanConfig") ) {
                    field.setAccessible(true); // You might want to set modifier to public first.
                    Object value = field.get(this);
                    if (value != null) {
                        magellan.getConfigMap().put(field.getName(), value.toString());
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Optional<String> retreiveConfigurationItemByKey(String key) {
        return Optional.ofNullable(magellan.get(key));
    }

    public void setKeyValuePair(String key,String value) {
        if(magellan.getBaseConfigMap().containsKey(key)){
            magellan.getBaseConfigMap().put(key, value);
        }
        magellan.getConfigMap().put(key, value);

    }

    public void removeKeyValuePair(String item) {
        magellan.getBaseConfigMap().remove(item);
        magellan.getConfigMap().remove(item);
    }
}
