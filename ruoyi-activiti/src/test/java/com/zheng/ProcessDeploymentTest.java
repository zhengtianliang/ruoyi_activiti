package com.zheng;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.junit.Before;
import org.junit.Test;

/**
 * @author: ZhengTianLiang
 * @date: 2021/6/29  17:27
 * @desc: 流程部署定义  测试类
 */

public class ProcessDeploymentTest {

    // activiti流程引擎对象
    private ProcessEngine processEngine;
    // activiti中操作用户的service
    private IdentityService identityService;
    // 流程定义对象
    RepositoryService repositoryService;

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

        // 初始化流程引擎对象
        processEngine = processEngineConfiguration.buildProcessEngine();
        // 初始化操作用户、用户组的service
        identityService = processEngine.getIdentityService();
        // 初始化一个流程定义对象
        repositoryService = processEngine.getRepositoryService();

        if (processEngine == null){
            System.out.println("劳资创建了个null，淦");
        }else {
            System.out.println("正常创建了工作流引擎对象，可以通过这个引擎创建xxxService来操作表");
        }

    }

    /**
     * @author: ZhengTianLiang
     * @date: 2021/6/29  17:30
     * @desc: 初始化用户信息
     */
    @Test
    public void initUser(){
        // 初始化一个对流程用户，id是axianlu，名称是一只闲鹿
        User axianlu = identityService.newUser("axianlu");
        axianlu.setFirstName("一只闲鹿");
        identityService.saveUser(axianlu);

        User rensm = identityService.newUser("rensm");
        rensm.setFirstName("人事喵");
        identityService.saveUser(rensm);
        System.out.println(identityService.createUserQuery().count());
        if ("2".equals(String.valueOf(identityService.createUserQuery().count()))){
            System.out.println("s 2个");
        }
        if (2 == identityService.createUserQuery().count()){
            System.out.println("s 2个1");
        }

    }

    /**
     * @author: ZhengTianLiang
     * @date: 2021/6/29  17:45
     * @desc: 初始化用户组(角色)信息
     */
    @Test
    public void initGroup(){
        // 初始化一个部门领导的用户组
        Group deptLeader = identityService.newGroup("deptLeader");
        deptLeader.setName("deptLeader");
        deptLeader.setType("assignment");

        // 初始化一个hr的用户组
        Group hr = identityService.newGroup("hr");
        hr.setName("hr");
        hr.setType("assignment");

        identityService.saveGroup(hr);
        identityService.saveGroup(deptLeader);

        System.out.println(identityService.createGroupQuery().count());

    }


    /**
     * @author: ZhengTianLiang
     * @date: 2021/6/29  17:45
     * @desc: 初始化用户和角色的关系表
     */
    @Test
    public void initMembership(){
        // 将id是axianlu的用户，分配给  deptLeader  的岗位
        identityService.createMembership("axianlu","deptLeader");
        identityService.createMembership("rensm","hr");
    }

    /**
     * @author: ZhengTianLiang
     * @date: 2021/6/29  17:56
     * @desc: 部署流程定义
     */
    @Test
    public void deployTest(){
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("zheng_test.bpmn")
                .deploy();
        if (deploy == null){
            System.out.println("我部署了个寂寞");
        }else {
            System.out.println("正常部署流程定义对象");
        }
    }


}
