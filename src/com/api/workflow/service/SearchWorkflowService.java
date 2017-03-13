package com.api.workflow.service;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import weaver.conn.RecordSet;
import weaver.general.PageIdConst;
import weaver.general.Util;
import weaver.hrm.HrmUserVarify;
import weaver.hrm.User;
import weaver.search.SearchClause;
import weaver.systeminfo.SystemEnv;
import weaver.systeminfo.menuconfig.LeftMenuInfo;
import weaver.systeminfo.menuconfig.LeftMenuInfoHandler;
import weaver.workflow.request.WFShareAuthorization;
import weaver.workflow.search.WfAdvanceSearchUtil;
import weaver.workflow.workflow.WorkTypeComInfo;
import weaver.workflow.workflow.WorkflowComInfo;
import weaver.workflow.workflow.WorkflowVersion;

import com.api.workflow.bean.SearchConditionItem;
import com.api.workflow.bean.SearchConditionOption;
import com.api.workflow.bean.WfTreeNode;
import com.api.workflow.util.ConditionKeyFactory;
import com.api.workflow.util.PageUidFactory;
import com.api.workflow.util.SearchConditionUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cloudstore.dev.api.util.Util_TableMap;

/**
 * 查询流程相关接口
 * @author liuzy 2017/2/15
 */
@Path("/workflow/search")
public class SearchWorkflowService {

	@GET
	@Path("/condition")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCondition(@Context HttpServletRequest request, @Context HttpServletResponse response){
		User user = HrmUserVarify.getUser(request, response);
		List<Map<String,Object>> grouplist = new ArrayList<Map<String,Object>>();
		
		Map<String,Object> groupitem1 = new HashMap<String,Object>();
		List<SearchConditionItem> itemlist1 = new ArrayList<SearchConditionItem>();
		groupitem1.put("title", SystemEnv.getHtmlLabelName(32905, user.getLanguage()));
		groupitem1.put("defaultshow", true);
		groupitem1.put("items", itemlist1);
		grouplist.add(groupitem1);
		
		Map<String,Object> groupitem2 = new HashMap<String,Object>();
		List<SearchConditionItem> itemlist2 = new ArrayList<SearchConditionItem>();
		groupitem2.put("title", SystemEnv.getHtmlLabelName(32843,user.getLanguage()));
		groupitem2.put("defaultshow", true);
		groupitem2.put("items", itemlist2);
		grouplist.add(groupitem2);
		
		List<SearchConditionOption> requestLevelOption = SearchConditionUtil.getRequestLevelOption(user.getLanguage());
		List<SearchConditionOption> manageStatusOption = SearchConditionUtil.getManageStatusOption(user.getLanguage());
		List<SearchConditionOption> archiveStatusOption = SearchConditionUtil.getArchiveStatusOption(user.getLanguage());
		List<SearchConditionOption> vaildStatusOption = SearchConditionUtil.getVaildStatusOption(user.getLanguage());
		List<SearchConditionOption> nodeTypeOption = SearchConditionUtil.getNodeTypeOption(user.getLanguage());
		//生成常用条件
		itemlist1.add(new SearchConditionItem(ConditionKeyFactory.KEY1, SystemEnv.getHtmlLabelName(229, user.getLanguage()),
			"", new String[]{"requestname"}, null, 6, 18));
		itemlist1.add(new SearchConditionItem(ConditionKeyFactory.KEY1, SystemEnv.getHtmlLabelName(19502, user.getLanguage()),
			"", new String[]{"workcode"}, null, 6, 18));
		itemlist1.add(new SearchConditionItem(ConditionKeyFactory.KEY8, SystemEnv.getHtmlLabelName(33234, user.getLanguage()),
			"", new String[]{"typeid"}, null, 6, 18));
		itemlist1.add(new SearchConditionItem(ConditionKeyFactory.KEY9, SystemEnv.getHtmlLabelName(26361, user.getLanguage()),
			"typeid", new String[]{"workflowid"}, null, 6, 18));
		itemlist1.add(new SearchConditionItem(ConditionKeyFactory.KEY2, SystemEnv.getHtmlLabelName(15534, user.getLanguage()),
			"", new String[]{"requestlevel"}, requestLevelOption, 6, 18));
		itemlist1.add(new SearchConditionItem(ConditionKeyFactory.KEY4, SystemEnv.getHtmlLabelName(882, user.getLanguage()),
			"", new String[]{"creatertype","createrid","createrid2"}, null, 6, 18));
		//生成其他条件
		itemlist2.add(new SearchConditionItem(ConditionKeyFactory.KEY6, SystemEnv.getHtmlLabelName(19225, user.getLanguage()),
			"", new String[]{"ownerdepartmentid"}, null, 6, 18));
		itemlist2.add(new SearchConditionItem(ConditionKeyFactory.KEY7, SystemEnv.getHtmlLabelName(22788, user.getLanguage()),
			"", new String[]{"creatersubcompanyid"}, null, 6, 18));
		itemlist2.add(new SearchConditionItem(ConditionKeyFactory.KEY3, SystemEnv.getHtmlLabelName(722, user.getLanguage()),
			"", new String[]{"createdateselect","createdatefrom","createdateto"}, SearchConditionUtil.getDateSelectOption(user.getLanguage()), 6, 18));
		itemlist2.add(new SearchConditionItem(ConditionKeyFactory.KEY3, SystemEnv.getHtmlLabelName(17994, user.getLanguage()),
			"", new String[]{"recievedateselect","recievedatefrom","recievedateto"}, SearchConditionUtil.getDateSelectOption(user.getLanguage()), 6, 18));
		itemlist2.add(new SearchConditionItem(ConditionKeyFactory.KEY2, SystemEnv.getHtmlLabelNames("553,602", user.getLanguage()),
			"", new String[]{"wfstatu"}, manageStatusOption, 6, 18));
		itemlist2.add(new SearchConditionItem(ConditionKeyFactory.KEY2, SystemEnv.getHtmlLabelName(15112, user.getLanguage()),
			"", new String[]{"archivestyle"}, archiveStatusOption, 6, 18));
		itemlist2.add(new SearchConditionItem(ConditionKeyFactory.KEY2, SystemEnv.getHtmlLabelName(19061, user.getLanguage()),
			"", new String[]{"isdeleted"}, vaildStatusOption, 6, 18));
		itemlist2.add(new SearchConditionItem(ConditionKeyFactory.KEY2, SystemEnv.getHtmlLabelName(15536, user.getLanguage()),
			"", new String[]{"nodetype"}, nodeTypeOption, 6, 18));
		itemlist2.add(new SearchConditionItem(ConditionKeyFactory.KEY5, SystemEnv.getHtmlLabelName(16354, user.getLanguage()),
			"", new String[]{"unophrmid"}, null, 6, 18));
		itemlist2.add(new SearchConditionItem(ConditionKeyFactory.KEY11, SystemEnv.getHtmlLabelName(857, user.getLanguage()),
			"", new String[]{"docids"}, null, 6, 18));
		itemlist2.add(new SearchConditionItem(ConditionKeyFactory.KEY5, SystemEnv.getHtmlLabelName(179, user.getLanguage()),
			"", new String[]{"hrmcreaterid"}, null, 6, 18));
		itemlist2.add(new SearchConditionItem(ConditionKeyFactory.KEY12, SystemEnv.getHtmlLabelName(783, user.getLanguage()),
			"", new String[]{"crmids"}, null, 6, 18));
		itemlist2.add(new SearchConditionItem(ConditionKeyFactory.KEY13, SystemEnv.getHtmlLabelName(782, user.getLanguage()),
			"", new String[]{"proids"}, null, 6, 18));
		
		Map<String,Object> retmap = new HashMap<String,Object>();
		retmap.put("condition", grouplist);
		return JSONObject.toJSONString(retmap);
	}
	
