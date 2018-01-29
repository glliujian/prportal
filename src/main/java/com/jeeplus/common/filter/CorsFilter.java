package com.jeeplus.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeeplus.common.mapper.JsonMapper;
import com.jeeplus.common.utils.CookieUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.sys.entity.BasePortalAPIReturn;
import com.jeeplus.modules.sys.security.RestUtil;

public class CorsFilter implements Filter {
@Override
public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {  
    HttpServletResponse response = (HttpServletResponse) res;  
    HttpServletRequest httpServletRequest = (HttpServletRequest) req;    
    if(httpServletRequest.getRemoteHost().equals("localhost")){
    	System.out.println(httpServletRequest.getRemoteHost());
    };    
    //System.out.println(httpServletRequest.getRemoteHost());
    String url = httpServletRequest.getRequestURI();
    if (url.contains("/api/")){ // 接口类才可跨域
	    response.setHeader("Access-Control-Allow-Origin", "*");  
	    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");  
	    response.setHeader("Access-Control-Max-Age", "3600");  
	    //response.setHeader("Access-Control-Allow-Credentials", "true");      
	    response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");  
    }
    
    chain.doFilter(req, res);  
}   

public void init(FilterConfig filterConfig) {}
public void destroy() {}
}

	
   
