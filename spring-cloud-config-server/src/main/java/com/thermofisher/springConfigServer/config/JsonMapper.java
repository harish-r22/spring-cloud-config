package com.thermofisher.springConfigServer.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.StringWriter;

public class JsonMapper extends ObjectMapper {

    private String json;

    @JsonRawValue
    public String getJson() {
        return json;
    }

    public void setJson(final String json) {
        this.json = json;
    }

}
