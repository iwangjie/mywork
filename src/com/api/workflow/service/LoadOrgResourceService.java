package com.api.workflow.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import com.alibaba.fastjson.JSONObject;

import weaver.general.Util;
import weaver.hrm.HrmUserVarify;
import weaver.hrm.User;
import weaver.hrm.resource.MutilResourceBrowser;
import weaver.hrm.resource.ResourceComInfo;

/**
 * 
 * @author jhy Mar 30, 2017
 * 
 */
@Path("/workflow/org")
public class LoadOrgResourceService {

	@GET
	@Path("/resource")
	public String getOrgResource(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		String types = Util.null2String(request.getParameter("types"));
		User user = HrmUserVarify.getUser(request, response);
		if ("".equals(types))
			return "";
		String[] typeArr = types.split(",");
		List<Map<String, Object>> apidatas = new ArrayList<Map<String, Object>>();
		try {
			MutilResourceBrowser mrb = new MutilResourceBrowser();
			ResourceComInfo rcomInfo = new ResourceComInfo();
			String alllevel = "1";
			String isNoAccount = "1";
			String sqlwhere = "";
			String resourceids = "";

			for (String typeInfo : typeArr) {
				String[] typeInfoArr = typeInfo.split("\\|");
				if (typeInfoArr.length > 1) {
					String type = typeInfoArr[0];
					String id = typeInfoArr[1];
					if (type.equals("subcom") || type.equals("dept") || type.equals("com")) {
						// 部门
						String nodeid = type + "_" + id;
						if (Integer.parseInt(id) < 0) {
							// 虚拟
							resourceids = mrb.getComDeptResourceVirtualIds(nodeid, alllevel, isNoAccount, user, sqlwhere);
						} else {
							resourceids = mrb.getComDeptResourceIds(nodeid, alllevel, isNoAccount, user, sqlwhere);
						}
					} else if (type.equals("group")) {// 自定义组
						resourceids = mrb.getGroupResourceIds(id, isNoAccount, user, sqlwhere);
					}

					String[] resourceidArr = Util.TokenizerString2(resourceids, ",");
					List<Map<String, Object>> users = new ArrayList<Map<String, Object>>();
					Map<String, Object> userInfo = null;
					for (String resourceid : resourceidArr) {
						if ("".equals(Util.null2String(resourceid)))
							continue;
						userInfo = new HashMap<String, Object>();
						userInfo.put("id", resourceid);
						userInfo.put("lastname", rcomInfo.getLastname(resourceid));
						userInfo.put("jobtitlename", MutilResourceBrowser.getJobTitlesname(resourceid));
						userInfo.put("icon", rcomInfo.getMessagerUrls(resourceid));
						userInfo.put("type", "resource");
						users.add(userInfo);
					}

					Map<String, Object> typeresult = new HashMap<String, Object>();
					typeresult.put("id", id);
					typeresult.put("type", type);
					typeresult.put("users", users);
					typeresult.put("nodeid", type + "_" + id + "x");
					apidatas.add(typeresult);
				} else {
					if ("".equals(Util.null2String(typeInfo)))
						continue;
					Map<String, Object> userInfo = new HashMap<String, Object>();
					userInfo.put("id", typeInfo);
					userInfo.put("lastname", rcomInfo.getLastname(typeInfo));
					userInfo.put("jobtitlename", MutilResourceBrowser.getJobTitlesname(typeInfo));
					userInfo.put("icon", rcomInfo.getMessagerUrls(typeInfo));
					userInfo.put("type", "resource");
					userInfo.put("nodeid", "resource_" + typeInfo + "x");
					apidatas.add(userInfo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSONObject.toJSONString(apidatas);
	}
}
