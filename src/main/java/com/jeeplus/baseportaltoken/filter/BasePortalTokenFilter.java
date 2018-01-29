package com.jeeplus.baseportaltoken.filter;

import java.io.Console;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeeplus.common.mapper.JsonMapper;
import com.jeeplus.common.utils.CookieUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.sys.entity.BasePortalAPIReturn;
import com.jeeplus.modules.sys.security.RestUtil;
import com.jeeplus.modules.sys.web.LoginController;



public class BasePortalTokenFilter extends UserFilter {

	
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		// TODO Auto-generated method stub
		/*try{
			Subject subject = SecurityUtils.getSubject(); 
			
			Session session = subject.getSession(); 
			String token =  session.getAttribute(LoginController.SESSION_TOKEN).toString();
			if(!StringUtils.isNotBlank(token)) return false;
			String resultString = RestUtil.SendGet("/api/token", "", token);
			BasePortalAPIReturn returnObject = (BasePortalAPIReturn) JsonMapper.getInstance().fromJsonString(resultString, BasePortalAPIReturn.class);
			if(returnObject==null || !returnObject.getReturnCode().equals("0"))
			{	
					return false;
			}
			
			
			return super.isAccessAllowed(request, response, mappedValue);
			
		}catch(Exception e)
		{
			return false;
		}*/
		return super.isAccessAllowed(request, response, mappedValue);
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return super.onAccessDenied(request, response);
	}


}