	@GET
	@Path("/pagingresult")
	@Produces(MediaType.TEXT_PLAIN)
	public String getPagingResult(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		Map<String,Object> retmap = new HashMap<String,Object>();
		try{
			User user = HrmUserVarify.getUser(request, response);
			HttpSession session = request.getSession();
			RecordSet RecordSet = new RecordSet();
			WorkflowComInfo WorkflowComInfo = new WorkflowComInfo();
			WFShareAuthorization wfShareAuthorization = new WFShareAuthorization();
			//SearchClause SearchClause = (SearchClause)session.getAttribute("SearchClause");
			SearchClause SearchClause = new SearchClause();
			if(SearchClause == null)	SearchClause = new SearchClause();
			//RequestUtil requestutil = new RequestUtil();
			
			boolean isopenos = false; //RequestUtil.isOpenOtherSystemToDo();
			String isfrom = Util.null2String(request.getParameter("isfrom"));//是否来自自定义查询
//			String fromhp = Util.null2String((String) session.getAttribute("fromhp"));//来自门户
//			String isall = Util.null2String((String) session.getAttribute("isall"));//来自门户所有流程
	
			WfAdvanceSearchUtil wfd = new WfAdvanceSearchUtil(request, RecordSet);
			String offical = Util.null2String(request.getParameter("offical"));
			int officalType = Util.getIntValue(request.getParameter("officalType"), -1);
	
			String typeid = Util.null2String(request.getParameter("typeid"));//流程类型id
			String workflowid = Util.null2String(request.getParameter("workflowid"));//流程id
			String nodetype = Util.null2String(request.getParameter("nodetype"));//节点类型
			String createrid = Util.null2String(request.getParameter("createrid"));//创建人id
			String requestlevel = Util.null2String(request.getParameter("requestlevel"));//紧急程度
	
			String workcode = Util.null2String(request.getParameter("workcode"));//编号
			String requestname = Util.fromScreen2(request.getParameter("requestname"), user.getLanguage());
			if (!"".equals(SearchClause.getRequestName())) {
				requestname = SearchClause.getRequestName();
				SearchClause.setRequestName("");
			}
			requestname = requestname.trim();//标题
	
			String ownerdepartmentid = Util.null2String(request.getParameter("ownerdepartmentid"));//创建人部门
			String creatersubcompanyid = Util.null2String(request.getParameter("creatersubcompanyid"));//创建人分部id
			String unophrmid = Util.null2String(request.getParameter("unophrmid"));//未操作者id
			String docids = Util.null2String(request.getParameter("docids"));//相关文档id
			String crmids = Util.null2String(request.getParameter("crmids"));//相关客户id
			String proids = Util.null2String(request.getParameter("proids"));//相关项目id
			String hrmcreaterid = Util.null2String(request.getParameter("hrmcreaterid"));//人力资源
			String requestnamed = Util.null2String(request.getParameter("requestnamed"));//快速搜索
	
			int date2during = Util.getIntValue(Util.null2String(request.getParameter("date2during")), 0);
			int isdeleted = Util.getIntValue(request.getParameter("isdeleted"), 0);//流程状态
			int wfstatu = Util.getIntValue(request.getParameter("wfstatu"), 0);//处理状态
			int archivestyle = Util.getIntValue(request.getParameter("archivestyle"), 0);//归档状态
			String branchid = Util.null2String((String) session.getAttribute("branchid"));
	
			String newsql = "";
			String newsqlos = "";
			//WFSearchs.jsp页面条件
			String viewtype = Util.null2String(request.getParameter("viewtype"));//0：未读  -1：反馈
			if (!viewtype.equals("")) {//未读 or 反馈
				newsql += " and t2.viewtype='" + viewtype + "'";
				newsqlos += " and viewtype=" + viewtype;
			}
			if (!typeid.equals("") && !typeid.equals("0")) {//类别
				newsql += " and t2.workflowtype='" + typeid + "'";
				newsqlos += " and sysid=" + typeid;
			}
			if (!workflowid.equals("") && !workflowid.equals("0")) {
				if (workflowid.indexOf("-") != -1) {
					newsql += " and t1.workflowid in(" + workflowid + ")";
				} else {
					newsql += " and t1.workflowid in(" + WorkflowVersion.getAllVersionStringByWFIDs(workflowid) + ")";
				}
				newsqlos += " and workflowid=" + workflowid;
			}
	
			if (date2during > 0 && date2during < 37) {
				newsql += WorkflowComInfo.getDateDuringSql(date2during);
				newsqlos += WorkflowComInfo.getDateDuringSql(date2during);
			}
	
			String dbNames = new RecordSet().getDBType();
			boolean isoracle = "oracle".equals(dbNames.toLowerCase());
			if (!requestname.equals("")) {
				if ((requestname.indexOf(" ") == -1 && requestname.indexOf("+") == -1)
					|| (requestname.indexOf(" ") != -1 && requestname.indexOf("+") != -1)) {
					if (isoracle) {
						newsql += " and instr(t1.requestnamenew, '" + requestname + "') > 0 ";
						newsqlos += " and instr(requestname, '" + requestname + "') > 0 ";
					} else {
						newsql += " and t1.requestnamenew like '%" + requestname + "%'";
						newsqlos += " and requestname like '%" + requestname + "%'";
					}
				} else if (requestname.indexOf(" ") != -1 && requestname.indexOf("+") == -1) {
					String orArray[] = Util.TokenizerString2(requestname, " ");
					if (orArray.length > 0) {
						newsql += " and ( ";
						newsqlos += " and ( ";
					}
					for (int i = 0; i < orArray.length; i++) {
						if (isoracle) {
							newsql += " and instr(t1.requestnamenew, '" + orArray[i] + "') > 0 ";
						} else {
							newsql += " t1.requestnamenew like '%" + orArray[i] + "%'";
						}
						newsqlos += " requestname like '%" + orArray[i] + "%'";
						if (i + 1 < orArray.length) {
							newsql += " or ";
							newsqlos += " or ";
						}
					}
					if (orArray.length > 0) {
						newsql += " ) ";
						newsqlos += " ) ";
					}
				} else if (requestname.indexOf(" ") == -1 && requestname.indexOf("+") != -1) {
					String andArray[] = Util.TokenizerString2(requestname, "+");
					for (int i = 0; i < andArray.length; i++) {
						if (isoracle) {
							newsql += " and instr(t1.requestnamenew, '" + andArray[i] + "') > 0 ";
						} else {
							newsql += " and t1.requestnamenew like '%" + andArray[i] + "%'";
						}
						newsqlos += " and requestname like '%" + andArray[i] + "%'";
					}
				}
			}
	
			if (!requestnamed.equals("")) {
				if (isoracle) {
					newsql += " and instr(t1.requestnamenew, '" + requestnamed + "') > 0 ";
				} else {
					newsql += " and t1.requestnamenew like '%" + requestnamed + "%'";
				}
				newsqlos += " and requestname like '%" + requestname + "%'";
			}
			if (!"".equals(workcode)) {
				newsql += " AND t1.requestmark LIKE '%" + workcode + "%' ";
				newsqlos += " and 1=2 ";
			}
			if (!requestlevel.equals("")) {
				newsql += " and t1.requestlevel=" + requestlevel;
				newsqlos += " and 1=2 ";
			}
			//创建人条件
			newsql += wfd.handleCreaterCondition("t1.creater");
			if (!createrid.equals("")) {
				newsqlos += "  and creatorid = " + createrid + "";
			}
			//创建人部门
			if (!ownerdepartmentid.equals("")) {
				newsql += " and t1.creatertype= '0' " + wfd.handleDepCondition("t1.creater");
				newsqlos += " and " + wfd.handleDepCondition("creater");
			}
			//创建人分部
			if (!creatersubcompanyid.equals("")) {
				newsql += " and t1.creatertype= '0' " + wfd.handleSubComCondition("t1.creater");
				newsqlos += " and " + wfd.handleSubComCondition("creater");
			}
			//创建日期
			newsql += wfd.handCreateDateCondition("t1.createdate");
			newsqlos += wfd.handCreateDateCondition("createdate");
			//接受日期
			newsql += wfd.handRecieveDateCondition("t2.receivedate");
			newsqlos += wfd.handRecieveDateCondition("receivedate");
			//流程状态
			if (isdeleted == 0) {
				isdeleted = 1;
			} else if (isdeleted == 1) {
				isdeleted = 0;
			}
			if (isdeleted != 1) {
				if (isdeleted == 0) {
					newsql += " and t1.workflowid in (select id from workflow_base where isvalid not in ('1', '2', '3'))";
					newsqlos += " and workflowid in (select workflowid from ofs_workflow where cancel!=0) ";
				} else if (isdeleted == 2) { //全部
					newsql += " and t1.workflowid in (select id from workflow_base where isvalid<>'2')";
					//newsqlos += " and workflowid in (select workflowid from ofs_workflow where cancel=0) ";
				}
			} else {
				newsqlos += " and workflowid in (select workflowid from ofs_workflow where cancel=0) ";
			}
			//处理状态
			if (wfstatu != 0) {
				if (wfstatu == 1) {
					newsql += " AND t2.isremark in( '0','1','5','8','9','7')";
					newsqlos += " and isremark in ('0') ";
				} else {
					newsql += " AND t2.isremark in('2','4')";
					newsqlos += " and isremark in ('2','4') ";
				}
			}
			//归档处理
			if (archivestyle != 0) {
				if (archivestyle == 1) {
					newsql += " and t2.iscomplete<>1";//未归档
					newsqlos += " and iscomplete<>1";
				} else {
					newsql += " and t2.iscomplete=1";//归档
					newsqlos += " and iscomplete=1";
				}
			}
			//节点类型
			if (!nodetype.equals("")) {
				newsql += " and t1.currentnodetype='" + nodetype + "'";
				newsqlos += " and 1=5 ";
			}
			//未操作人
			newsql += wfd.handleUnOpCondition("t2.REQUESTID");
			if (!unophrmid.equals("")) {
				newsqlos += " and requestid in (select requestid from ofs_todo_data where userid=" + unophrmid + " and isremark=0 )";
			}
			//相关文档
			newsql += wfd.handleDocCondition("t1.docids");
			//相关人力资源
			newsql += wfd.handleHrmCondition("t1.hrmids");
			//相关客户
			newsql += wfd.handleCrmCondition("t1.crmids");
			//相关项目
			newsql += wfd.handleProsCondition("t1.prjids");
	
			if (!docids.equals("") || !hrmcreaterid.equals("") || !crmids.equals("") || !proids.equals("")) {//相关客户等不查询
				newsqlos += " and 1=2 ";
			}
	
			String CurrentUser = Util.null2String(request.getParameter("resourceid"));
			if (CurrentUser.equals(""))
				CurrentUser = "" + user.getUID();
			String userID = String.valueOf(user.getUID());
			String belongtoshow = "";
			RecordSet.executeSql("select * from HrmUserSetting where resourceId = " + userID);
			if (RecordSet.next()) {
				belongtoshow = RecordSet.getString("belongtoshow");
			}
			String userIDAll = String.valueOf(user.getUID());
			String Belongtoids = user.getBelongtoids();
			if (!"".equals(Belongtoids))
				userIDAll = userID + "," + Belongtoids;
	
			int usertype = "2".equals(user.getLogintype()) ? 1 : 0;

			boolean superior = false; //是否为被查看者上级或者本身
			if (userID.equals(CurrentUser)) {
				superior = true;
			} else {
				RecordSet.executeSql("SELECT * FROM HrmResource WHERE ID = " + CurrentUser + " AND managerStr LIKE '%" + userID + "%'");
				if (RecordSet.next())
					superior = true;
			}


			//获取流程共享信息
			String currentid = wfShareAuthorization.getCurrentoperatorIDByUser(user);
			String sqlwhere = "";
			String sqlwhereos = "";
			String sqlwhereuserids = "1".equals(belongtoshow) ? userIDAll : CurrentUser;
			Util.null2String(request.getParameter("resourceid"));
			//门户条件--begin
			String jsonstr = Util.null2String(request.getParameter("jsonstr"));;//来自门户所有条件
			String fromhp = "";//来自门户
			String isall = "";//来自门户所有流程
			String hpsqlwhere = getHpSqlWhere(jsonstr,sqlwhereuserids,usertype,user);//门户条件拼接
			if(!"".equals(hpsqlwhere.trim())){
				fromhp = "1";
				isall =hpsqlwhere.split("~splite~")[1];
				hpsqlwhere = hpsqlwhere.split("~splite~")[0];
			}
			//门户条件--end
			
			//是否有共享的流程
			if (!"".equals(currentid) && !"1".equals(fromhp)) {
				sqlwhere = " where (t1.deleted <> 1 or t1.deleted is null or t1.deleted='') and t1.requestid = t2.requestid AND t2.islasttimes=1 ";
				sqlwhere += " and (( t2.userid in (" + sqlwhereuserids + " ) and t2.usertype=" + usertype;
			} else {
				if ("1".equals(fromhp) && "1".equals(isall)) {
					sqlwhere = " where (t1.deleted <> 1 or t1.deleted is null or t1.deleted='') and t1.requestid = t2.requestid AND t2.islasttimes=1 ";
				} else {
				sqlwhere = " where (t1.deleted <> 1 or t1.deleted is null or t1.deleted='') and t1.requestid = t2.requestid and t2.userid in ("
					+ sqlwhereuserids + " ) and t2.usertype=" + usertype + " AND t2.islasttimes=1 ";
				}
			}
			if (RecordSet.getDBType().equals("oracle")) {
				sqlwhere += " and (nvl(t1.currentstatus,-1) = -1 or (nvl(t1.currentstatus,-1)=0 and t1.creater in (" + sqlwhereuserids + "))) ";
			} else {
				sqlwhere += " and (isnull(t1.currentstatus,-1) = -1 or (isnull(t1.currentstatus,-1)=0 and t1.creater in (" + sqlwhereuserids + "))) ";
			}
			sqlwhereos += " and userid in (" + CurrentUser + ") and islasttimes=1 ";
			//是否有共享的流程
			String wfid = Util.getSubINClause(currentid, "t2.id", "IN");
			if (!"".equals(currentid) && !"1".equals(fromhp)) {
				sqlwhere += " ) or (" + wfid + ")) ";
			}
	
			//来自于借款流程查看相关还款流程按钮的查询
			boolean _isFromShowRelatedProcess = Util.getIntValue(request.getParameter("_isFromShowRelatedProcess"), -1) == 1;
			if (_isFromShowRelatedProcess) {
				int _borrowRequestid = Util.getIntValue(request.getParameter("_borrowRequestid"), -1);
				int _borrowDetailRecordId = Util.getIntValue(request.getParameter("_borrowDetailRecordId"), -1);
				if ("".equals(sqlwhere)) {
					sqlwhere += " where ";
				} else {
					sqlwhere += " and ";
				}
				if (_borrowDetailRecordId > 0 && _borrowRequestid > 0) {
					sqlwhere += " exists ( \n" + " select 1 \n" + " from FnaBorrowInfo fbi \n"
						+ " where fbi.borrowDirection = -1  \n" + " and fbi.borrowRequestIdDtlId = "
						+ _borrowDetailRecordId + " and fbi.borrowRequestId = " + _borrowRequestid + " \n"
						+ " and fbi.requestid = t1.requestid \n" + " ) \n";
				} else {
					sqlwhere += " 1=2 ";
				}
			}
			sqlwhere += " " + newsql;
			sqlwhereos += " " + newsqlos;
			session.setAttribute("fromhp", "");
			session.setAttribute("isall", "");
			String orderby = "t2.receivedate,t2.receivetime";
			
			boolean hasSubWorkflow = false;
			if (workflowid != null && !workflowid.equals("") && workflowid.indexOf(",") == -1) {
				RecordSet.executeSql("select id from Workflow_SubWfSet where mainWorkflowId=" + workflowid);
				if (RecordSet.next()) {
					hasSubWorkflow = true;
				}
				RecordSet.executeSql("select id from Workflow_TriDiffWfDiffField where mainWorkflowId=" + workflowid);
				if (RecordSet.next()) {
					hasSubWorkflow = true;
				}
			}
	
			String tableString = "";
			String backfields = " t1.requestid, t1.createdate, t1.createtime,t1.creater, t1.creatertype, t1.workflowid, t1.requestname, t1.status,t1.requestlevel,t1.currentnodeid,t2.receivedate,t2.receivetime,t2.viewtype,t2.isremark,t2.userid,t2.nodeid,t2.agentorbyagentid,t2.agenttype,t2.isprocessed, 0 as systype,t2.workflowtype";
			String fromSql = " from workflow_requestbase t1,workflow_currentoperator t2 ";//xxxxx
			String sqlWhere = sqlwhere;
			if (sqlWhere.indexOf("in (select id from workflow_base where isvalid") < 0) {
				sqlWhere += " and t1.workflowid in (select id from workflow_base where (isvalid='1' or isvalid='3') )";
			}
			
			String para2 = "column:requestid+column:workflowid+column:viewtype+0+" + user.getLanguage()
				+ "+column:nodeid+column:isremark+" + user.getUID()
				+ "+column:agentorbyagentid+column:agenttype+column:isprocessed+" + currentid
				+ "+column:userid+column:workflowtype+column:creater";
	
			String para4 = user.getLanguage() + "+" + user.getUID() + "+column:userid";
			if (!docids.equals("")) {
				fromSql = fromSql + ",workflow_form t4 ";
				sqlWhere = sqlWhere + " and t1.requestid=t4.requestid ";
			}
			
			if (!superior) {
				sqlWhere += " AND EXISTS (SELECT 1 FROM workFlow_CurrentOperator workFlowCurrentOperator WHERE t2.workflowid = workFlowCurrentOperator.workflowid AND t2.requestid = workFlowCurrentOperator.requestid AND workFlowCurrentOperator.userid in ("
					+ sqlwhereuserids + ") and workFlowCurrentOperator.usertype = " + usertype + ") ";
			}
			if (!branchid.equals("")) {
				sqlWhere += " AND t1.creater in (select id from hrmresource where subcompanyid1=" + branchid + ")  ";
			}
			if (offical.equals("1")) {//发文/收文/签报
				if (officalType == 1) {
					sqlWhere += (" and t1.workflowid in (select id from workflow_base where isWorkflowDoc=1 and officalType in (1,3) and (isvalid=1 or isvalid=3))");
				} else if (officalType == 2) {
					sqlWhere += (" and t1.workflowid in (select id from workflow_base where isWorkflowDoc=1 and officalType=2 and (isvalid=1 or isvalid=3))");
				}
			}
	
			//判断session中是否存在附加条件
//			String __whereClause = SearchClause.getWhereClause();
//			if (__whereClause != null && !__whereClause.trim().equals("")) {
//				if (__whereClause.trim().startsWith("and")) {
//					sqlWhere += __whereClause;
//				} else {
//					sqlWhere += (" and " + __whereClause);
//				}
//			}
			
			if (hpsqlwhere != null && !hpsqlwhere.trim().equals("")) {
				if (hpsqlwhere.trim().startsWith("and")) {
					sqlWhere += hpsqlwhere;
				} else {
					sqlWhere += (" and " + hpsqlwhere);
				}
			}
			
			
			String __whereClauseOs = SearchClause.getWhereclauseOs();
			if (__whereClauseOs != null && !__whereClauseOs.trim().equals("")) {
				if (__whereClauseOs.trim().startsWith("and")) {
					sqlwhereos += __whereClauseOs;
				} else {
					sqlwhereos += (" and " + __whereClauseOs);
				}
			}
			String __orderClause = SearchClause.getOrderClause();
			if (__orderClause != null && !__orderClause.trim().equals("")) {
				orderby = __orderClause;
			}
	
			//判断是否是从WFCustomSearchMiddleHandler转发过来的请求
			String[] _fieldLabelArray = null;
			String[] _fieldNameArray = null;
			List<String> fieldids = null;
			List<String> fieldhtmltypes = null;
			List<String> fieldtypes = null;
			List<String> fielddbtypes = null;
			String isNeed = (String) request.getAttribute("CustomSearch_IsNeed");
			if (isNeed != null) {
				String _tableName = (String) request.getAttribute("CustomSearch_TableName");
				String _tableNameAlias = (String) request.getAttribute("CustomSearch_TableNameAlias");
				fieldids = (List<String>) request.getAttribute("CUSTOMSEARCH_FIELDIDS");
				fieldhtmltypes = (List<String>) request.getAttribute("CUSTOMSEARCH_FIELDHTMLTYPES");
				fieldtypes = (List<String>) request.getAttribute("CUSTOMSEARCH_FIELDTYPES");
				fielddbtypes = (List<String>) request.getAttribute("CUSTOMSEARCH_FIELDDBTYPES");
				if (_tableName != null && !_tableName.trim().equals("")) {
					fromSql += ("," + _tableName + " " + _tableNameAlias + " ");
				}
	
				String _fieldNameList = (String) request.getAttribute("CustomSearch_FieldNameList");
				if (_fieldNameList != null && !_fieldNameList.trim().isEmpty()) {
					backfields += ("," + _fieldNameList);
					_fieldNameArray = _fieldNameList.split(",");
				} else {
					_fieldNameArray = new String[] {};
				}
	
				String _requestIdList = (String) request.getAttribute("CustomSearch_RequestIdList");
				if (_requestIdList != null && !_requestIdList.trim().equals("")) {
					sqlWhere += (" and t1.requestid = " + _tableNameAlias + ".requestid");
					sqlWhere += (" and t1.requestId in " + _requestIdList);
				}
	
				String _fieldLabelList = (String) request.getAttribute("CustomSearch_FieldLabelList");
				if (_fieldLabelList != null && !_fieldLabelList.trim().isEmpty()) {
					_fieldLabelArray = _fieldLabelList.split(",");
				} else {
					_fieldLabelArray = new String[] {};
				}
			}
	
			if (isNeed != null || "customSearch".equals(isfrom)) {
				isopenos = false;
			}
	
			//out.println(sqlWhere);
			//主要用于 显示定制列以及 表格 每页展示记录数选择
			String pageIdStr = offical.equals("1") ? "13" : "8";
			String pageid = PageIdConst.WF_CUSTOM_SEARCH;
			String pageUid = PageUidFactory.getWfPageUid("SearchWorkflow");
			
			//rslog.writeLog("20170307 === fromSql=> "+fromSql+"||sqlWhere="+sqlWhere);
			
			tableString = " <table instanceid=\"" + PageIdConst.getWFPageId(pageIdStr) + "\" tabletype=\"checkbox\" pageId=\""
				+ pageid + "\" pageUid=\"" + pageUid + "\" pagesize=\"" + PageIdConst.getPageSize(pageid, user.getUID()) + "\" >";
			if ("customSearch".equals(isfrom)) {
				tableString += " <checkboxpopedom  id=\"checkbox\" popedompara=\"column:workflowid+column:isremark+column:requestid+column:nodeid+column:userid\" showmethod=\"weaver.general.WorkFlowTransMethod.getWFSearchResultCheckBox\"  />";
			} else {
				tableString += " <checkboxpopedom  id=\"checkbox\" popedompara=\"column:workflowid+column:requestid+column:userid+"
					+ userID + "\" showmethod=\"weaver.workflow.request.WFShareTransMethod.getWFSearchCheckbox\"  />";
			}
	
			if (isopenos) {//开启统一待办
				String backfields0 = " requestid, createdate, createtime,creater, creatertype, workflowid, requestname, status,requestlevel,currentnodeid,receivedate,receivetime,viewtype,isremark,userid,nodeid,agentorbyagentid,agenttype,isprocessed, systype,workflowtype";
				String backfieldsos = " requestid, createdate, createtime,creatorid as creater, 0 as creatertype, workflowid, requestname, '' as status, 0 as requestlevel, 0 as currentnodeid,receivedate,receivetime,viewtype,isremark,userid,0 as nodeid, -1 as agentorbyagentid,'0' as agenttype,'0' as isprocessed,1 as systype,sysid as workflowtype";
				fromSql = " from (select " + backfields0 + " from (select " + backfields + " " + fromSql + "" + sqlWhere
					+ " union (select " + backfieldsos + " from ofs_todo_data where 1=1 " + sqlwhereos + ") ) t1 ) t2 ";
				orderby = " receivedate ";
				tableString += " <sql backfields=\"" + backfields0 + "\" sqlform=\"" + Util.toHtmlForSplitPage(fromSql) + "\" sqlwhere=\"\"  sqlorderby=\"" 
					+ orderby + "\"  sqlprimarykey=\"t2.requestid\" sqlsortway=\"Desc\" sqlisdistinct=\"false\" />";
			} else {
				tableString += " <sql backfields=\"" + backfields + "\" sqlform=\"" + fromSql + "\" sqlwhere=\"" + Util.toHtmlForSplitPage(sqlWhere) + "\"  sqlorderby=\"" 
					+ orderby + "\"  sqlprimarykey=\"t2.requestid\" sqlsortway=\"Desc\" sqlisdistinct=\"false\" />";
			}
			tableString += "	<head>";
			if (isopenos) {		//统一待办 liuzy
				/*String showname = requestutil.getOfsSetting().getShowsysname();
				if (!showname.equals("0")) {
					tableString += "<col width=\"8%\" text=\"" + SystemEnv.getHtmlLabelName(22677, user.getLanguage())
						+ "\" column=\"workflowtype\"  orderkey=\"workflowtype\" transmethod=\"weaver.workflow.request.todo.RequestUtil.getSysname\" otherpara=\"" + showname + "\" />";
				}*/
			}
			tableString += "<col width=\"10%\" text=\"" + SystemEnv.getHtmlLabelName(722, user.getLanguage())
				+ "\" column=\"createdate\" orderkey=\"createdate,createtime\" otherpara=\"column:createtime\" transmethod=\"weaver.general.WorkFlowTransMethod.getWFSearchResultCreateTime\" />";
			tableString += "<col width=\"8%\" text=\"" + SystemEnv.getHtmlLabelName(882, user.getLanguage())
				+ "\" column=\"creater\" orderkey=\"creater\"  otherpara=\"column:creatertype\" transmethod=\"weaver.general.WorkFlowTransMethod.getWFSearchResultName\" />";
			if(isopenos){
				tableString += "<col width=\"10%\" text=\"" + SystemEnv.getHtmlLabelName(259, user.getLanguage())
					+ "\" column=\"workflowid\" orderkey=\"workflowid\" otherpara=\"column:workflowtype\" transmethod=\"weaver.workflow.request.todo.RequestUtil.getWorkflowName\" />";
			}else{
				tableString += "<col width=\"10%\" text=\"" + SystemEnv.getHtmlLabelName(259, user.getLanguage())
					+ "\" column=\"workflowid\" orderkey=\"t1.workflowid\" transmethod=\"weaver.workflow.workflow.WorkflowComInfo.getWorkflowname\" />";
			}
			tableString += "<col width=\"8%\" text=\"" + SystemEnv.getHtmlLabelName(15534, user.getLanguage())
				+ "\" column=\"requestlevel\"  orderkey=\"requestlevel\" transmethod=\"weaver.general.WorkFlowTransMethod.getWFSearchResultUrgencyDegree\" otherpara=\"" + user.getLanguage() + "\"/>";
	
			if (!userIDAll.equals(String.valueOf(user.getUID())) && "1".equals(belongtoshow)) {
				tableString += "<col width=\"20%\" text=\"" + SystemEnv.getHtmlLabelName(1334, user.getLanguage())
					+ "\" column=\"requestname\" orderkey=\"requestname\"  linkkey=\"requestid\" linkvaluecolumn=\"requestid\" target=\"_fullwindow\" transmethod=\"com.api.workflow.util.WorkFlowSPATransMethod.getWfShareLinkWithTitle2\"  otherpara=\""
					+ para2 + "\" pkey=\"requestname+weaver.general.WorkFlowTransMethod.getWfShareLinkWithTitle\"/>";
			} else {
				tableString += "<col width=\"20%\" text=\"" + SystemEnv.getHtmlLabelName(1334, user.getLanguage())
					+ "\" column=\"requestname\" orderkey=\"requestname\"  linkkey=\"requestid\" linkvaluecolumn=\"requestid\" target=\"_fullwindow\" transmethod=\"com.api.workflow.util.WorkFlowSPATransMethod.getWfShareLinkWithTitle\"  otherpara=\""
					+ para2 + "\" pkey=\"requestname+weaver.general.WorkFlowTransMethod.getWfShareLinkWithTitle\"/>";
			}
			if(isopenos){
				tableString += "<col width=\"8%\" text=\"" + SystemEnv.getHtmlLabelName(18564, user.getLanguage())
					+ "\" column=\"currentnodeid\" transmethod=\"weaver.workflow.request.todo.RequestUtil.getCurrentNode\" otherpara=\"column:requestid+column:workflowid+column:workflowtype\" />";
			}else{
				tableString += "<col width=\"8%\" text=\"" + SystemEnv.getHtmlLabelName(18564, user.getLanguage())
					+ "\" column=\"currentnodeid\" transmethod=\"weaver.general.WorkFlowTransMethod.getCurrentNode\"/>";
			}
			tableString += "<col width=\"10%\" text=\"" + SystemEnv.getHtmlLabelName(17994, user.getLanguage())
				+ "\" column=\"receivedate\" orderkey=\"receivedate,receivetime\" otherpara=\"column:receivetime\" transmethod=\"weaver.general.WorkFlowTransMethod.getWFSearchResultCreateTime\" />";
			tableString += "<col width=\"10%\" text=\"" + SystemEnv.getHtmlLabelName(1335, user.getLanguage()) + "\" column=\"status\" orderkey=\"status\" />";
			if (offical.equals("1")) {
				tableString += "<col width=\"10%\" text=\"" + SystemEnv.getHtmlLabelName(1265, user.getLanguage())
					+ "\" _key=\"officalbodytext\" column=\"requestid\" linkkey=\"requestid\" linkvaluecolumn=\"requestid\" target=\"_fullwindow\" transmethod=\"weaver.general.WorkFlowTransMethod.getContentNewLinkWithTitle\"  otherpara=\""
					+ para2 + "\"  />";
			}
			tableString += "<col width=\"10%\" text=\"" + SystemEnv.getHtmlLabelName(16354, user.getLanguage())
				+ "\" _key=\"unoperators\" column=\"requestid\"  otherpara=\"" + para4 + "\" transmethod=\"weaver.general.WorkFlowTransMethod.getUnOperators\"/>";
			tableString += "<col width=\"10%\" display=\"" + hasSubWorkflow + "\" text=\"" + SystemEnv.getHtmlLabelName(19363, user.getLanguage())
				+ "\" _key=\"subwflink\" column=\"requestid\" orderkey=\"t2.requestid\"  linkkey=\"requestid\" linkvaluecolumn=\"requestid\" target=\"_self\" transmethod=\"weaver.general.WorkFlowTransMethod.getSubWFLink\"  otherpara=\""
				+ user.getLanguage() + "\"/>";
	
			/*自定义查询定义的需要展示的列*/
			if (isNeed != null && "customSearch".equals(isfrom)) {
				for (int i = 0; i < _fieldNameArray.length; i++) {
					String _fieldName = _fieldNameArray[i];
					String _fieldLabel = _fieldLabelArray[i];
					String _otherPara = fieldids.get(i) + "+" + fieldhtmltypes.get(i) + "+" + fieldtypes.get(i) + "+"
						+ fielddbtypes.get(i) + "+" + user.getLanguage();
					int indexOfName = _fieldName.indexOf(".");
					if (indexOfName >= 0) {
						//column不支持表名加列名形式
						_fieldName = _fieldName.substring(indexOfName + 1);
					}
					//column不支持AS形式
					if (_fieldName.indexOf(" as ") >= 0) {
						_fieldName = _fieldName.substring(_fieldName.indexOf(" as ") + 4);
					}
					if (_fieldName.indexOf(" AS ") >= 0) {
						_fieldName = _fieldName.substring(_fieldName.indexOf(" AS ") + 4);
					}
					tableString += "<col width='15%' name='" + _fieldName + "' text='" + _fieldLabel + "' column='" + _fieldName
						+ "' transmethod=\"weaver.workflow.search.WorkflowSearchUtil.getFieldValueShowStringFromTable\" otherpara=\"" + _otherPara + "\" />";
				}
			}
			tableString += "</head>" + "</table>";
			
			String sessionkey = user.getUID()+"_"+pageUid;
			Util_TableMap.setVal(sessionkey, tableString);
			//System.err.println(tableString);
			retmap.put("sessionkey", sessionkey);
		}catch(Exception e){
			retmap.put("api_status", false);
			retmap.put("api_errormsg", e.getMessage());
			e.printStackTrace();
		}
		return JSONObject.toJSONString(retmap);
	}
	
