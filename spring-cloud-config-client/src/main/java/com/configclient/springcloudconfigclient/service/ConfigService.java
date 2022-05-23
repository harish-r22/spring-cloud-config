package com.configclient.springcloudconfigclient.service;

import com.configclient.springcloudconfigclient.controller.ConfigProps;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.lifetech.magellan.core.action.ConfigurationAction;
import com.lifetech.magellan.core.config.MagellanConfigEntity;
import com.lifetech.magellan.core.config.VersionDetail;
import com.lifetech.magellan.core.config.YamlMagellanConfigAdapter;
import com.lifetech.magellan.core.config.yaml.beans.ConfigurationItem;
import com.lifetech.magellan.core.config.yaml.beans.MagellanConfigOverridesEntity;
import com.lifetech.magellan.core.config.yaml.beans.PredefinedValue;
import com.lifetech.magellan.core.config.yaml.beans.YamlBasedMagellanConfig;
import com.lifetech.magellan.core.config.yaml.reader.MyYamlReader;
import com.lifetech.magellan.core.config.MagellanConfig;
import com.lifetech.magellan.core.hbn.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

import static com.google.common.base.Strings.isNullOrEmpty;

@Service
public class ConfigService {

    @Autowired
    ConfigProps configProps;

    public YamlBasedMagellanConfig getConfigItem() throws IOException {
        MyYamlReader reader = new MyYamlReader(MagellanConfig.getInstance());
        YamlBasedMagellanConfig yamlBasedMagellanConfig = reader.read();
        return yamlBasedMagellanConfig;
    }

    public void putConfigItem(String key, String value) throws IOException {

        Preconditions.checkArgument(!isNullOrEmpty(key), "Key cannot be null or empty");
        Preconditions.checkArgument(!isNullOrEmpty(value), "Value cannot be null or empty");
        VersionDetail versionDetail = new VersionDetail();
        String database = "orcl_tsi_gart";
        SessionFactory factory = HibernateUtil.getSessionFactory(database);
        Session session = factory.getCurrentSession();
        Optional<String> optionalItem = configProps.retreiveConfigurationItemByKey(key);
        if (optionalItem.isPresent()) {
            String item = optionalItem.get();
            MagellanConfigEntity entity = new MagellanConfigEntity();
            MagellanConfigEntity.CompoundKey compKey= new MagellanConfigEntity.CompoundKey("ALL",key);
            entity.setCompKey(compKey);
            entity.setValue(value);
            Transaction transaction = session.beginTransaction();
            try {
                session.saveOrUpdate(entity);
                transaction.commit();
                /*configProps.removeKeyValuePair(key);
                // add to magellan configuration
                configProps.setKeyValuePair(key, value);*/
            } catch (HibernateException e) {
                transaction.rollback();
                throw new HibernateException("Hibernate exception, we had to rollback " + e.getMessage());
            }
        }
    }
}