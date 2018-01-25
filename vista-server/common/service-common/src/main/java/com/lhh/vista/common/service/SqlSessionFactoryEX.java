package com.lhh.vista.common.service;

import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.dom4j.io.SAXReader;
import org.dom4j.*;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.springframework.util.Assert.notNull;
import static org.springframework.util.ObjectUtils.isEmpty;
import static org.springframework.util.StringUtils.hasLength;
import static org.springframework.util.StringUtils.tokenizeToStringArray;

public class SqlSessionFactoryEX extends SqlSessionFactoryBean {
    private static final Log logger = LogFactory.getLog(SqlSessionFactoryBean.class);
    private SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
    private Interceptor[] plugins;
    private Class<?>[] typeAliases;
    private String typeAliasesPackage;
    private TypeHandler<?>[] typeHandlers;
    private String typeHandlersPackage;
    private TransactionFactory transactionFactory;
    private Properties configurationProperties;
    private Resource[] configLocations;
    private DataSource dataSource;
    private String environment = SqlSessionFactoryBean.class.getSimpleName();
    private DatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
    private Resource[] mapperLocations;
    private SqlSessionFactory sqlSessionFactory;

    /**
     * ======================================methods============================
     * =====
     */
    /* 修改该方法 */
    public void setConfigLocation(Resource configLocation) {
        this.configLocations = configLocation != null ? new Resource[]{configLocation} : null;
    }

    /* 增加该方法 */
    public void setConfigLocations(Resource[] configLocations) {
        this.configLocations = configLocations;
    }

    /**
     * 生成一个mybatis配置文件,存储合并后的文件
     */
    public Document SQLConfigMap() {
        Document doc = DocumentHelper.createDocument();
        doc.setXMLEncoding("UTF-8");
        DocumentFactory documentFactory = new DocumentFactory();
        DocumentType docType = documentFactory.createDocType("configuration", "-//mybatis.org//DTD Config 3.0//EN", "http://mybatis.org/dtd/mybatis-3-config.dtd");
        doc.setDocType(docType);
        doc.addElement("configuration");
        return doc;
    }

