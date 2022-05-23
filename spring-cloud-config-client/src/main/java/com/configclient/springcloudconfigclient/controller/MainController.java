package com.configclient.springcloudconfigclient.controller;

import atg.taglib.json.util.JSONObject;
import com.configclient.springcloudconfigclient.service.ConfigService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import net.sf.json.JSONSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;


@RestController
public class MainController {

    @Autowired
    private ConfigService service;

    @Value("${channel.source: No Channel}")
    private String source;

    @Autowired
    private ConfigProps configProps;


    @GetMapping("/info")
    @ResponseBody
    public String getChannleInfo() {
        JSONObject mJSONObject=new JSONObject();
        String str="";
        try {
            ChannelInfo info=new ChannelInfo(configProps.getSource(), configProps.getName(), configProps.getUrl(), ConfigProps.getMagellan());
            String jsonInString = new Gson().toJson(info);
            mJSONObject = new JSONObject(jsonInString);
            JsonMapper jsonMap = new JsonMapper();
            jsonMap.setJson(mJSONObject.toString());
           str = jsonMap.getJson();
        }catch (Exception e){
            e.printStackTrace();
          }
        return str;
    }

    @RequestMapping(value="/postConfig",method = RequestMethod.POST)
    @ResponseBody
    public String postConfig(@RequestBody String json) {
        try {
            net.sf.json.JSONObject jsonObject = (net.sf.json.JSONObject) JSONSerializer.toJSON(json);
            String key = jsonObject.getString("key");
            String value = jsonObject.getString("value");
            service.putConfigItem(key, value);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "Config Updated successfully";
    }



}
