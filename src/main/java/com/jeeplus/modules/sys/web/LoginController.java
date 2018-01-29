/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.web;

import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.shiro.session.Session;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.task.Task;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.google.common.collect.Maps;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.language.MessageResource;
import com.jeeplus.common.mapper.JsonMapper;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.security.shiro.session.SessionDAO;
import com.jeeplus.common.servlet.ValidateCodeServlet;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.CookieUtils;
import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.act.entity.Act;
import com.jeeplus.modules.act.service.ActTaskService;
import com.jeeplus.modules.iim.entity.MailBox;
import com.jeeplus.modules.iim.entity.MailPage;
import com.jeeplus.modules.iim.service.MailBoxService;
import com.jeeplus.modules.oa.entity.OaNotify;
import com.jeeplus.modules.oa.entity.Pr;
import com.jeeplus.modules.oa.service.OaNotifyService;
import com.jeeplus.modules.oa.service.PrService;
import com.jeeplus.modules.sys.entity.BasePortalAPIReturn;
import com.jeeplus.modules.sys.entity.Menu;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.security.FormAuthenticationFilter;
import com.jeeplus.modules.sys.security.RestUtil;
import com.jeeplus.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.jeeplus.modules.sys.service.LanguageService;
import com.jeeplus.modules.sys.utils.DictUtils;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 登录Controller
 * @author jeeplus
 * @version 2013-5-31
 */
@Controller
public class LoginController extends BaseController{
	
	@Autowired
	private SessionDAO sessionDAO;
	
	@Autowired
	private OaNotifyService oaNotifyService;
	
	@Autowired
	private MailBoxService mailBoxService;
	@Autowired
	private MessageResource messageResource;
	
	@Autowired SessionLocaleResolver resolver;
	
	public static final String SESSION_TOKEN = "token";
	public static final String COOKIE_TOKEN = "token";
	public static final String SESSION_USER="user";
	
	
	/**
	 * 管理登录
	 * @throws IOException 
	 */
	@RequestMapping(value = "${adminPath}/login")
	public String login(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		/*
		Principal principal = UserUtils.getPrincipal();
		
		
		if (logger.isDebugEnabled()){
			logger.debug("login, active session size: {}", sessionDAO.getActiveSessions(false).size());
		}
		
		// 如果已登录，再次访问主页，则退出原账号。
		if (Global.TRUE.equals(Global.getConfig("notAllowRefreshIndex"))){
			CookieUtils.setCookie(response, "LOGINED", "false");
		}
		
		// 如果已经登录，则跳转到管理首页
		if(principal != null)
		{
			return "redirect:" + adminPath;
		}else if(principal!=null)
		{
			UserUtils.getSubject().logout();
		}
		/*if(principal != null && !principal.isMobileLogin()){
			return "redirect:" + adminPath;
		}*/
		
		//提取从Bowker portal传进的token参数
		String token=request.getParameter("token");
		//如果为空，则在Session提取是否有token
		//if(token==null)
		//{			
		//	token = CookieUtils.getCookie(request, COOKIE_TOKEN);
		//}
		
		String resultString = "";
		//若token不为空
 		if(token!=null)
		{
			resultString = RestUtil.SendGet("/api/token", "", token);
		}else{//若token 为空，则跳去bowker portal
			//return "redirect:"+"http://bowkerportaldev.azurewebsites.net?redirection=localhost:43433/prportal/a/login";
		}
		
		BasePortalAPIReturn returnObject = (BasePortalAPIReturn) JsonMapper.getInstance().fromJsonString(resultString, BasePortalAPIReturn.class);
		if(returnObject!=null && returnObject.getReturnCode().equals("0"))
		{			
			CookieUtils.setCookie(response, COOKIE_TOKEN, token);
			request.getSession().setAttribute(SESSION_USER,returnObject.getUserinfo().getUsername());
			request.getSession().setAttribute(SESSION_TOKEN,token);
			String username = "";
			if(StringUtils.isNotBlank(returnObject.getUserinfo().getAad()))
				username= returnObject.getUserinfo().getAad();
			else 
				username = returnObject.getUserinfo().getUsername();
				
			if(UserUtils.getByLoginName(username)==null)
			{
				return "modules/sys/noAccess";
			}
				
			
			
			model.addAttribute("username", username);
			model.addAttribute("password", "test");	
			model.addAttribute("redirectURL", DictUtils.getDictValue("BowkerPortal", "BowkerPortal","")+
				"?redirection="+DictUtils.getDictValue("WebSite","WebSite",""));
			
		
		}else{
			//return "redirect:"+"http://bowkerportaldev.azurewebsites.net?redirection=localhost:43433/prportal/a/login";
			model.addAttribute("username", "");
			model.addAttribute("password", "");
			model.addAttribute("redirectURL", DictUtils.getDictValue("BowkerPortal", "BowkerPortal","")+
				"?redirection="+DictUtils.getDictValue("WebSite","WebSite",""));
		
		
		}
		
			
		//禁用SSO功能
		/*model.addAttribute("username", "1");
		model.addAttribute("password", "");
		model.addAttribute("redirectURL", DictUtils.getDictValue("BowkerPortal", "BowkerPortal","")+"?redirection="+DictUtils.getDictValue("WebSite","WebSite",""));
		*/
		 return "modules/sys/sysLogin";	
	}

