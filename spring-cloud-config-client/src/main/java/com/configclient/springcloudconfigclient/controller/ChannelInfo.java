package com.configclient.springcloudconfigclient.controller;


import com.lifetech.magellan.core.config.MagellanConfig;

import java.util.Map;

public class ChannelInfo {
    private String source;

    private String name;

    private String url;

    public MagellanConfig getConfig() {
        return config;
    }

    public void setConfig(MagellanConfig config) {
        this.config = config;
    }

    private MagellanConfig config;

    public ChannelInfo(String source, String name, String url,MagellanConfig config) {
        this.source = source;
        this.name = name;
        this.url = url;
        this.config=config;
    }

    public ChannelInfo() {
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

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



}
