package com.thermofisher.springConfigServer.service;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.lifetech.magellan.core.action.ConfigurationAction;
import com.lifetech.magellan.core.config.VersionDetail;
import com.lifetech.magellan.core.config.YamlMagellanConfigAdapter;
import com.lifetech.magellan.core.config.yaml.beans.ConfigurationItem;
import com.lifetech.magellan.core.config.yaml.beans.PredefinedValue;
import com.lifetech.magellan.core.config.yaml.beans.YamlBasedMagellanConfig;
import com.lifetech.magellan.core.config.yaml.reader.MyYamlReader;
import com.lifetech.magellan.core.config.MagellanConfig;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class ConfigService {

    private VersionDetail vnDetail;

    public YamlBasedMagellanConfig getConfigItem() throws IOException {
        MyYamlReader reader= new MyYamlReader(MagellanConfig.getInstance());
        YamlBasedMagellanConfig yamlBasedMagellanConfig=reader.read();
       return yamlBasedMagellanConfig;
    }

    public MagellanConfig getConfigFromDataBase(){
      MagellanConfig config= MagellanConfig.getInstance();
      return config.populateConfigValuesFromDataBase(config.getVersionDetail(),config);
    }

}
