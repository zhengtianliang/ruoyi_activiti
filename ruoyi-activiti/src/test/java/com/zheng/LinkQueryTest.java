package com.zheng;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.junit.Before;
import org.junit.Test;

/**
 * @author: ZhengTianLiang
 * @date: 2021/6/29  17:17
 * @desc: 测试activiti的链式调用
 */


public class LinkQueryTest {

    private ProcessEngine processEngine;

    /**
     * @author: ZhengTianLiang
     * @date: 2021/6/29  15:31
     * @desc: 测试流程引擎
     */
    @Before
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
        processEngine = processEngineConfiguration.buildProcessEngine();
        if (processEngine == null){
            System.out.println("劳资创建了个null，淦");
        }else {
            System.out.println("正常创建了工作流引擎对象，可以通过这个引擎创建xxxService来操作表");    // 走进来了
        }

    }

    /**
     * @author: ZhengTianLiang
     * @date: 2021/6/29  17:17
     * @desc: 测试activiti的链式调用   使用activiti的链式风格调用时，始终返回一个查询对象
     */
    @Test
    public void testLinkQuery(){
        ProcessDefinitionQuery processDefinitionQuery =
                processEngine.getRepositoryService().createProcessDefinitionQuery();
        ProcessDefinitionQuery processDefinitionQuery2 = processDefinitionQuery.active();

        if (processDefinitionQuery.equals(processDefinitionQuery2)){
            System.out.println("一样一样滴");        // 走进了她分支
        }else {
            System.out.println("这俩不一样");
        }

    }

}
