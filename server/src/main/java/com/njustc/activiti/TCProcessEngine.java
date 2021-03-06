package com.njustc.activiti;

import com.alibaba.fastjson.JSONObject;
import com.njustc.framework.core.web.Request;
import com.njustc.framework.mybatis.UserMapper;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TCProcessEngine {

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    HistoryService historyService;

    @Autowired
    FormService formService;

    @Autowired
    private BaseOperation baseOperation;

  /*  enum TaskOperation {
        Submit, Review, Confirm, Write, Implement, Approve, Send, Done, Fil, Satisfact
    }*/
    enum TaskOperationnoGate {
        Submit, Write, Implement, Send, Done, Fil, Satisfact
    }
    enum TaskOperationwithGate{
        Review, Confirm, Approve
    }
    @Autowired
    private UserMapper userMapper;

    /**
     * 获取工作人员列表
     * @return 以字符串形式返回工作人员ID
     */
    public List<String> getWorkersList() {
        List<String> userids = userMapper.getUserIdsByRoleId("1");
        return userids;
    }

    /**
     * TODO 将用户组传入流程实例 新建一个新的委托实例
     * 新建委托实例
     * @param consignId 委托ID
     * @param clientId 客户ID
     * @return 以String形式返回流程实例的ID
     * @throws Exception 新建委托实例失败
     */
    public String createConsignProcess(String consignId, String clientId) throws Exception {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("ConsignID", consignId);
        variables.put("ClientID", clientId);
        List<String> userids = this.getWorkersList();
        if (!userids.isEmpty()) {
            for (String userid : userids) {
                variables.put("WorkerIDs", userid);
            }
        } else
            throw new Exception("EMPTY");
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("Consign", variables);
        return pi.getProcessInstanceId();
    }

    /**
     * 新建合同实例
     * 
     * @param contractId 委托ID
     * @param clientId 客户ID
     * @return 以String形式返回流程实例的ID
     * @throws Exception 新建合同实例失败
     */
    public String createContractProcess(String contractId, String clientId) throws Exception {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("ContractID", contractId);
        variables.put("ClientID", clientId);
        List<String> userids = this.getWorkersList();
        if (!userids.isEmpty()) {
            for (String userid : userids) {
                variables.put("WorkerIDs", userid);
            }
        } else
            throw new Exception("EMPTY");
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("contract", variables);
        return pi.getProcessInstanceId();
    }


    /**
     * 新建测试方案实例
     * @return 以String形式返回流程实例的ID
     * @throws Exception 新建测试方案实例失败
     */
    public String createTestplanProcess() throws Exception {
        Map<String, Object> variables = new HashMap<String, Object>();
        List<String> userids = this.getWorkersList();
        if (!userids.isEmpty()) {
            for (String userid : userids) {
                variables.put("WorkerId", userid);
            }
        } else
            throw new Exception("EMPTY");
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("testplan", variables);
        return pi.getProcessInstanceId();
    }


    /**
     * 新建测试报告实例
     * @param clientId 客户ID
     * @return 以String形式返回流程实例的ID
     * @throws Exception 新建测试报告实例失败
     */
    public String createTestreportProcess(String clientId) throws Exception {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("ClientID", clientId);
        List<String> userids = this.getWorkersList();
        if (!userids.isEmpty()) {
            for (String userid : userids) {
                variables.put("WorkerId", userid);
            }
        } else
            throw new Exception("EMPTY");
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("testreport", variables);
        return pi.getProcessInstanceId();
    }

    /**
     * 根据具体流程实例的ID，更新其状态
     * 
     * @param processInstanceId 流程实例ID
     * @param request 要执行的操作、请求
     * @throws Exception 更新流程实例状态失败
     */
    public void updateProcess(String processInstanceId, Request request) throws Exception {
        JSONObject params = request.getParams();
        String object = params.getString("object");
        String operation = params.getString("operation");
        // String comments=params.getString("comments");
        boolean test=true;
        if (object == null) {
            throw new Exception("object is null");
        }
        for(TaskOperationwithGate operationwithGate:TaskOperationwithGate.values()) {
            if(operation.contains(operationwithGate.name())) {
                String comments=params.getString("comments");
                baseOperation.containGate(operation, processInstanceId, request.getUser().getId(),comments);
                test=false;
                break;
            }
        }
        if(test==true) {
            for(TaskOperationnoGate operationnoGate:TaskOperationnoGate.values()) {
                if(operation.contains(operationnoGate.name())) {
                    baseOperation.noGate(processInstanceId, request.getUser().getId());
                    test=false;
                    break;
                }
            }
        }
       /* if (operation.contains(TaskOperation.Submit.name()) || operation.contains(TaskOperation.Write.name())
                || operation.contains(TaskOperation.Implement.name()) || operation.contains(TaskOperation.Send.name())
                || operation.contains(TaskOperation.Done.name()) || operation.contains(TaskOperation.Fil.name())
                || operation.contains(TaskOperation.Satisfact.name())) {
            baseOperation.noGate(processInstanceId, request.getUser().getId());
        } else if(operation.contains(TaskOperation.Review.name()) || operation.contains(TaskOperation.Confirm.name())
                || operation.contains(TaskOperation.Approve.name())){
            String comments=params.getString("comments");
            baseOperation.containGate(operation, processInstanceId, request.getUser().getId(),comments);*/
         if(test==true){
            throw new Exception("Operation match failed");
        }
    }


    /**
     * 根据具体流程实例的ID获取其在流程中的状态
     * @param processInstanceId 流程实例ID
     * @return 以String形式返回流程实例的状态
     * @throws Exception 获取流程实例状态失败
     */
    public String getProcessState(String processInstanceId) throws Exception {
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
                .singleResult();
        List<HistoricActivityInstance> pi1 = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId).list();
        if (pi == null && !pi1.isEmpty()) {
            return "Finished";
        } else if (pi != null) {
            Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
            return task.getName();
        } else {
            return "NotExist";
        }
    }


    /**
     * 获取当前task用户可执行的权限
     * @param processInstanceId 流程实例ID
     * @return 以String形式返回用户可执行的操作
     * @throws Exception 获取当前task用户可执行的权限
     */
    public List<String> getUserOperation(String processInstanceId) throws Exception {
        List<String> varies = new ArrayList<String>();
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        String S = this.getProcessState(processInstanceId);
        if (S.equals("Finished")) {
            varies.add("Finish");
            return varies;
        }
        TaskFormData taskFormData = formService.getTaskFormData(task.getId());
        List<FormProperty> formProperties = taskFormData.getFormProperties();
        boolean test=true;
        for(TaskOperationnoGate operationnoGate:TaskOperationnoGate.values()){
            if(task.getName().contains(operationnoGate.name())){
                varies.add(operationnoGate.name());
                test=false;
                break;
            }
        }
        if(test==true){
            for (TaskOperationwithGate s : TaskOperationwithGate.values()) {
                if (task.getName().contains(s.name())) {
                    String s1 = s.name() + "Pass";
                    varies.add(s1);
                    String s2 = s.name() + "Reject";
                    varies.add(s2);
                    // System.out.println(task.getProcessDefinitionId());
                    if (s.name().equals("Confirm") && task.getProcessDefinitionId().contains("testreport")) {
                        String s3 = s.name() + "Abort";
                        varies.add(s3);
                    }
                    break;
                }
            }
            return varies;
        }
        return varies;
        /*if (formProperties.size() == 1) {
            for (TaskOperation s : TaskOperation.values()) {
                if (task.getName().contains(s.name())) {
                    varies.add(s.name());
                    break;
                }
            }
            return varies;
        } else if (formProperties.size() > 1) {
            for (TaskOperation s : TaskOperation.values()) {
                if (task.getName().contains(s.name())) {
                    String s1 = s.name() + "Pass";
                    varies.add(s1);
                    String s2 = s.name() + "Reject";
                    varies.add(s2);
                    // System.out.println(task.getProcessDefinitionId());
                    if (s.name().equals("Confirm") && task.getProcessDefinitionId().contains("testreport")) {
                        String s3 = s.name() + "Abort";
                        varies.add(s3);
                    }
                    break;
                }
            }
            return varies;
        }
        return varies;*/
    }


    /**
     * 获取当前task的用户类型，若流程结束，返回nouser
     * @param processInstanceId 流程实例ID
     * @return 以String形式返回用户任务的候选执行者
     * @throws Exception 获取当前task的用户类型为空
     */
    public String getTaskAssignee(String processInstanceId) throws Exception {
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        String S = this.getProcessState(processInstanceId);
        String user = "";
        if (S.equals("Finished")) {
            user = "NoUser";
            return user;
        }
        TaskFormData taskFormData = formService.getTaskFormData(task.getId());
        List<FormProperty> formProperties = taskFormData.getFormProperties();
        if (formProperties.isEmpty() == false) {
            for (FormProperty formProperty : formProperties) {
                if ("user".equals(formProperty.getId().toString()))
                    user = formProperty.getName();
            }
        } else
            throw new Exception("empty error");
        return user;
    }


    /**
     * 查询某个流程实例的历史活动的详细信息
     * @param processInstanceId 流程实例ID
     * @return 以String形式返回历史任务的信息
     * @throws Exception 查询流程实例的历史活动为空
     */
    public List<String> queryHistoricTask(String processInstanceId) throws Exception {
        List<HistoricTaskInstance> hti = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId).orderByHistoricTaskInstanceStartTime().asc().list();
        List<String> htiList = new ArrayList<String>();
        if (hti.isEmpty() == false) {
            for (HistoricTaskInstance temp : hti) {
                htiList.add(temp.getId() + " " + temp.getAssignee() + " " + temp.getName() + " " + temp.getEndTime()
                        + '\n');
            }
            return htiList;
        } else
            throw new Exception("historicList is null");
    }

    /**
     * 获取用户意见
     * @param processInstanceId 流程实例ID
     * @return 以String形式返回意见
     * @throws Exception 获取用户意见失败
     */
    public List<String> getComments(String processInstanceId) throws Exception {
        List<String> list=new ArrayList<String>();
        List<HistoricVariableInstance> hviList=historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId).orderByVariableName().desc().list();
        for(HistoricVariableInstance hti:hviList)
        {
            if(hti.getVariableName().contains("comments"))
                list.add(hti.getVariableName()+"   "+hti.getValue());
        }
       /* List<HistoricTaskInstance> htiList=historyService.createHistoricTaskInstanceQuery().processInstanceId(
            processInstanceId).orderByHistoricTaskInstanceStartTime().asc().list();
        for(HistoricTaskInstance task:htiList)
        {
            System.out.println(task.getId()+task.getName());
            TaskFormData taskFormData = formService.getTaskFormData(task.getId());
            List<FormProperty> formProperties = taskFormData.getFormProperties();
            if (formProperties.isEmpty() == false) {
                for (FormProperty formProperty : formProperties) {
                    list.add(formProperty.getName()+"      "+formProperty.getValue());
                }
            }
        }*/
    return list;
    }

    /**
     * 获取历史任务
     * @param processInstanceId 流程实例ID
     * @return 以String形式返回历史任务的ID
     * @throws Exception 获取历史任务失败
     */
    public String getLastTask(String processInstanceId) throws Exception{
        List<HistoricTaskInstance> htiList=historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId)
                .orderByHistoricTaskInstanceStartTime().desc().list();
        return htiList.get(0).getName().toString();
    }
}