	@GET
	@Path("/resulttree")
	@Produces(MediaType.TEXT_PLAIN)
	public String getResultTree(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		Map<String,Object> retmap = new HashMap<String,Object>();
		try{
			User user = HrmUserVarify.getUser(request, response);
			HttpSession session = request.getSession();
			RecordSet RecordSet = new RecordSet();
			WorkflowComInfo WorkflowComInfo = new WorkflowComInfo();
			WFShareAuthorization wfShareAuthorization = new WFShareAuthorization();
			SearchClause SearchClause = new SearchClause();
			if(SearchClause == null)	SearchClause = new SearchClause();
			
			List<WfTreeNode> tree = new ArrayList<WfTreeNode>();;
			WorkTypeComInfo wfTypeComInfo = new WorkTypeComInfo();
			int fromAdvancedMenu = Util.getIntValue(request.getParameter("fromadvancedmenu"),0);
			String selectedContent = Util.null2String(request.getParameter("selectedContent"));
		    String menuType = Util.null2String(request.getParameter("menuType"));
		    int infoId = Util.getIntValue(request.getParameter("infoId"),0);
		    if(selectedContent!=null && selectedContent.startsWith("key_")){
				String menuid = selectedContent.substring(4);
				RecordSet.executeSql("select * from menuResourceNode where contentindex = '"+menuid+"'");
				selectedContent = "";
				while(RecordSet.next()){
					String keyVal = RecordSet.getString(2);
					selectedContent += keyVal +"|";
				}
				if(selectedContent.indexOf("|")!=-1)
					selectedContent = selectedContent.substring(0,selectedContent.length()-1);
			}
			String selectedworkflow = "";
		    LeftMenuInfoHandler infoHandler = new LeftMenuInfoHandler();
		    LeftMenuInfo info = infoHandler.getLeftMenuInfo(infoId);
		    if(info!=null){
		    	selectedworkflow = info.getSelectedContent();
		    }
		    if(!"".equals(selectedContent))
		    {
		    	selectedworkflow = selectedContent;
		    }
		    selectedworkflow+="|";
			
//			String fromhp = Util.null2String((String) session.getAttribute("fromhp"));//来自门户
//			String isall = Util.null2String((String) session.getAttribute("isall"));//来自门户所有流程
	
			WfAdvanceSearchUtil wfd = new WfAdvanceSearchUtil(request, RecordSet);
			String offical = Util.null2String(request.getParameter("offical"));
			int officalType = Util.getIntValue(request.getParameter("officalType"), -1);
	
			String typeid = Util.null2String(request.getParameter("typeid"));//流程类型id
			String workflowid = Util.null2String(request.getParameter("workflowid"));//流程id
			String nodetype = Util.null2String(request.getParameter("nodetype"));//节点类型
			String createrid = Util.null2String(request.getParameter("createrid"));//创建人id
			String requestlevel = Util.null2String(request.getParameter("requestlevel"));//紧急程度
			
			String workcode = Util.null2String(request.getParameter("workcode"));//编号
			String requestname = Util.fromScreen2(request.getParameter("requestname"), user.getLanguage());
			if (!"".equals(SearchClause.getRequestName())) {
				requestname = SearchClause.getRequestName();
				SearchClause.setRequestName("");
			}
			requestname = requestname.trim();//标题
	
			String ownerdepartmentid = Util.null2String(request.getParameter("ownerdepartmentid"));//创建人部门
			String creatersubcompanyid = Util.null2String(request.getParameter("creatersubcompanyid"));//创建人分部id
			String unophrmid = Util.null2String(request.getParameter("unophrmid"));//未操作者id
			String docids = Util.null2String(request.getParameter("docids"));//相关文档id
			String crmids = Util.null2String(request.getParameter("crmids"));//相关客户id
			String proids = Util.null2String(request.getParameter("proids"));//相关项目id
			String hrmcreaterid = Util.null2String(request.getParameter("hrmcreaterid"));//人力资源
			String requestnamed = Util.null2String(request.getParameter("requestnamed"));//快速搜索
	
			int date2during = Util.getIntValue(Util.null2String(request.getParameter("date2during")), 0);
			int isdeleted = Util.getIntValue(request.getParameter("isdeleted"), 0);//流程状态
			int wfstatu = Util.getIntValue(request.getParameter("wfstatu"), 0);//处理状态
			int archivestyle = Util.getIntValue(request.getParameter("archivestyle"), 0);//归档状态
			String branchid = Util.null2String((String) session.getAttribute("branchid"));
	
			String newsql = "";
			String newsqlos = "";
			//WFSearchs.jsp页面条件
			String viewtype = Util.null2String(request.getParameter("viewtype"));//0：未读  -1：反馈
			if (!viewtype.equals("")) {//未读 or 反馈
				newsql += " and t2.viewtype='" + viewtype + "'";
				newsqlos += " and viewtype=" + viewtype;
			}
			if (!typeid.equals("") && !typeid.equals("0")) {//类别
				newsql += " and t2.workflowtype='" + typeid + "'";
				newsqlos += " and sysid=" + typeid;
			}
			if (!workflowid.equals("") && !workflowid.equals("0")) {
				if (workflowid.indexOf("-") != -1) {
					newsql += " and t1.workflowid in(" + workflowid + ")";
				} else {
					newsql += " and t1.workflowid in(" + WorkflowVersion.getAllVersionStringByWFIDs(workflowid) + ")";
				}
				newsqlos += " and workflowid=" + workflowid;
			}
	
			if (date2during > 0 && date2during < 37) {
				newsql += WorkflowComInfo.getDateDuringSql(date2during);
				newsqlos += WorkflowComInfo.getDateDuringSql(date2during);
			}
	
			String dbNames = new RecordSet().getDBType();
			boolean isoracle = "oracle".equals(dbNames.toLowerCase());
			if (!requestname.equals("")) {
				if ((requestname.indexOf(" ") == -1 && requestname.indexOf("+") == -1)
					|| (requestname.indexOf(" ") != -1 && requestname.indexOf("+") != -1)) {
					if (isoracle) {
						newsql += " and instr(t1.requestnamenew, '" + requestname + "') > 0 ";
						newsqlos += " and instr(requestname, '" + requestname + "') > 0 ";
					} else {
						newsql += " and t1.requestnamenew like '%" + requestname + "%'";
						newsqlos += " and requestname like '%" + requestname + "%'";
					}
				} else if (requestname.indexOf(" ") != -1 && requestname.indexOf("+") == -1) {
					String orArray[] = Util.TokenizerString2(requestname, " ");
					if (orArray.length > 0) {
						newsql += " and ( ";
						newsqlos += " and ( ";
					}
					for (int i = 0; i < orArray.length; i++) {
						if (isoracle) {
							newsql += " and instr(t1.requestnamenew, '" + orArray[i] + "') > 0 ";
						} else {
							newsql += " t1.requestnamenew like '%" + orArray[i] + "%'";
						}
						newsqlos += " requestname like '%" + orArray[i] + "%'";
						if (i + 1 < orArray.length) {
							newsql += " or ";
							newsqlos += " or ";
						}
					}
					if (orArray.length > 0) {
						newsql += " ) ";
						newsqlos += " ) ";
					}
				} else if (requestname.indexOf(" ") == -1 && requestname.indexOf("+") != -1) {
					String andArray[] = Util.TokenizerString2(requestname, "+");
					for (int i = 0; i < andArray.length; i++) {
						if (isoracle) {
							newsql += " and instr(t1.requestnamenew, '" + andArray[i] + "') > 0 ";
						} else {
							newsql += " and t1.requestnamenew like '%" + andArray[i] + "%'";
						}
						newsqlos += " and requestname like '%" + andArray[i] + "%'";
					}
				}
			}
	
			if (!requestnamed.equals("")) {
				if (isoracle) {
					newsql += " and instr(t1.requestnamenew, '" + requestnamed + "') > 0 ";
				} else {
					newsql += " and t1.requestnamenew like '%" + requestnamed + "%'";
				}
				newsqlos += " and requestname like '%" + requestname + "%'";
			}
			if (!"".equals(workcode)) {
				newsql += " AND t1.requestmark LIKE '%" + workcode + "%' ";
				newsqlos += " and 1=2 ";
			}
			if (!requestlevel.equals("")) {
				newsql += " and t1.requestlevel=" + requestlevel;
				newsqlos += " and 1=2 ";
			}
			//创建人条件
			newsql += wfd.handleCreaterCondition("t1.creater");
			if (!createrid.equals("")) {
				newsqlos += "  and creatorid = " + createrid + "";
			}
			//创建人部门
			if (!ownerdepartmentid.equals("")) {
				newsql += " and t1.creatertype= '0' " + wfd.handleDepCondition("t1.creater");
				newsqlos += " and " + wfd.handleDepCondition("creater");
			}
			//创建人分部
			if (!creatersubcompanyid.equals("")) {
				newsql += " and t1.creatertype= '0' " + wfd.handleSubComCondition("t1.creater");
				newsqlos += " and " + wfd.handleSubComCondition("creater");
			}
			//创建日期
			newsql += wfd.handCreateDateCondition("t1.createdate");
			newsqlos += wfd.handCreateDateCondition("createdate");
			//接受日期
			newsql += wfd.handRecieveDateCondition("t2.receivedate");
			newsqlos += wfd.handRecieveDateCondition("receivedate");
			//流程状态
			if (isdeleted == 0) {
				isdeleted = 1;
			} else if (isdeleted == 1) {
				isdeleted = 0;
			}
			if (isdeleted != 1) {
				if (isdeleted == 0) {
					newsql += " and t1.workflowid in (select id from workflow_base where isvalid not in ('1', '2', '3'))";
					newsqlos += " and workflowid in (select workflowid from ofs_workflow where cancel!=0) ";
				} else if (isdeleted == 2) { //全部
					newsql += " and t1.workflowid in (select id from workflow_base where isvalid<>'2')";
					//newsqlos += " and workflowid in (select workflowid from ofs_workflow where cancel=0) ";
				}
			} else {
				newsqlos += " and workflowid in (select workflowid from ofs_workflow where cancel=0) ";
			}
			//处理状态
			if (wfstatu != 0) {
				if (wfstatu == 1) {
					newsql += " AND t2.isremark in( '0','1','5','8','9','7')";
					newsqlos += " and isremark in ('0') ";
				} else {
					newsql += " AND t2.isremark in('2','4')";
					newsqlos += " and isremark in ('2','4') ";
				}
			}
			//归档处理
			if (archivestyle != 0) {
				if (archivestyle == 1) {
					newsql += " and t2.iscomplete<>1";//未归档
					newsqlos += " and iscomplete<>1";
				} else {
					newsql += " and t2.iscomplete=1";//归档
					newsqlos += " and iscomplete=1";
				}
			}
			//节点类型
			if (!nodetype.equals("")) {
				newsql += " and t1.currentnodetype='" + nodetype + "'";
				newsqlos += " and 1=5 ";
			}
			//未操作人
			newsql += wfd.handleUnOpCondition("t2.REQUESTID");
			if (!unophrmid.equals("")) {
				newsqlos += " and requestid in (select requestid from ofs_todo_data where userid=" + unophrmid + " and isremark=0 )";
			}
			//相关文档
			newsql += wfd.handleDocCondition("t1.docids");
			//相关人力资源
			newsql += wfd.handleHrmCondition("t1.hrmids");
			//相关客户
			newsql += wfd.handleCrmCondition("t1.crmids");
			//相关项目
			newsql += wfd.handleProsCondition("t1.prjids");
	
			if (!docids.equals("") || !hrmcreaterid.equals("") || !crmids.equals("") || !proids.equals("")) {//相关客户等不查询
				newsqlos += " and 1=2 ";
			}
	
			String CurrentUser = Util.null2String(request.getParameter("resourceid"));
			if (CurrentUser.equals(""))
				CurrentUser = "" + user.getUID();
			String userID = String.valueOf(user.getUID());
			String belongtoshow = "";
			RecordSet.executeSql("select * from HrmUserSetting where resourceId = " + userID);
			if (RecordSet.next()) {
				belongtoshow = RecordSet.getString("belongtoshow");
			}
			String userIDAll = String.valueOf(user.getUID());
			String Belongtoids = user.getBelongtoids();
			if (!"".equals(Belongtoids))
				userIDAll = userID + "," + Belongtoids;
	
			int usertype = "2".equals(user.getLogintype()) ? 1 : 0;
			boolean superior = false; //是否为被查看者上级或者本身
			if (userID.equals(CurrentUser)) {
				superior = true;
			} else {
				RecordSet.executeSql("SELECT * FROM HrmResource WHERE ID = " + CurrentUser + " AND managerStr LIKE '%" + userID + "%'");
				if (RecordSet.next())
					superior = true;
			}
	
			//获取流程共享信息
			String currentid = wfShareAuthorization.getCurrentoperatorIDByUser(user);
			String sqlwhere = "";
			String sqlwhereuserids = "1".equals(belongtoshow) ? userIDAll : CurrentUser;
			//门户条件--begin
			String jsonstr = Util.null2String(request.getParameter("jsonstr"));//来自门户所有条件
			String fromhp = "";//来自门户
			String isall = "";//来自门户所有流程
			String hpsqlwhere = getHpSqlWhere(jsonstr,sqlwhereuserids,usertype,user);//门户条件拼接
			if(!"".equals(hpsqlwhere.trim())){
				fromhp = "1";
				isall =hpsqlwhere.split("~splite~")[1];
				hpsqlwhere = hpsqlwhere.split("~splite~")[0];
			}
			//门户条件--end
			//是否有共享的流程
			if (!"".equals(currentid) && !"1".equals(fromhp)) {
				sqlwhere = " where (t1.deleted <> 1 or t1.deleted is null or t1.deleted='') and t1.requestid = t2.requestid AND t2.islasttimes=1 ";
				sqlwhere += " and (( t2.userid in (" + sqlwhereuserids + " ) and t2.usertype=" + usertype;
			} else {
				if ("1".equals(fromhp) && "1".equals(isall)) {
					sqlwhere = " where (t1.deleted <> 1 or t1.deleted is null or t1.deleted='') and t1.requestid = t2.requestid AND t2.islasttimes=1 ";
				} else {
				sqlwhere = " where (t1.deleted <> 1 or t1.deleted is null or t1.deleted='') and t1.requestid = t2.requestid and t2.userid in ("
					+ sqlwhereuserids + " ) and t2.usertype=" + usertype + " AND t2.islasttimes=1 ";
				}
			}
			
			if (RecordSet.getDBType().equals("oracle")) {
				sqlwhere += " and (nvl(t1.currentstatus,-1) = -1 or (nvl(t1.currentstatus,-1)=0 and t1.creater in (" + sqlwhereuserids + "))) ";
			} else {
				sqlwhere += " and (isnull(t1.currentstatus,-1) = -1 or (isnull(t1.currentstatus,-1)=0 and t1.creater in (" + sqlwhereuserids + "))) ";
			}
	
			//来自于借款流程查看相关还款流程按钮的查询
			boolean _isFromShowRelatedProcess = Util.getIntValue(request.getParameter("_isFromShowRelatedProcess"), -1) == 1;
			if (_isFromShowRelatedProcess) {
				int _borrowRequestid = Util.getIntValue(request.getParameter("_borrowRequestid"), -1);
				int _borrowDetailRecordId = Util.getIntValue(request.getParameter("_borrowDetailRecordId"), -1);
				if ("".equals(sqlwhere)) {
					sqlwhere += " where ";
				} else {
					sqlwhere += " and ";
				}
				if (_borrowDetailRecordId > 0 && _borrowRequestid > 0) {
					sqlwhere += " exists ( \n" + " select 1 \n" + " from FnaBorrowInfo fbi \n"
						+ " where fbi.borrowDirection = -1  \n" + " and fbi.borrowRequestIdDtlId = "
						+ _borrowDetailRecordId + " and fbi.borrowRequestId = " + _borrowRequestid + " \n"
						+ " and fbi.requestid = t1.requestid \n" + " ) \n";
				} else {
					sqlwhere += " 1=2 ";
				}
			}
			sqlwhere += " " + newsql;
	
			String fromSql = " from workflow_requestbase t1,workflow_currentoperator t2 ";//xxxxx
			String sqlWhere = sqlwhere;
			if (sqlWhere.indexOf("in (select id from workflow_base where isvalid") < 0) {
				sqlWhere += " and t1.workflowid in (select id from workflow_base where (isvalid='1' or isvalid='3') )";
			}
			
			if (!docids.equals("")) {
				fromSql = fromSql + ",workflow_form t4 ";
				sqlWhere = sqlWhere + " and t1.requestid=t4.requestid ";
			}
			if (!superior) {
				sqlWhere += " AND EXISTS (SELECT 1 FROM workFlow_CurrentOperator workFlowCurrentOperator WHERE t2.workflowid = workFlowCurrentOperator.workflowid AND t2.requestid = workFlowCurrentOperator.requestid AND workFlowCurrentOperator.userid in ("
					+ sqlwhereuserids + ") and workFlowCurrentOperator.usertype = " + usertype + ") ";
			}
			if (!branchid.equals("")) {
				sqlWhere += " AND t1.creater in (select id from hrmresource where subcompanyid1=" + branchid + ")  ";
			}
			if (offical.equals("1")) {//发文/收文/签报
				if (officalType == 1) {
					sqlWhere += (" and t1.workflowid in (select id from workflow_base where isWorkflowDoc=1 and officalType in (1,3) and (isvalid=1 or isvalid=3))");
				} else if (officalType == 2) {
					sqlWhere += (" and t1.workflowid in (select id from workflow_base where isWorkflowDoc=1 and officalType=2 and (isvalid=1 or isvalid=3))");
				}
			}
			
			StringBuffer sqlsb = new StringBuffer();
	        sqlsb.append("select workflowtype, ");
	        sqlsb.append("     workflowid ");
	        sqlsb.append("  from workflow_currentoperator ");

	        if(!"".equals(currentid) && !"1".equals(fromhp)){
	            sqlsb.append(" where (");
	        }else{
	            sqlsb.append(" where ");
	        }
	        
	        if (!("1".equals(fromhp) && "1".equals(isall))) {
		        if("1".equals(belongtoshow)){
		        	sqlsb.append("  userid in (").append(userIDAll);
		        }else{
		        	sqlsb.append("  userid in (").append(userID);
		        }
		        sqlsb.append(")   and usertype = ").append(usertype).append(" and ");
	        }
	        sqlsb.append("	 exists ");
	        sqlsb.append("	  (select 1 ");
	        sqlsb.append("	           from workflow_requestbase c ");
	        sqlsb.append("	          where islasttimes=1 AND (c.deleted <> 1 or c.deleted is null or c.deleted='') and c.workflowid = workflow_currentoperator.workflowid ");
	        sqlsb.append(" and workflowid in (select id from workflow_base where (isvalid=1 or isvalid=3) ) ");
	        sqlsb.append("	            and c.requestid = workflow_currentoperator.requestid ");
	        if(RecordSet.getDBType().equals("oracle"))
	        {
	            sqlsb.append(" and (nvl(c.currentstatus,-1) = -1 or (nvl(c.currentstatus,-1)=0 and c.creater="+user.getUID()+")) ");
	        }
	        else
	        {
	            sqlsb.append(" and (isnull(c.currentstatus,-1) = -1 or (isnull(c.currentstatus,-1)=0 and c.creater="+user.getUID()+")) ");
	        }
	        sqlsb.append(")");
	        if(offical.equals("1")){
	            if(officalType==1){
	                sqlsb.append(" and workflowid in (select id from workflow_base where isWorkflowDoc=1 and officalType in (1,3) and (isvalid=1 or isvalid=3))");
	            }else if(officalType==2){
	                sqlsb.append(" and workflowid in (select id from workflow_base where isWorkflowDoc=1 and officalType=2 and (isvalid=1 or isvalid=3))");
	            }
	        }

	        if (hpsqlwhere != null && !hpsqlwhere.trim().equals("")) {
				if (hpsqlwhere.trim().startsWith("and")) {
					sqlsb.append( "and exists (select 1 from workflow_requestbase t1,workflow_currentoperator t2 where 1=1 "+hpsqlwhere);
					sqlsb.append("	            and t2.requestid = workflow_currentoperator.requestid ) ");
				} else {
					sqlsb.append( "and exists (select 1 from workflow_requestbase t1,workflow_currentoperator t2 where 1=1 and "+hpsqlwhere);
					sqlsb.append("	            and t2.requestid = workflow_currentoperator.requestid ) ");
				}
			}
	        
	        if(!"".equals(currentid) && !"1".equals(fromhp)){
	            sqlsb.append(") or ( id in ("+currentid+") )");
	        }
	        sqlsb.append(" group by workflowtype, workflowid ");
	        sqlsb.append(" order by workflowtype, workflowid ");
	        RecordSet.executeSql(sqlsb.toString());
	        List<String> wftypes = new ArrayList<String>();
			List<String> workflows = new ArrayList<String>();
	        while(RecordSet.next()){
	            String theworkflowid = Util.null2String(RecordSet.getString("workflowid")) ;
	            String theworkflowtype = Util.null2String(RecordSet.getString("workflowtype")) ;
//	            int theworkflowcount = Util.getIntValue(RecordSet.getString("workflowcount"),0) ;
//	            int viewtype = Util.getIntValue(RecordSet.getString("viewtype"),2) ;
//	            int isremark = Util.getIntValue(RecordSet.getString("isremark"),0) ;
	            theworkflowid = WorkflowVersion.getActiveVersionWFID(theworkflowid);
	            
	            if(WorkflowComInfo.getIsValid(theworkflowid).equals("1")){
	                /* added by wdl 2006-06-14 left menu advanced menu */
	                if(selectedworkflow.indexOf("T"+theworkflowtype+"|")==-1 && fromAdvancedMenu==1) continue;
	                if(selectedworkflow.indexOf("W"+theworkflowid+"|")==-1 && fromAdvancedMenu==1) continue;
	                /* added end */

	                List<WfTreeNode> childs = null;
	                int wftindex = wftypes.indexOf(theworkflowtype);
	                if(wftindex == -1){
	                	wftypes.add(theworkflowtype);
	                	childs = new ArrayList<WfTreeNode>();
	    				WfTreeNode typenode = new WfTreeNode();
	    				typenode.setDomid("type_"+theworkflowtype);
	    				typenode.setKey(theworkflowtype);
	    				typenode.setName(convertChar(wfTypeComInfo.getWorkTypename(theworkflowtype), user.getLanguage()));
	    				typenode.setIsopen(true);
	    				typenode.setHaschild(true);
	    				typenode.setChilds(childs);
	    				tree.add(typenode);
	                }else{
	                	WfTreeNode typenode = tree.get(wftindex);
	    				childs = typenode.getChilds();
	                }
	                
	                int wfindex = workflows.indexOf(theworkflowid) ;
	                if(wfindex == -1) {
	                	workflows.add(theworkflowid);
	    				WfTreeNode wfnode = new WfTreeNode();
	    				wfnode.setDomid("wf_"+theworkflowid);
	    				wfnode.setKey(theworkflowid);
	    				wfnode.setName(convertChar(WorkflowComInfo.getWorkflowname(theworkflowid), user.getLanguage()));
	    				childs.add(wfnode);
	                }
	            }
	        }
	        retmap.put("treedata", tree);
		}catch(Exception e){
			retmap.put("api_status", false);
			retmap.put("api_errormsg", e.getMessage());
			e.printStackTrace();
		}
		return JSONObject.toJSONString(retmap);
	}
	
