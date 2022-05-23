package com.thermofisher.springConfigServer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifetech.magellan.core.config.yaml.beans.YamlBasedMagellanConfig;
import com.thermofisher.springConfigServer.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

@RestController
public class ConfigController {

    @Autowired
    ConfigService service;

    @RequestMapping(value = "/fetchConfig", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,String>  getConfig() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        YamlBasedMagellanConfig yamlBasedMagellanConfig=null;
        String jsonStr="";
        try {
            yamlBasedMagellanConfig = service.getConfigItem();
            jsonStr = objectMapper.writeValueAsString(yamlBasedMagellanConfig.getCoreConfigurationAsMap());
        }catch (Exception e){
            throw e;
        }
        return yamlBasedMagellanConfig.getFullConfigurationAsMap();
    }


}
