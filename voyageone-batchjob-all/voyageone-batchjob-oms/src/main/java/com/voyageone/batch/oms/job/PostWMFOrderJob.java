package com.voyageone.batch.oms.job;

import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.dao.TaskDao;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.oms.service.PostWMFOrderService;
import com.voyageone.common.components.issueLog.IssueLog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PostWMFOrderJob {
	
	private static Log logger = LogFactory.getLog(PostWMFOrderJob.class);
	
	@Autowired
	TaskDao taskDao;
	
	@Autowired
	PostWMFOrderService postWMFOrderService;
	
	@Autowired
	IssueLog issueLog;

	// 准备取消订单推送
	private final static String POST_WMF_CANCEL_ORDER = "PostWMFPendingCancelOrder";


	/**
	 * 准备取消订单推送
	 */
	public void postWMFPendingCancelOrder() throws Exception {

		List<TaskControlBean> taskControlList = taskDao.getTaskControlList(POST_WMF_CANCEL_ORDER);
		// 是否可以运行的判断
		if (TaskControlUtils.isRunnable(taskControlList, POST_WMF_CANCEL_ORDER) == false) {
			return;
		}
		String taskID =  TaskControlUtils.getTaskId(taskControlList);
		logger.info(POST_WMF_CANCEL_ORDER + "任务开始");
		// 任务监控历史记录添加:启动
		taskDao.insertTaskHistory(taskID, TaskControlEnums.Status.START.getIs());

		boolean isSuccess = true;

		isSuccess = postWMFOrderService.postWMFPendingCancelOrder();

		// 任务监控历史记录添加:结束
		String result = "";
		if (isSuccess) {
			result = TaskControlEnums.Status.SUCCESS.getIs();
		} else {
			result = TaskControlEnums.Status.ERROR.getIs();
		}
		taskDao.insertTaskHistory(taskID, result);

		logger.info(POST_WMF_CANCEL_ORDER + "任务结束");
	}

}

