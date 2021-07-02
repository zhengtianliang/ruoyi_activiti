package com.zheng;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: ZhengTianLiang
 * @date: 2021/6/30  15:15
 * @desc: 发起流程申请的测试类
 */

public class ProcessTest {

    // activiti流程引擎对象
    private ProcessEngine processEngine;
    // activiti中操作用户的service
    private IdentityService identityService;
    // 流程定义对象
    private RepositoryService repositoryService;
    // 流程实例化
    private RuntimeService runtimeService;
    // 任务实例
    private TaskService taskService;
    // 历史实例
    private HistoryService historyService;

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
        // 实例化流程实例
        runtimeService = processEngine.getRuntimeService();
        // 获取任务service
        taskService = processEngine.getTaskService();
        // 获取历史接口
        historyService = processEngine.getHistoryService();

        if (processEngine == null){
            System.out.println("劳资创建了个null，淦");
        }else {
            System.out.println("正常创建了工作流引擎对象，可以通过这个引擎创建xxxService来操作表");
        }

    }

    /**
     * @author: ZhengTianLiang
     * @date: 2021/6/30  16:28
     * @desc: 发起流程申请
     */
    @Test
    public void submitApplyTest(){
        // 发起流程申请需要指定一个发起人
        String applyUserId = "niubi";
        identityService.setAuthenticatedUserId(applyUserId);
        runtimeService.startProcessInstanceByKey("zheng_test");
    }

    /**
     * @author: ZhengTianLiang
     * @date: 2021/6/30  17:23
     * @desc: 获取我的待办
     */
    @Test
    public void queryTaskTodo(){
        // 根据当前人的id去查任务
        List<Task> list1 = taskService.createTaskQuery()
                .processDefinitionKey("zheng_test")
                .taskAssignee("axianlu") // 用户id
                .active()
                .list();
        // 根据当前人未签收的任务
        List<Task> list2 = taskService.createTaskQuery()
                .processDefinitionKey("zheng_test")
                .taskCandidateUser("axianlu")
                .active()
                .list();

        List<Task> list3 = new ArrayList<>();
        list3.addAll(list1);
        list3.addAll(list2);

        long count = taskService.createTaskQuery().count(); // 1
    }

    /**
     * @author: ZhengTianLiang
     * @date: 2021/6/30  18:03
     * @desc: 完成待办  (act_ru_task表变了，下个审批人变成了hr；act_hi_comment表变了，多了一条审批意见的记录)
     */
    @Test
    public void completeTask(){
        // 获取当前人未签收的任务
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey("zheng_test")
                .taskCandidateUser("axianlu")
                .active()
                .list();

        // 拿到第一个任务
        Task task = list.get(0);
        // 获取流程实例对象
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey("zheng_test")
                .singleResult();

        // 指定审批人是谁
        identityService.setAuthenticatedUserId("axianlu");
        // 审批通过    任务id、流程实例id、审批意见
        taskService.addComment(task.getId(),processInstance.getId(),"同意该申请");
        Map<String,Object> map = new HashMap<>();
        // true 审批通过，false，审批失败(这个deptLeaderApproved是你的配置节点的${deptLeaderApproved})
        map.put("deptLeaderApproved",true);
        // 先签收任务，只有签收完了以后，act_hi_taskinst 表的 assignee 字段才不为null
        taskService.claim(task.getId(),"axianlu");
        // 调用tasksesrvice的完成方法
        taskService.complete(task.getId(),map);
    }

    /**
     * @author: ZhengTianLiang
     * @date: 2021/7/1  9:55
     * @desc: 获取  我的已办(活动实例历史表act_hi_actinst)
     */
    @Test
    public void testDoneTest(){
        // 创建一个历史任务实例查询
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .processDefinitionKey("zheng_test")
                .taskAssignee("axianlu")
                .finished()
                .list();
        for (HistoricTaskInstance instance : list){
            String name = instance.getName();
            String assignee = instance.getAssignee();
            System.out.println("历史任务名称："+name+"，办理人是："+assignee);
        }
    }

    /**
     * @author: ZhengTianLiang
     * @date: 2021/7/1  9:55
     * @desc: 获取  审批的历史批注
     */
    @Test
    public void queryHistoryComment(){
        // 先获取流程实例对象
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey("zheng_test")
                .singleResult();
        // 历史备注就是历史活动任务所写的备注，所以需要先获取历史活动的集合
        List<HistoricActivityInstance> userTask = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstance.getId())
                .activityType("userTask")   // 用户任务，
                .finished() // 已经完成的
                .list();

        for (HistoricActivityInstance instance : userTask){
            // 通过任务id去查询审批意见，
            List<Comment> comment = taskService.getTaskComments(instance.getTaskId(), "comment");
            System.out.println("历史批注：Assignee -> "
                    +instance.getAssignee()+",Comment->"+comment.get(0).getFullMessage());
        }
    }

}
