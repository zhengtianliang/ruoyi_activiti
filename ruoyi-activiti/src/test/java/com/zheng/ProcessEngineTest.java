package com.zheng;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;

/**
 * @author: ZhengTianLiang
 * @date: 2021/6/29  15:09
 * @desc: 流程引擎的测试类，创建流程引擎
 */


public class ProcessEngineTest {

    /**
     * @author: ZhengTianLiang
     * @date: 2021/6/29  15:31
     * @desc: 测试流程引擎
     */
    @Test
    public void testProcessEngine(){
        ProcessEngineConfiguration processEngineConfiguration =
                ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        processEngineConfiguration.setJdbcDriver("com.mysql.cj.jdbc.Driver");
        processEngineConfiguration.setJdbcUrl("jdbc:mysql://localhost:3306/ry_demo2?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true");
        processEngineConfiguration.setJdbcUsername("root");
        processEngineConfiguration.setJdbcPassword("123456");

        /**
         * DB_SCHEMA_UPDATE_TRUE = "true";      // 如果表不存在，则自动创建表(适合第一次使用的场景)
         * DB_SCHEMA_UPDATE_CREATE_DROP = "create-drop";    // 先删除表，在创建表
         * DB_SCHEMA_UPDATE_FALSE = "false";    // 不能自动创建表，需要表先存在
         */
        processEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        if (processEngine == null){
            System.out.println("劳资创建了个null，淦");
        }else {
            System.out.println("正常创建了工作流引擎对象，可以通过这个引擎创建xxxService来操作表");
        }

    }
}
