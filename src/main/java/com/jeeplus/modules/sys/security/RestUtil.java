package com.jeeplus.modules.sys.security;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.jeeplus.common.mapper.JsonMapper;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.sys.entity.BasePortalAPIReturn;
import com.jeeplus.modules.sys.utils.DictUtils;



@Service
public class RestUtil {

	private static Logger logger = LoggerFactory.getLogger(RestUtil.class);

	private RestUtil(){
		super();
	}

	public static String SendPost(String method, Map<String, String> query, String token){
//		String url = dictService.getData("BASEPORTAL_URL", "BASEPORTAL_URL") + method;
		String urlpram = DictUtils.getDictValue("BasePortal", "BasePortal", "");
		String url = urlpram+"baseportal" + method;
		String result = "";  
		Locale lang = Locale.SIMPLIFIED_CHINESE;
		HttpPost httpPost = new HttpPost(url);
		try {
			
			token = URLEncoder.encode(token, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		httpPost.addHeader("Authorization", "Bearer " + token);
		httpPost.addHeader("Accept-Language", lang.getLanguage());
		
		List <NameValuePair> params = new ArrayList<>();  
		Iterator iter = query.entrySet().iterator();
		while(iter.hasNext()){
			Entry<String, String> entry = (Entry<String, String>) iter.next();
			params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8)); 

			HttpResponse httpResponse = httpclient.execute(httpPost);  
			if(httpResponse.getStatusLine().getStatusCode() == 200){  
				HttpEntity httpEntity = httpResponse.getEntity();  
				result = EntityUtils.toString(httpEntity);//取出应答字符串
			} 
		} catch (IOException e) {
			logger.info(e.getMessage(), e);
		}finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				logger.info(e.getMessage(), e);
			}
		}

		return result;  
	}
	public static String SendGet(String method, String param, String token){
		//String url = dictService.getData("BASEPORTAL_URL", "BASEPORTAL_URL") + method;
		String urlpram = DictUtils.getDictValue("BasePortal", "BasePortal", "");
		
		String url = urlpram+"baseportal" + method+param;		
		//DictUtils.getDictValue("", type, defaultLabel)
		Locale lang = Locale.SIMPLIFIED_CHINESE;
		
		String result = "";  
		HttpGet httpGet = new HttpGet(url);
		try {
			token = URLEncoder.encode(token, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		httpGet.addHeader("Authorization", "Bearer " + token);
		httpGet.addHeader("Accept-Language", lang.getLanguage());
		
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			//if(!params.isEmpty())
			//httpGet.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8)); 

			HttpResponse httpResponse = httpclient.execute(httpGet); 
			HttpEntity httpEntity2 = httpResponse.getEntity();  
			result = EntityUtils.toString(httpEntity2);
			int flag = httpResponse.getStatusLine().getStatusCode();
			if(httpResponse.getStatusLine().getStatusCode() == 200){  
				HttpEntity httpEntity = httpResponse.getEntity();  
				result = EntityUtils.toString(httpEntity);//取出应答字符串
			} 
		} catch (IOException e) {
			logger.info(e.getMessage(), e);
		}finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				logger.info(e.getMessage(), e);
			}
		}

		return result;  
	}

	public static String validateToken(HttpServletRequest request){
		 // 检查token
	    String token = request.getHeader("authorization");
	    String resultString = "";
	    if(token!=null)
		{
			resultString = RestUtil.SendGet("/api/token", "", token);
		}
		BasePortalAPIReturn returnObject = (BasePortalAPIReturn) JsonMapper.getInstance().fromJsonString(resultString, BasePortalAPIReturn.class);
		if (returnObject == null) {
			throw new RuntimeException("无效的Token");
		}
		String user = "";
		if(returnObject!=null && returnObject.getReturnCode().equals("0"))
		{			
			if(StringUtils.isNotBlank(returnObject.getUserinfo().getAad()))
				user = returnObject.getUserinfo().getAad();
			else 
				user = returnObject.getUserinfo().getUsername();
		
		}
		return user;
	}

	public static void main(String []args) {
		RestUtil restUtil = new RestUtil();
		String token ="eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJqd3QiLCJpYXQiOjE0OTc1MTMyNjcsInN1YiI6IntcInJvbGVpZFwiOlwiXCIsXCJ1c2VyaWRcIjo1MyxcInVzZXJuYW1lXCI6XCJqYXlkb25sQHdpbi1oYW52ZXJreS5jb20uaGtcIn0iLCJleHAiOjE0OTc1Mjc2Njd9.FUOQbWLciwJWdJTmqm6ECmQyPiD1DQ5k_twhSJiYIBw";
		//String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJqd3QiLCJpYXQiOjE0OTU1Mjc1OTksInN1YiI6IntcImRlcHRpZFwiOjUsXCJyb2xlaWRzXCI6XCI3ODFhY2IyMzYxMjQ0ZTQ5YWVmNTA5Yzg2ODhjM2VjMiwxYzU0ZTAwM2MxZmM0ZGNkOWIwODdlZjhkNDhhYmFjMyxjYWFjZjYxMDE3MTE0MTIwYmNmN2JmMTA0OWI2ZDRjM1wiLFwidXNlcmlkXCI6MixcInVzZXJuYW1lXCI6XCJhZG1pblwifSIsImV4cCI6MTQ5NTU0OTE5OX0.NfOphNMB5XB6QQDX8V9RPwForeboMRPQdAtW3BHMQIs";
		Map<String, String> parameter = new HashMap<>();
		Locale lang = Locale.SIMPLIFIED_CHINESE;
//		parameter.put("userName", "admin");
		//parameter.put("userid", "1001");
//		parameter.put("password", "jGl25bVBBBW96Qi9Te4V37Fnqchz/Eu4qB9vKrRIqRg=");
//		parameter.put("timestamp", "2017-05-19 16:22:00");
//		
//		parameter.put("sign", TokenUtil.sign(parameter));
//		parameter.remove("password");

//		String resultString = restUtil.load("/api/authorize", parameter, token);
		//String resultString = restUtil.SendGet("/api/token","", token, lang);
		//System.out.println(resultString); 	
	}
}