    /**
     * 将每个配置文件的typeAliases和mapper合并到新的配置文件
     *
     * @param configXML
     * @param configXML
     * @param root
     * @param saxReader
     */
    public void readXML(Resource configXML, final Element root, SAXReader saxReader) {
        /* typeAliases合并 */
        saxReader.addHandler("/configuration/typeAliases/typeAlias", new ElementHandler() {
            public void onEnd(ElementPath path) {
                if (root.element("typeAliases") == null) {
                    root.addElement("typeAliases");
                }
                Element row = path.getCurrent();
                root.element("typeAliases").add((Element) row.clone());
                row.detach();
            }

            public void onStart(ElementPath arg0) {
            }
        });
        /* mapper合并 */
        saxReader.addHandler("/configuration/mappers/mapper", new ElementHandler() {

            public void onEnd(ElementPath path) {
                if (root.element("mappers") == null) {
                    root.addElement("mappers");
                }
                Element row = path.getCurrent();
                root.element("mappers").add((Element) row.clone());
                row.detach();
            }

            public void onStart(ElementPath arg0) {

            }
        });
        try {
            saxReader.read(configXML.getInputStream());
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readPlugin(Resource configXML, final Element root, SAXReader saxReader, final List<String> existPlugin) {
        /* plugins合并 */
        saxReader.addHandler("/configuration/plugins/plugin", new ElementHandler() {
            public void onEnd(ElementPath path) {
                if (root.element("plugins") == null) {
                    root.addElement("plugins");
                }
                Element row = path.getCurrent();
                //先判断插件添加了没有
                String pluginInterceptor = row.attribute("interceptor").getValue();
                if (!existPlugin.contains(pluginInterceptor)) {
                    existPlugin.add(pluginInterceptor);
                    root.element("plugins").add((Element) row.clone());
                    row.detach();
                }
            }

            public void onStart(ElementPath arg0) {
            }
        });
        try {
            saxReader.read(configXML.getInputStream());
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return SqlSessionFactory
     * @throws IOException
     */
    protected SqlSessionFactory buildSqlSessionFactory() throws IOException {
        Configuration configuration = null;
        XMLConfigBuilder xmlConfigBuilder = null;
        Document document = this.SQLConfigMap();
        Element root = document.getRootElement();
        //先解析typeAliases和mapper
        SAXReader saxReader1 = new SAXReader();
        //禁止检查dtd,会出现超时错误
        saxReader1.setEntityResolver(new EntityResolver() {
            public InputSource resolveEntity(String publicId, String systemId) {
                return new InputSource(new StringReader(""));
            }
        });
        for (Resource configLocation : configLocations) {
            readXML(configLocation, root, saxReader1);
        }
        SAXReader saxReader2 = new SAXReader();
        //禁止检查dtd,会出现超时错误
        saxReader2.setEntityResolver(new EntityResolver() {
            public InputSource resolveEntity(String publicId, String systemId) {
                return new InputSource(new StringReader(""));
            }
        });
        //这里必须把插件放到后面...不知道为什么放到前面会报错
        List<String> existPlugin = new ArrayList<>();
        for (Resource configLocation : configLocations) {
            readPlugin(configLocation, root, saxReader2, existPlugin);
        }

        if (this.configLocations != null) {
            logger.debug(document.asXML());
            InputStream inputSteam = new ByteArrayInputStream(document.asXML().getBytes());
            xmlConfigBuilder = new XMLConfigBuilder(inputSteam, null, this.configurationProperties);
            configuration = xmlConfigBuilder.getConfiguration();
            if (inputSteam != null) {
                inputSteam.close();
                inputSteam = null;
            }
            document = null;
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Property 'configLocation' not specified,using default MyBatis Configuration");
            }
            configuration = new Configuration();
            configuration.setVariables(this.configurationProperties);
        }


        if (hasLength(this.typeAliasesPackage)) {
            String[] typeAliasPackageArray = tokenizeToStringArray(this.typeAliasesPackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
            for (String packageToScan : typeAliasPackageArray) {
                configuration.getTypeAliasRegistry().registerAliases(packageToScan);
                if (logger.isDebugEnabled()) {
                    logger.debug("Scanned package: '" + packageToScan + "' for aliases");
                }
            }
        }
        if (!isEmpty(this.typeAliases)) {
            for (Class<?> typeAlias : this.typeAliases) {
                configuration.getTypeAliasRegistry().registerAlias(typeAlias);
                if (logger.isDebugEnabled()) {
                    logger.debug("Registered type alias: '" + typeAlias + "'");
                }
            }
        }
        if (!isEmpty(this.plugins)) {
            for (Interceptor plugin : this.plugins) {
                configuration.addInterceptor(plugin);
                if (logger.isDebugEnabled()) {
                    logger.debug("Registered plugin: '" + plugin + "'");
                }
            }
        }
        if (hasLength(this.typeHandlersPackage)) {
            String[] typeHandlersPackageArray = tokenizeToStringArray(this.typeHandlersPackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
            for (String packageToScan : typeHandlersPackageArray) {
                configuration.getTypeHandlerRegistry().register(packageToScan);
                if (logger.isDebugEnabled()) {
                    logger.debug("Scanned package: '" + packageToScan + "' for type handlers");
                }
            }
        }
        if (!isEmpty(this.typeHandlers)) {
            for (TypeHandler<?> typeHandler : this.typeHandlers) {
                configuration.getTypeHandlerRegistry().register(typeHandler);
                if (logger.isDebugEnabled()) {
                    logger.debug("Registered type handler: '" + typeHandler + "'");
                }
            }
        }
        if (xmlConfigBuilder != null) {
            try {
                xmlConfigBuilder.parse();

                if (logger.isDebugEnabled()) {
                    logger.debug("Parsed configuration file: '" + this.configLocations + "'");
                }
            } catch (Exception ex) {
                throw new NestedIOException("Failed to parse config resource: " + this.configLocations, ex);
            } finally {
                ErrorContext.instance().reset();
            }
        }
        if (this.transactionFactory == null) {
            this.transactionFactory = new SpringManagedTransactionFactory();
        }

        Environment environment = new Environment(this.environment, this.transactionFactory, this.dataSource);
        configuration.setEnvironment(environment);

        if (this.databaseIdProvider != null) {
            try {
                configuration.setDatabaseId(this.databaseIdProvider.getDatabaseId(this.dataSource));
            } catch (SQLException e) {
                throw new NestedIOException("Failed getting a databaseId", e);
            }
        }

        if (!isEmpty(this.mapperLocations)) {
            for (Resource mapperLocation : this.mapperLocations) {
                if (mapperLocation == null) {
                    continue;
                }

                try {
                    XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(mapperLocation.getInputStream(), configuration, mapperLocation.toString(), configuration.getSqlFragments());
                    xmlMapperBuilder.parse();
                } catch (Exception e) {
                    throw new NestedIOException("Failed to parse mapping resource: '" + mapperLocation + "'", e);
                } finally {
                    ErrorContext.instance().reset();
                }

                if (logger.isDebugEnabled()) {
                    logger.debug("Parsed mapper file: '" + mapperLocation + "'");
                }
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Property 'mapperLocations' was not specified or no matching resources found");
            }
        }
        return this.sqlSessionFactoryBuilder.build(configuration);
    }

    public void afterPropertiesSet() throws Exception {
        notNull(dataSource, "Property 'dataSource' is required");
        notNull(sqlSessionFactoryBuilder, "Property 'sqlSessionFactoryBuilder' is required");
        this.sqlSessionFactory = buildSqlSessionFactory();
    }

    public SqlSessionFactory getObject() throws Exception {
        if (this.sqlSessionFactory == null) {
            afterPropertiesSet();
        }

        return this.sqlSessionFactory;
    }

    public SqlSessionFactoryBuilder getSqlSessionFactoryBuilder() {
        return sqlSessionFactoryBuilder;
    }

    public void setSqlSessionFactoryBuilder(SqlSessionFactoryBuilder sqlSessionFactoryBuilder) {
        this.sqlSessionFactoryBuilder = sqlSessionFactoryBuilder;
    }

    public void setPlugins(Interceptor[] plugins) {
        this.plugins = plugins;
    }

    public void setTypeAliases(Class<?>[] typeAliases) {
        this.typeAliases = typeAliases;
    }

    public void setTypeAliasesPackage(String typeAliasesPackage) {
        this.typeAliasesPackage = typeAliasesPackage;
    }

    public void setTypeHandlers(TypeHandler<?>[] typeHandlers) {
        this.typeHandlers = typeHandlers;
    }

    public void setTypeHandlersPackage(String typeHandlersPackage) {
        this.typeHandlersPackage = typeHandlersPackage;
    }

    public void setTransactionFactory(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }

    public void setConfigurationProperties(Properties configurationProperties) {
        this.configurationProperties = configurationProperties;
    }

    public void setMapperLocations(Resource[] mapperLocations) {
        this.mapperLocations = mapperLocations;
    }

    public void setDataSource(DataSource dataSource) {
        if (dataSource instanceof TransactionAwareDataSourceProxy) {
            this.dataSource = ((TransactionAwareDataSourceProxy) dataSource).getTargetDataSource();
        } else {
            this.dataSource = dataSource;
        }
    }
}