	/**
	 * 登录失败，真正登录的POST请求由Filter完成
	 */
	@RequestMapping(value = "${adminPath}/login", method = RequestMethod.POST)
	public String loginFail(HttpServletRequest request, HttpServletResponse response, Model model) {
		
		Principal principal = UserUtils.getPrincipal();
		if(principal != null && principal.getLoginName().equals((String)request.getSession().getAttribute(SESSION_USER)))
		{
			return "redirect:" + adminPath;
		}else if(principal != null)
		{
			UserUtils.getSubject().logout();
			
			return "redirect:" + adminPath+"/login";
			//return "modules/sys/sysLogin";
		}
		// 如果已经登录，则跳转到管理首页
		/*if(principal != null){ 
			return "redirect:" + adminPath;
		}*/

		String username = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
		boolean rememberMe = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM);
		boolean mobile = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_MOBILE_PARAM);
		String exception = (String)request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
		String message = (String)request.getAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM);
		
		if (StringUtils.isBlank(message) || StringUtils.equals(message, "null")){
			message = "用户或密码错误, 请重试.";
		}

		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM, rememberMe);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_MOBILE_PARAM, mobile);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME, exception);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM, message);
		
		if (logger.isDebugEnabled()){
			logger.debug("login fail, active session size: {}, message: {}, exception: {}", 
					sessionDAO.getActiveSessions(false).size(), message, exception);
		}
		
		// 非授权异常，登录失败，验证码加1。
		if (!UnauthorizedException.class.getName().equals(exception)){
			//model.addAttribute("isValidateCodeLogin", isValidateCodeLogin(username, true, false));
			model.addAttribute("isValidateCodeLogin", isValidateCodeLogin(username, false, false));
		}
		
		// 验证失败清空验证码
		request.getSession().setAttribute(ValidateCodeServlet.VALIDATE_CODE, IdGen.uuid());
		
		// 如果是手机登录，则返回JSON字符串
		if (mobile){
			AjaxJson j = new AjaxJson();
			j.setSuccess(false);
			j.setMsg(message);
			j.put("username", username);
			j.put("name","");
			j.put("mobileLogin", mobile);
			j.put("JSESSIONID", "");
	        return renderString(response, j.getJsonStr());
		}
		
		//return "modules/sys/sysLogin";
		
		return "redirect: "+DictUtils.getDictValue("BowkerPortal", "BowkerPortal","")+
				"?redirection="+DictUtils.getDictValue("WebSite","WebSite","");
	}
	
	

	/**
	 * home 页面
	 * @throws IOException 
	 */
	@Autowired
	private ActTaskService actTaskService;
	@Autowired
	private PrService prService;
	@RequestMapping(value = "${adminPath}/login/home")
	public String Home(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		Principal principal = UserUtils.getPrincipal();
		List<Menu> list = UserUtils.getMenuList();
		
		boolean flag1=false,flag2=false,flag3=false,flag4=false;
        for(Menu tmp:list){
        	if(flag1&&flag2&&flag3&&flag4)break;
        	if(tmp.getHref()==null)continue;
            if(tmp.getHref().equals("/act/task/todo/"))
            flag1 = true;
            if(tmp.getHref().equals("/oa/pr/tracing/"))
            flag2 = true;
            if(tmp.getHref().equals("/oa/pr/overview/")||tmp.getHref().equals("/oa/pr/overview"))
            flag3 = true;
            if(tmp.getHref().equals("/act/task/form?procDefId=pr:19:d17961db888b46eead8b24784efd674d"))
            flag4 = true;            
        }
        model.addAttribute("flag1", flag1);
        model.addAttribute("flag2", flag2);
        model.addAttribute("flag3", flag3);
        model.addAttribute("flag4", flag4);
		
        //获取to do list
		Act act = new Act();
		List<Act> listTodo = actTaskService.todoListForHome(act);
		model.addAttribute("list", listTodo);
		//model.addAttribute("timeZone", request.getSession().getAttribute(FormAuthenticationFilter.TIMEZONE));
		
		
		//获取my tracing
		Pr pr = new Pr();
		pr.setLoginName(UserUtils.getUser().getLoginName());
		Page<Pr> pagePram = new Page<Pr>(request, response);
		String tracingListNum = DictUtils.getDictValue("TRACING_LIST_NUM", "HOME_LIST_NUM", "");
		
		if(StringUtils.isNotBlank(tracingListNum))
		pagePram.setPageSize(Integer.parseInt(tracingListNum));
		else{
			pagePram.setPageSize(6);
		}
		
		Page<Pr> page = prService.OverViewListPage(pagePram,pr);

		model.addAttribute("page", page);
		

		//获取时区
		Subject subject = SecurityUtils.getSubject(); 
		Session session = subject.getSession(); 
		model.addAttribute("timeZone", session.getAttribute(FormAuthenticationFilter.TIMEZONE));
        
		return "modules/sys/home";	
	}
	
	/**
	 * 管理登录
	 * @throws IOException 
	 */
	@RequestMapping(value = "${adminPath}/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		Principal principal = UserUtils.getPrincipal();
		// 如果已经登录，则跳转到管理首页
		if(principal != null){
			UserUtils.getSubject().logout();
			
		}
	   // 如果是手机客户端退出跳转到login，则返回JSON字符串
			String ajax = request.getParameter("__ajax");
			if(	ajax!=null){
				model.addAttribute("success", "1");
				model.addAttribute("msg", "退出成功");
				return renderString(response, model);
			}
		 //return "redirect:" + adminPath+"/login";
		CookieUtils.getCookie(request, response, COOKIE_TOKEN);
		CookieUtils.setCookie(response, COOKIE_TOKEN, "", 0);

		return "redirect: "+DictUtils.getDictValue("BowkerPortal", "BowkerPortal","");
	}

	/**
	 * 登录成功，进入管理首页
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "${adminPath}")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		//messageResource.reload();
		String language = UserUtils.getUser().getLanguage();
		if(!StringUtils.isEmpty(language)){
			if(language.equals("zh_cn")){
				resolver.setLocale(request, response, Locale.CHINA );
			}else if(language.equals("hk")){
				resolver.setLocale(request, response, new Locale("hk", "HK"));
			}else if(language.equals("en")){
				resolver.setLocale(request, response, Locale.ENGLISH );
			}else if(language.equals("vn")){
				resolver.setLocale(request, response, new Locale("vn", "VN"));
			}else if(language.equals("cam")){
				resolver.setLocale(request, response, new Locale("cam", "CAM"));
			}else{
				resolver.setLocale(request, response, Locale.CHINA );
			}
		}
		
		
		Principal principal = UserUtils.getPrincipal();
		// 登录成功后，验证码计算器清零
		isValidateCodeLogin(principal.getLoginName(), false, true);
		/*
		 * 解决cacth问题
		if (logger.isDebugEnabled()){
			logger.debug("show index, active session size: {}", sessionDAO.getActiveSessions(false).size());
		}*/
		
		// 如果已登录，再次访问主页，则退出原账号。
		if (Global.TRUE.equals(Global.getConfig("notAllowRefreshIndex"))){
			String logined = CookieUtils.getCookie(request, "LOGINED");
			if (StringUtils.isBlank(logined) || "false".equals(logined)){
				CookieUtils.setCookie(response, "LOGINED", "true");
			}else if (StringUtils.equals(logined, "true")){
				UserUtils.getSubject().logout();
				return "redirect:" + adminPath + "/login";
			}
		}
		
		// 如果是手机登录，则返回JSON字符串
		if (principal.isMobileLogin()){
			if (request.getParameter("login") != null){
				return renderString(response, principal);
			}
			if (request.getParameter("index") != null){
				return "modules/sys/sysIndex";
			}
			return "redirect:" + adminPath + "/login";
		}
		