	private String convertChar(String str, int languageid){
		return Util.toScreenForJs(Util.toScreen(str, languageid));
	}
	
	private String getHpSqlWhere(String jsonstr,String userid,int usertype,User user){
		String inSelectedStr="";
	  	String inSelectedStrOs=""; //统一待办
	  	String whereclause="";
	  	String whereclauseOs="";   //统一待办
		String viewType = "";//流程类型
		String showCopy = "";//待办中是否显示抄送事宜 1、显示，0、不显示
		int completeflag = 0;//针对我的请求    0：所有,1：未完成,2：已完成 
		boolean isExclude = false;//是否排除上述选择    0：不排除,1：排除
		String perpage = "";//查询的个数，默认为5
		String fieldColumnList = "";//返回的列
		String typeids = "";//流程类型id集合
		String flowids = "";//流程id集合
		String nodeids = "";//流程节点id集合
		String isall = "0";
		WFShareAuthorization wfShareAuthorization = new WFShareAuthorization();
		List<String> typeidslist = null;//流程类型id集合
		List<String> flowidslist = null;//流程id集合
		List<String> nodeidslist = null;//流程节点id集合
		int typeCount = 0;//流程类型计数
		int flowCount = 0;//流程计数
		int nodeCount = 0;//流程节点计数
		String strsqlClause = "";
		if(!"".equals(jsonstr)){
			JSONObject object = JSON.parseObject(jsonstr);
			//List<Map> datas = JSONObject.parseArray(jsonstr, Map.class);
//			fromhp = Util.null2String((String) object.get("fromhp"));
			//isall = Util.null2String((String) object.get("isall"));
			viewType = Util.null2String((String) object.get("viewType"));
			isExclude = object.getInteger("isExclude")==0?false:true;
			perpage = Util.null2String((String) object.get("perpage"));
			fieldColumnList = Util.null2String((String) object.get("fieldColumnList"));
			typeidslist = (List)(JSONArray) object.get("typeids");
			flowidslist = (List)(JSONArray) object.get("flowids");
			nodeidslist = (List)(JSONArray) object.get("nodeids");
			typeCount = object.getInteger("typeCount");
//			typeCount = Util.getIntValue((String) object.get("typeCount"), 0);
			flowCount = object.getInteger("flowCount");
			nodeCount = object.getInteger("nodeCount");
//			flowCount = Util.getIntValue((String) object.get("flowCount"), 0);
//			nodeCount = Util.getIntValue((String) object.get("nodeCount"), 0);
			String inornot = "";
			if(isExclude){
				inornot = "not in";
			}else{
				inornot = "in";
			}
			
			if(typeidslist != null){
				for(String s: typeidslist){
					if("".equals(typeids)){
						typeids = s;
					}else{
						typeids += ","+s;
					}
				}
			}
			if(flowidslist != null){
				for(String s: flowidslist){
					if("".equals(flowids)){
						flowids = s;
					}else{
						flowids += ","+s;
					}
				}
			}
			if(nodeidslist != null){
				for(String s: nodeidslist){
					if("".equals(nodeids)){
						nodeids = s;
					}else{
						nodeids += ","+s;
					}
				}
			}
			
			if("1".equals(viewType)){ //待办事宜
				showCopy = Util.null2String((String) object.get("showCopy"));
				if(flowCount>0){
					if(nodeCount>0){
						strsqlClause = Util.getSubINClause(nodeids, "t2.nodeid", inornot);
						inSelectedStr += " and "+strsqlClause;
					}
					strsqlClause = Util.getSubINClause(flowids, "t1.workflowid", inornot);
					inSelectedStr += " and "+strsqlClause;
//					inSelectedStrOs += strsqlClause;
				}
				if(typeCount > 0){
					whereclause += inSelectedStr+" and t2.workflowtype "+inornot+" ("+typeids+") ";
				}
//				whereclauseOs += inSelectedStrOs;
				if("1".equals(showCopy)){//显示抄送事宜
					if("".equals(whereclause)){
						whereclause +=" (t2.isremark in('1','5','8','9','7') or t2.isremark='0' and (takisremark is null or takisremark=0)) and t2.islasttimes=1";
					}else{
						whereclause +=" and (t2.isremark in('1','5','8','9','7') or t2.isremark='0' and (takisremark is null or takisremark=0)) and t2.islasttimes=1";
					}
				}else{//不显示抄送事宜
					if("".equals(whereclause)){
						whereclause +=" (t2.isremark in('1','5','7') or t2.isremark='0' and (takisremark is null or takisremark=0)) and t2.islasttimes=1";
					}else{
						whereclause +=" and (t2.isremark in('1','5','7') or t2.isremark='0' and (takisremark is null or takisremark=0)) and t2.islasttimes=1";
					}
				}
				//if(wfviewtype!=-1)		whereclause+=" and t2.viewtype="+wfviewtype;
				
				whereclause +=" and t2.workflowid in (select id from workflow_base where (isvalid='1' or isvalid='3') )";
		        
			}else if("2".equals(viewType)){ //已办事宜
				if(flowCount>0){
					if(nodeCount>0){
						strsqlClause = Util.getSubINClause(nodeids, "t2.nodeid", inornot);
						inSelectedStr += " and "+strsqlClause;
					}
					strsqlClause = Util.getSubINClause(flowids, "t1.workflowid", inornot);
					inSelectedStr += " and "+strsqlClause;
//					inSelectedStrOs += strsqlClause;
				}
				if(typeCount > 0){
					whereclause += inSelectedStr+" and t2.workflowtype "+inornot+" ("+typeids+") ";
				}
				
				if("(".equals(whereclause)){
					whereclause += " t2.isremark ='2' or t2.isremark='0' and takisremark=-2)  and t2.iscomplete=0 and t2.islasttimes=1 ";
				}else{
					whereclause += " and (t2.isremark ='2' or t2.isremark='0' and takisremark=-2)  and t2.iscomplete=0 and t2.islasttimes=1 ";
				}
//				if(wfviewtype!=-1)		whereclause+=" and t2.viewtype="+wfviewtype;
				
				//whereclause+="  and t2.workflowtype!=1 ";
				whereclause +=" and t2.workflowid in (select id from workflow_base where (isvalid='1' or isvalid='3') ) ";
			}else if("3".equals(viewType)){ //办结事宜
				if(flowCount>0){
					strsqlClause = Util.getSubINClause(flowids, "t1.workflowid", inornot);
					inSelectedStr += " and "+strsqlClause;
				}
				if(typeCount > 0){
					whereclause += inSelectedStr+" and t2.workflowtype "+inornot+" ("+typeids+") ";
				}
				
				if("".equals(whereclause)){
					whereclause = "t1.currentnodetype = '3' and (t2.isremark in('2','4') or t2.isremark='0' and takisremark=-2) and iscomplete=1 and islasttimes=1 ";
				}else{
					whereclause += " and t1.currentnodetype = '3' and (t2.isremark in('2','4') or t2.isremark='0' and takisremark=-2) and iscomplete=1 and islasttimes=1 ";
				}
//				if(wfviewtype!=-1)		whereclause+=" and t2.viewtype="+wfviewtype;
				
				//whereclause+="  and t2.workflowtype!=1 ";
				whereclause +=" and t2.workflowid in (select id from workflow_base where (isvalid='1' or isvalid='3') )";
		        
		   	}else if("4".equals(viewType)){ //我的请求
				completeflag = object.getInteger("completeflag");
				if(flowCount>0){
					if(nodeCount>0){
						strsqlClause = Util.getSubINClause(nodeids, "t2.nodeid", inornot);
						inSelectedStr += " and "+strsqlClause;
					}
					strsqlClause = Util.getSubINClause(flowids, "t1.workflowid", inornot);
					inSelectedStr += " and "+strsqlClause;
//					inSelectedStrOs += strsqlClause;
				}
				if(typeCount > 0){
					whereclause += inSelectedStr+" and t2.workflowtype "+inornot+" ("+typeids+") ";
				}
				
				if("".equals(whereclause)){
					whereclause +=" t1.creater in ( "+userid+") and t1.creater=t2.userid  and t1.creatertype = " + usertype;
				}else{
					whereclause +=" and t1.creater in  ("+userid+") and t1.creater=t2.userid  and t1.creatertype = " + usertype;
				}
				whereclause += " and (t1.deleted=0 or t1.deleted is null) and t2.islasttimes=1 ";
//				if(wfviewtype!=-1)		whereclause+=" and t2.viewtype="+wfviewtype;
				
				//whereclause+="  and t2.workflowtype!=1 ";
				if(completeflag==1){
				    whereclause += " and t1.currentnodetype <> '3' ";
				}else if(completeflag==2){
				    whereclause += " and t1.currentnodetype = '3' ";
				}
				//System.out.println("whereclause=="+whereclause);
				whereclause +=" and t2.workflowid in (select id from workflow_base where (isvalid='1' or isvalid='3') )";
				
			}else if("5".equals(viewType)){ //抄送事宜
				if(flowCount>0){
					strsqlClause = Util.getSubINClause(flowids, "t1.workflowid", inornot);
					inSelectedStr += " and "+strsqlClause;
				}
				if(typeCount > 0){
					whereclause += inSelectedStr+" and t2.workflowtype "+inornot+" ("+typeids+") ";
				}
				if("".equals(whereclause)){
					whereclause +=" t2.isremark in ('8', '9','7') and t2.islasttimes=1 ";
				}else{
					whereclause +=" and t2.isremark in ('8', '9','7') and t2.islasttimes=1 ";
				}
//				if(wfviewtype!=-1)		whereclause+=" and t2.viewtype="+wfviewtype;
				//whereclause+="  and t2.workflowtype!=1 ";
			    whereclause +=" and t2.workflowid in (select id from workflow_base where (isvalid='1' or isvalid='3') )";
				
			}else if("6".equals(viewType)){ //督办事宜
				if(flowCount>0){
					strsqlClause = Util.getSubINClause(flowids, "t1.workflowid", inornot);
					inSelectedStr += " and "+strsqlClause;
				}
				whereclause += inSelectedStr;
//				if(wfviewtype!=-1)		whereclause+=" and t2.viewtype="+wfviewtype;
				whereclause +=" and t1.workflowid in (select id from workflow_base where (isvalid='1' or isvalid='3') )";
				
			}else if("7".equals(viewType)){ //超时事宜
				if(flowCount>0){
					strsqlClause = Util.getSubINClause(flowids, "t1.workflowid", inornot);
					inSelectedStr += " and "+strsqlClause;
				}
				if("".equals(whereclause)){
					whereclause +=" ((t2.isremark='0' and (t2.isprocessed='2' or t2.isprocessed='3'))  or t2.isremark='5') ";
			        whereclause += " and t1.currentnodetype <> 3 ";
				}else{
					whereclause +=" and ((t2.isremark='0' and (t2.isprocessed='2' or t2.isprocessed='3'))  or t2.isremark='5') ";
			        whereclause += " and t1.currentnodetype <> 3 ";
				}
//				if(wfviewtype!=-1)		whereclause+=" and t2.viewtype="+wfviewtype;
				whereclause +=" and t2.workflowid in (select id from workflow_base where (isvalid='1' or isvalid='3') )";
				
			}else if("8".equals(viewType)){ //反馈事宜
				if(flowCount>0){
					strsqlClause = Util.getSubINClause(flowids, "t1.workflowid", inornot);
					inSelectedStr += " and "+strsqlClause;
				}
				if(!"".equals(whereclause)){
					whereclause +=" and ";
				}
				whereclause += " t2.needwfback=1 and viewtype=-1 and isremark in('2','4') and t1.requestid = t2.requestid and userid  in ("+userid+") and usertype="+usertype;		
				whereclause += " and t2.id in(select max(id) from workflow_currentoperator where needwfback = 1 and viewtype = -1 and isremark in ('2', '4') and userid in ("+userid+") and usertype="+usertype+" group by requestid)";
				whereclause +=" and t2.workflowid in (select id from workflow_base where (isvalid='1' or isvalid='3') )";
			//所有事宜	
			} else if("10".equals(viewType)){
				if(flowCount>0){
					strsqlClause = Util.getSubINClause(flowids, "t1.workflowid", inornot);
					whereclause += " and "+strsqlClause;
				}
				
				//获取流程共享信息
				String currentid = "";
				try {
					currentid = wfShareAuthorization.getCurrentoperatorIDByUser(user);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(!"".equals(currentid)){
					whereclause +=" and t1.requestid = t2.requestid  and   t2.islasttimes=1 " ;
					whereclause += "and  (( t2.userid in( "+userid+") and t2.usertype="+usertype;
				}else{
					whereclause +=" and t1.requestid = t2.requestid  and   t2.islasttimes=1 and  t2.userid in( "+userid+") and t2.usertype="+usertype;
				}
				//是否有共享的流程
				if(!"".equals(currentid)){
					whereclause += " ) or (t2.id in ("+currentid+") )) ";
				}
				
				whereclause += " and (t1.deleted=0 or t1.deleted is null) ";
				whereclause +=" and t2.workflowid in (select id from workflow_base where (isvalid='1' or isvalid='3') )";
				isall = "1";
//				whereclauseOs = " 1=2 ";
			}else{ //不是以上任何一个，跳转到安全页面
				inSelectedStr += "t1.workflowid in (0) ";
				whereclause += inSelectedStr;
			}
		}
		if(!"".equals(whereclause)){
			whereclause+="~splite~"+isall;
		}
		return whereclause;
	}
	
}