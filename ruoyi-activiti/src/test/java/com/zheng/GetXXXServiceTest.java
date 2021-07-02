package com.zheng;

import org.activiti.engine.*;
import org.junit.Before;
import org.junit.Test;

/**
 * @author: ZhengTianLiang
 * @date: 2021/6/29  16:05
 * @desc: 测试xxx的service，根据流程引擎，创建 XXXService
 */

public class GetXXXServiceTest {

    private ProcessEngine processEngine;

    /**
     * @author: ZhengTianLiang
     * @date: 2021/6/29  16:07
     * @desc: 初始化一个流程引擎对象
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
            System.out.println("正常创建了工作流引擎对象，可以通过这个引擎创建xxxService来操作表");
        }

    }

    /**
     * @author: ZhengTianLiang
     * @date: 2021/6/29  16:10
     * @desc: 测试一个service
     */
    @Test
    public void testGetXXXService(){
        // repositoryService    部署文件和支持数据
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 当一个流程实例被启动后，就生成一个流程对象实例，还提供了流程部署、流程定义、流程实例的存取服务
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 运行时任务查询、领取、完成、删除以及变量设置的工作
        TaskService taskService = processEngine.getTaskService();
        // 设置了用户及组管理的功能，必须拿到对应的用户和组的信息才能获取相应的task
        IdentityService identityService = processEngine.getIdentityService();
        // 流程引擎的管理和维护功能，不在工作流驱动中使用，主要用于activiti的日常维护
        ManagementService managementService = processEngine.getManagementService();
        // 获取正在运行或者已经完成的流程实例的信息
        HistoryService historyService = processEngine.getHistoryService();

        if (repositoryService == null){
            System.out.println("劳资创建了个寂寞");
        }else {
            System.out.println("正常的创建了service对象");
        }

    }

}