//		// 登录成功后，获取上次登录的当前站点ID
//		UserUtils.putCache("siteId", StringUtils.toLong(CookieUtils.getCookie(request, "siteId")));

//		System.out.println("==========================a");
//		try {
//			byte[] bytes = com.jeeplus.common.utils.FileUtils.readFileToByteArray(
//					com.jeeplus.common.utils.FileUtils.getFile("c:\\sxt.dmp"));
//			UserUtils.getSession().setAttribute("kkk", bytes);
//			UserUtils.getSession().setAttribute("kkk2", bytes);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
////		for (int i=0; i<1000000; i++){
////			//UserUtils.getSession().setAttribute("a", "a");
////			request.getSession().setAttribute("aaa", "aa");
////		}
//		System.out.println("==========================b");
		//

		
		
		//
		/*
		MailBox mailBox = new MailBox();
		mailBox.setReceiver(UserUtils.getUser());
		mailBox.setReadstatus("0");//筛选未读
		Page<MailBox> mailPage = mailBoxService.findPage(new MailPage<MailBox>(request, response), mailBox); 
		request.setAttribute("noReadCount", mailBoxService.getCount(mailBox));
		request.setAttribute("mailPage", mailPage);
		*/
		// 默认风格
		String indexStyle = "default";
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie == null || StringUtils.isEmpty(cookie.getName())) {
				continue;
			}
			if (cookie.getName().equalsIgnoreCase("theme")) {
				indexStyle = cookie.getValue();
			}
		}
		// 要添加自己的风格，复制下面三行即可

		if (StringUtils.isNotEmpty(indexStyle)
				&& indexStyle.equalsIgnoreCase("ace")) {
			return "modules/sys/sysIndex-ace";
		}
		
		return "modules/sys/sysIndex";
	}
	
	/**
	 * 获取主题方案
	 */
	@RequestMapping(value = "/theme/{theme}")
	public String getThemeInCookie(@PathVariable String theme, HttpServletRequest request, HttpServletResponse response){
		if (StringUtils.isNotBlank(theme)){
			CookieUtils.setCookie(response, "theme", theme);
		}else{
			theme = CookieUtils.getCookie(request, "theme");
		}
		return "redirect:"+request.getParameter("url");
	}
	
	/**
	 * 是否是验证码登录
	 * @param useruame 用户名
	 * @param isFail 计数加1
	 * @param clean 计数清零
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isValidateCodeLogin(String useruame, boolean isFail, boolean clean){
		Map<String, Integer> loginFailMap = (Map<String, Integer>)CacheUtils.get("loginFailMap");
		if (loginFailMap==null){
			loginFailMap = Maps.newHashMap();
			CacheUtils.put("loginFailMap", loginFailMap);
		}
		Integer loginFailNum = loginFailMap.get(useruame);
		if (loginFailNum==null){
			loginFailNum = 0;
		}
		if (isFail){
			loginFailNum++;
			loginFailMap.put(useruame, loginFailNum);
		}
		if (clean){
			loginFailMap.remove(useruame);
		}
		return loginFailNum >= 3;
	}
	
	
	/**
	 * 首页
	 * @throws IOException 
	 */
	@RequestMapping(value = "${adminPath}/home")
	public String home(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		
		
		
		return "modules/sys/sysHome";
		
	}
	
	/**
	 * login fail
	 * @throws IOException 
	 */
	@RequestMapping(value = "${adminPath}/noAccess")
	public String noAccess(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		
		
		
		return "modules/sys/noAccess";
		
	}
}
