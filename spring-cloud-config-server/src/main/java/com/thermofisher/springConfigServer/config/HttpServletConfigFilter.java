package com.thermofisher.springConfigServer.config;

import atg.taglib.json.util.JSONArray;
import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.api.client.json.JsonObjectParser;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lifetech.magellan.core.config.MagellanConfig;
import com.lifetech.magellan.core.config.yaml.beans.YamlBasedMagellanConfig;
import com.thermofisher.springConfigServer.config.*;
import com.thermofisher.springConfigServer.service.ConfigService;
import org.apache.catalina.connector.RequestFacade;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.mortbay.util.ajax.JSONObjectConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.util.*;


@Component
public class HttpServletConfigFilter implements Filter {

    @Autowired
    ConfigService configService;

    private MagellanConfig config;

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter
            (ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        Map<String, String> map = new HashMap<String, String>();
        ServletResponseWrapperCopier capturingResponseWrapper = new ServletResponseWrapperCopier(
                (HttpServletResponse) response);

        chain.doFilter(request, capturingResponseWrapper);
        try {
            String respopn = capturingResponseWrapper.getCaptureAsString();
            JSONObject json = new JSONObject(respopn);
            JSONArray jsonArray=json.getJSONArray("propertySources");
            if(((RequestFacade) request).getRequestURI().equals("/application/prod")){
                config=config.populateConfigValuesFromDataBase(config.getVersionDetail(),config);
            }else{
                config= MagellanConfig.getInstance();
            }
            String jsonInString = new Gson().toJson(config);
            for(int i=0;i<jsonArray.length();i++){
                if(i==0) {
                    jsonArray.getJSONObject(i).getJSONObject("source").put("magellanConfig", jsonInString);
                }else{
                    jsonArray.remove(i);
                }
            }
            JsonMapper jsonMap = new JsonMapper();
            jsonMap.setJson(json.toString());
            String str = jsonMap.getJson();
            response.getOutputStream().write(str.getBytes());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void init(FilterConfig filterconfig) throws ServletException {

    }

    private byte[] restResponseBytes(Object response) throws IOException {
        String serialized = new ObjectMapper().writeValueAsString(response);
        return serialized.getBytes();
    }

    public JSONObject toJson(Map<String, String> map) {
        JSONObject jsonObject = new JSONObject();

        for (String key : map.keySet()) {
            try {
                Object obj = map.get(key);
                if (obj instanceof Map) {
                    jsonObject.put(key, toJson((Map) obj));
                } else if (obj instanceof List) {
                    jsonObject.put(key, toJson((List) obj));
                } else {
                    jsonObject.put(key, map.get(key));
                }
            } catch (JSONException jsone) {
                jsone.printStackTrace();
            }
        }

        return jsonObject;
    }

    public JSONArray toJson(List<Object> list) {
        JSONArray jsonArray = new JSONArray();

        for (Object obj : list) {
            if (obj instanceof Map) {
                jsonArray.put(toJson((Map) obj));
            } else if (obj instanceof List) {
                jsonArray.put(toJson((List) obj));
            } else {
                jsonArray.put(obj);
            }
        }

        return jsonArray;
    }

    private JSONArray toJsonArray(Map<String, String> map) {
        JSONArray jsonArray = new JSONArray();
            for (String key : map.keySet()) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    Object obj = map.get(key);
                    if (obj instanceof Map) {
                        jsonObject.put(key, toJson((Map) obj));
                    } else if (obj instanceof List) {
                        jsonObject.put(key, toJson((List) obj));
                    } else {
                        jsonObject.put(key, map.get(key));
                    }
                    jsonArray.put(jsonObject);
                } catch (JSONException jsone) {
                    jsone.printStackTrace();
                }
        }
        return jsonArray;
    }
}
