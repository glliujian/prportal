/**
 * There are <a href="http://www.jeeplus.org/">jeeplus</a> code generation
 */
package com.jeeplus.modules.oa.web;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.mapper.JsonMapper;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.utils.AzureUpload;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.Encodes;
import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.oa.entity.Pr;
import com.jeeplus.modules.oa.service.PrService;
import com.jeeplus.modules.sys.entity.Dict;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.security.FormAuthenticationFilter;
import com.jeeplus.modules.sys.service.LanguageService;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.utils.DictUtils;
import com.jeeplus.modules.sys.utils.UserUtils;


/**
 * Pr审批单Controller
 * @author liuj
 * @version 2013-04-05
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/pr")
public class PrController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(getClass());
	//private static LanguageService languageService = SpringContextHolder.getBean(LanguageService.class);
	@Autowired
	protected PrService prService;
	
	@Autowired
	protected RuntimeService runtimeService;

	private static String BLOBNAME = "prportal";
	@Autowired
	protected TaskService taskService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	protected OfficeService officeService;
	
	
	@ModelAttribute
	public Pr get(@RequestParam(required=false) String id){//, 
//			@RequestParam(value="act.procInsId", required=false) String procInsId) {
		Pr pr = null;
		if (StringUtils.isNotBlank(id)){		
			//add by J 2017/6/24  因保存时没有生成processInstance，所以先判断	begin
			pr = prService.getById(id);
			if (pr != null){
				if(StringUtils.isNoneBlank(pr.getProcessInstanceId()))
				pr = prService.get(id);
			}
			//end by J 2017/6/24 加了保存功能
//		}else if (StringUtils.isNotBlank(procInsId)){
//			testAudit = testAuditService.getByProcInsId(procInsId);
		}
		if (pr == null){
			pr = new Pr();
		}
		return pr;
	}

	@RequestMapping(value = {"form"})
	public String form(Pr pr, Model model,HttpServletRequest request) {
		String view = "prForm";
		// 查看审批申请单
		
		
		try {
			pr.getAct().setTaskName(URLDecoder.decode(pr.getAct().getTaskName(),"utf8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (StringUtils.isNotBlank(pr.getId())){//.getAct().getProcInsId())){

			// 环节编号
			String taskDefKey = pr.getAct().getTaskDefKey();
			
			// 查看工单
			if(pr.getAct().isFinishTask()){
				view = "prView";
			}
			else if("ITCheckAudit".equals(taskDefKey))
			{
				view="prCheckerAudit";
			}
			else if("localITConfirmAudit".equals(taskDefKey))
			{
				view="prLocalITConfirm";
			}
			// 修改环节
			else if ("prModify".equals(taskDefKey)){
				view = "prModify";
			}
			else {
				view = "prAudit";
			}
			// 审核环节
			/*
			else if ("factoryGMAudit".equals(taskDefKey)){
				view = "prAudit";
			}
			// 审核环节
			else if ("assetAudit".equals(taskDefKey)){
				view = "prAudit";
			}
			// 审核环节2
			else if ("regionFCAudit".equals(taskDefKey)){
				view = "prAudit";
			}
			// 审核环节3
			else if ("groupMDAudit".equals(taskDefKey)){
				view = "prAudit";
			}
			// 审核环节4
			else if ("groupFCAudit".equals(taskDefKey)){
				view = "prAudit";
			}
			// 审核环节5
			else if ("ceoAudit".equals(taskDefKey)){
				view = "prAudit";
			}
			// 审核环节6
			else if ("cfoAudit".equals(taskDefKey)){
				view = "prAudit";
			}*/
			// modify PR 
			
		}
		try{
			if(view == "prForm")
			{
				User user = UserUtils.getUser();
				if(user.getCompany()!=null)
				{
					pr.setApplySiteCode(UserUtils.getUser().getCompany().getCode());
				}
				//设置币种默认值
				String currencyDefault = DictUtils.getDictValue(pr.getApplySiteCode(), "Apply_Site_Code_Currency", "");
				if(StringUtils.isNotBlank(currencyDefault))
				{
					pr.setPriceCurrency(currencyDefault);
				}
				
				//增加文档类型的识别 --20180128 by J--begin
				if(pr.getAct().getProcDefId().contains("pr:"))pr.setDocuType(1);
				else if(pr.getAct().getProcDefId().contains("pr_oem:"))pr.setDocuType(2);
				else return "modules/sys/noAccess";
				//增加文档类型的识别 --20180128 by J--end
			}
			
			getAttachmentAppendixFile(pr);
		}catch(Exception e)
		{
			
		}finally{
			model.addAttribute("pr", pr);
			//时区
			
			//获取时区
			Subject subject = SecurityUtils.getSubject(); 
			Session session = subject.getSession(); 
			model.addAttribute("timeZone", session.getAttribute(FormAuthenticationFilter.TIMEZONE));
			
		}
		

		return "modules/oa/"+view;
	}
	
	
	@RequestMapping(value = {"formDraft"})
	public String formDraft(Pr pr, Model model,HttpServletRequest request) {
		String view = "prDraftForm";
		// 查看审批申请单
		pr = prService.findbyPRPO(pr.getApplicationNo());
		getAttachmentAppendixFile(pr);
		model.addAttribute("MType", request.getParameter("MType"));
		model.addAttribute("pr",pr);
		return "modules/oa/"+view;
	}
	
	public void getAttachmentAppendixFile(Pr pr){
		//提取附件
		if(pr==null)return;
		String attachmentTemp = pr.getAttachment4();
		
		
		List<String> attachmentArray = new ArrayList<String>();
		if(StringUtils.isNotEmpty(attachmentTemp))
		{
					
			attachmentArray = Arrays.asList(attachmentTemp.split(","));
			
		}
		/*
		for(int i=0;i<attachmentArray.size();i++)
		{
			attachmentArray.set(i, "/"+BLOBNAME + "/"+attachmentArray.get(i));
		}*/
		pr.setAttachmentArray(attachmentArray);
	}
	
	public void deleteAllAttachments(Pr pr){
		AzureUpload azureUpload = new AzureUpload();
		
		if(StringUtils.isNotBlank(pr.getAttachment()))
		{
			azureUpload.deleteBlob(pr.getAttachment(), BLOBNAME);
		}
		if(StringUtils.isNotBlank(pr.getAttachment2()))
		{
			azureUpload.deleteBlob(pr.getAttachment2(), BLOBNAME);
		}
		if(StringUtils.isNotBlank(pr.getAttachment3()))
		{
			azureUpload.deleteBlob(pr.getAttachment3(), BLOBNAME);
		}
		
		
		getAttachmentAppendixFile(pr);
		List<String> deleteList = pr.getAttachmentArray();
		for(int i=0;i<deleteList.size();i++)
		{
			azureUpload.deleteBlob(deleteList.get(i), BLOBNAME);
		}
		
	}
	
	@RequestMapping(value={"deleteAttachment"})
	public String deleteAttachment(Pr pr,RedirectAttributes redirectAttributes)
	{
		String attachmentDelete = "";
		AzureUpload azureUpload = new AzureUpload();
		int deleteType = 0;
		String messageType = "success";
		Pr prTmp = prService.findbyPRPO(pr.getApplicationNo());
		if(StringUtils.isNotBlank(pr.getAttachment()))
		{
			attachmentDelete= Encodes.unescapeHtml(pr.getAttachment());
			deleteType = 1;
			if(azureUpload.deleteBlob(attachmentDelete, BLOBNAME))
			{
				addMessage(redirectAttributes, DictUtils.getLanguageLabel("已经删除附件"));
				prTmp.setAttachment("");
				prService.UpdatePr(prTmp);
			}else
			{
				messageType = "danger";
				addMessage(redirectAttributes, DictUtils.getLanguageLabel("实在抱歉，系统出现了错误，您可以尝试重新操作，或者联系系统管理人员！"));
			}
			return "redirect:" + adminPath + "/oa/pr/formDraft?applicationNo="+pr.getApplicationNo()+"&MType="+messageType;
		}
		else if(StringUtils.isNotBlank(pr.getAttachment2()))
		{
			attachmentDelete= Encodes.unescapeHtml(pr.getAttachment2());
			deleteType = 2;
			if(azureUpload.deleteBlob(attachmentDelete, BLOBNAME))
			{
				addMessage(redirectAttributes, DictUtils.getLanguageLabel("已经删除附件"));
				prTmp.setAttachment2("");
				prService.UpdatePr(prTmp);
			}else
			{
				messageType = "danger";
				addMessage(redirectAttributes, DictUtils.getLanguageLabel("实在抱歉，系统出现了错误，您可以尝试重新操作，或者联系系统管理人员！"));
			}
			return "redirect:" + adminPath + "/oa/pr/formDraft?applicationNo="+pr.getApplicationNo()+"&MType="+messageType;
		}
		else if(StringUtils.isNotBlank(pr.getAttachment3()))
		{
			attachmentDelete= Encodes.unescapeHtml(pr.getAttachment3());
			deleteType = 3;
			if(azureUpload.deleteBlob(attachmentDelete, BLOBNAME))
			{
				addMessage(redirectAttributes, DictUtils.getLanguageLabel("已经删除附件"));
				prTmp.setAttachment3("");
				prService.UpdatePr(prTmp);
			}else
			{
				messageType = "danger";
				addMessage(redirectAttributes, DictUtils.getLanguageLabel("实在抱歉，系统出现了错误，您可以尝试重新操作，或者联系系统管理人员！"));
			}
			return "redirect:" + adminPath + "/oa/pr/formDraft?applicationNo="+pr.getApplicationNo()+"&MType="+messageType;
		}
		else if(StringUtils.isNotBlank(pr.getAttachment4()))
		{
			attachmentDelete= Encodes.unescapeHtml(pr.getAttachment4());
			deleteType = 4;
		}
		//删除Appendix
		if(StringUtils.isBlank(attachmentDelete)){
			addMessage(redirectAttributes, DictUtils.getLanguageLabel("实在抱歉，系统出现了错误，您可以尝试重新操作，或者联系系统管理人员！"));
			messageType = "danger";
			return "redirect:" + adminPath + "/oa/pr/formDraft?applicationNo="+pr.getApplicationNo()+"&MType="+messageType;
		}

		List<String> attachmentArray = new ArrayList<String>();
		String newAttachmentString = "";
		
		getAttachmentAppendixFile(prTmp);
		attachmentArray = prTmp.getAttachmentArray();
		boolean flag = false;//判断是否找到要删除的attachment
		
		//构造新的attachment string串
		for(int i=0;i<attachmentArray.size();i++)
		{
			if(!attachmentArray.get(i).equals(attachmentDelete))
			{
				if(StringUtils.isNotBlank(newAttachmentString))
					newAttachmentString = newAttachmentString+"," + attachmentArray.get(i);
				else newAttachmentString = attachmentArray.get(i);
				
			}else {
				flag = true;
			}
		}
		if(flag)
		{
			if(azureUpload.deleteBlob(attachmentDelete, BLOBNAME))
			{
				addMessage(redirectAttributes, DictUtils.getLanguageLabel("已经删除附件"));
				prTmp.setAttachment4(newAttachmentString);

				prService.UpdatePr(prTmp);
			}
	
			else
			{
				messageType = "danger";
				addMessage(redirectAttributes, DictUtils.getLanguageLabel("实在抱歉，系统出现了错误，您可以尝试重新操作，或者联系系统管理人员！"));
			}
		}
		return "redirect:" + adminPath + "/oa/pr/formDraft?applicationNo="+pr.getApplicationNo()+"&MType="+messageType;
	
	}
	/**
	 * 删除草稿
	 * @param pr
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"deleteDraft"})
	public String deleteDraft(Pr pr,RedirectAttributes redirectAttributes) {
		String view = "prDraftList";
		String messageType = "success";
		Pr prDelete = prService.DeleteByPRPO(pr.getApplicationNo());
		if(prDelete==null)
			{
				messageType = "danger";
				addMessage(redirectAttributes, DictUtils.getLanguageLabel("实在抱歉，系统出现了错误，您可以尝试重新操作，或者联系系统管理人员！"));
			}
			
		else 
			{
				addMessage(redirectAttributes, DictUtils.getLanguageLabel("已经删除草稿：")+ " :" + prDelete.getApplicationNo());
				deleteAllAttachments(prDelete);//删除该PR草稿的所有附件
			}
		return "redirect:"+  adminPath + "/oa/pr/draft?MType="+messageType;
	}
	/**
	 * 驗證是否有相同的附件
	 * @param attachmentList
	 * @return
	 */
	/*
	private String filterSameAttachment(List<String> attachmentList) {
		// TODO Auto-generated method stub
		for(int i=0;i<attachmentList.size();i++)
		{
			for(int j=i+1;j<attachmentList.size();j++)
			{
				boolean flag = false;
				if(attachmentList.get(i).equals(attachmentList.get(j)))
				{
					flag = true;
				}
				if(!flag)
			}
		}
	}*/
	/**
	 * 启动PR流程
	 * @param pr	
	 */
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(Pr pr, RedirectAttributes redirectAttributes,Model model,
			@RequestParam("file1") MultipartFile[] file1,@RequestParam("file2") MultipartFile[] file2,@RequestParam("file3") MultipartFile[] file3,	
			@RequestParam("file") MultipartFile[] file,HttpServletRequest request) {
		String messageType = "success";
		try {
			//add by J 2017-6-4 auto-gen application begin ---
			User currentUser = UserUtils.getUser();
			
			
		//	String sequence = DictUtils.getSqlDictLabel("GET_PR_SEQ", "", "num");
			Office o =  UserUtils.getUser().getCompany();
			 	
			
			//end ---
			Map<String, Object> variables = Maps.newHashMap();
			
			//prNewId = pr.getId();
			
			
			if(currentUser!=null&&!StringUtils.isNotBlank(pr.getApplicationNo()))
			{	
				String sequenceAuto = DictUtils.getSqlField("GET_PR_SEQ_BYYEAR", "seq", DateUtils.getYear());
				pr.setApplicationNo("PRPO-MIS-" + UserUtils.getUser().getCompany().getCode()+"-"+ DateUtils.getYear() + "-"+ sequenceAuto );
				//pr.setSeq(Integer.parseInt(sequence));
			}
			//保存报价单1,2,3
			if(file1.length>0)
			pr.setAttachment(UploadQuotation(file1, pr, request));
			if(file2.length>0)
			pr.setAttachment2(UploadQuotation(file2, pr, request));
			if(file3.length>0)
			pr.setAttachment3(UploadQuotation(file3, pr, request));
			
			//保存Appendix			
			String attachment4 = pr.getAttachment4();
			int count=0;//记录附件数量
			for(int i=0;i<file.length;i++)
			{
				String fileName = saveFiletoAzure(file[i],request,Integer.toString(i+1),pr.getApplicationNo()+Integer.toString(i+1));			
				if(StringUtils.isNotBlank(fileName))
				{
					
					if(StringUtils.isNotBlank(attachment4))
					{
						attachment4 = attachment4+","+fileName;
					}else{
						attachment4 = fileName;
					}
					
				}				
			}
			pr.setAttachment4(attachment4);
			//取得附件数量			
			if(StringUtils.isNotBlank(attachment4))
			count = Arrays.asList(attachment4.split(",")).size();
			
			
			if(pr.getAct().getFlag().equals("0"))
			{
				
				prService.saveAsDraft(pr, variables);
				
				addMessage(redirectAttributes, DictUtils.getLanguageLabel("PR申请已经为您保存, 申请号为")+ " :" + pr.getApplicationNo());
			}
			else{
				
			}
			
			
			if(pr.getAct().getFlag().equals("1"))
			{
				//计算出所需要的附件数量，根据不同币种转换为港币---begin
				if(!validateAttachmentQuantity(pr,count)){
					//prService.saveAsDraft(pr, variables);
					//addMessage(redirectAttributes, DictUtils.getLanguageLabel("由于您的报价单数量不够，系统已经为您保存, 请补充上传报价单，申请号为")+ " :" + pr.getApplicationNo());
					//return "redirect:" + adminPath + "/oa/pr/formDraft?applicationNo="+pr.getApplicationNo();
					addMessage(redirectAttributes, DictUtils.getLanguageLabel("您的报价单数量不足，可能会影响审批结果。"));
				}
				//计算出所需要的附件数量，根据不同币种转换为港币  end--
				
				
				prService.save(pr, variables);
				
				addMessage(redirectAttributes, DictUtils.getLanguageLabel("PR申请已经提交, 申请号为")+ " :" + pr.getApplicationNo());
			}
			
			
		} catch (Exception e) {
			logger.error("启动PR流程失败：", e);
			messageType="danger";
			addMessage(redirectAttributes, DictUtils.getLanguageLabel("实在抱歉，系统出现了错误，您可以尝试重新操作，或者联系系统管理人员！"));
			
		}
		
		if(pr.getAct().getFlag().equals("0"))
		{
			
			return "redirect:" + adminPath + "/oa/pr/formDraft?applicationNo="+pr.getApplicationNo() + "&MType="+messageType;
		}
		else if(pr.getAct().getFlag().equals("1"))
		{
			return "redirect:" + adminPath + "/oa/pr/tracing?" + "MType="+messageType;
			
		}
		return "redirect:" + adminPath + "/act/task/draft?" + "MType="+messageType;
		
	}
	
	private String UploadQuotation(MultipartFile[] file,Pr pr,HttpServletRequest request)
	{
		String fileName = "";
		for(int i=0;i<file.length;i++)
		{
			fileName = saveFiletoAzure(file[i],request,Integer.toString(i+1),pr.getApplicationNo()+Integer.toString(i+1));			
			
		}
		return fileName;
	}
	
	private boolean validateAttachmentQuantity(Pr pr,int count) {
		
		// TODO Auto-generated method stub
		Double amount = 0.0;
		String tmp="";
		
		String enableChecker = DictUtils.getDictValue("ENABLE", "ATTACHMENT_CHECKER", "");
		if(!enableChecker.equals("1"))
		{
			return true;
		}
		
		if(pr.getPriceCurrency().equals("USD"))
		{
			tmp = DictUtils.getDictValue("USD", "CURRENCY_RATE", "");
			
		}else if(pr.getPriceCurrency().equals("RMB"))
		{
			tmp = DictUtils.getDictValue("RMB", "CURRENCY_RATE", "");
			
		}
		if(StringUtils.isNotBlank(tmp))amount = pr.getPriceAmount()/Double.parseDouble(tmp);
		else amount = pr.getPriceAmount();
		
		if((amount>100000&&count<3)||(amount<=100000&&count<2))
		{
			return false;
		}
		return true;
	}
	
	@RequestMapping(value = "draft")
	public String getDraft(Model model,HttpServletRequest request)
	{
		User user = UserUtils.getUser();
		List<Pr> draftList = null;
		try{
			if(StringUtils.isNoneBlank(user.getId()))
			{
				draftList = prService.getDraft(user.getId());
			}
		}catch(Exception e)
		{
			
		}
		model.addAttribute("MType",request.getParameter("MType"));
		model.addAttribute("list", draftList);
		//获取时区
		Subject subject = SecurityUtils.getSubject(); 
		Session session = subject.getSession(); 
		model.addAttribute("timeZone", session.getAttribute(FormAuthenticationFilter.TIMEZONE));
		return "modules/oa/prDraftList"; 
	}
	
	
	/**
	 * 工单执行（完成任务）
	 * @param testAudit
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "saveAudit")
	public String saveAudit(Pr pr,Map<String, Object> vars, Model model,RedirectAttributes redirectAttributes,HttpServletRequest request) {
		// 不需要强制必填
		/*if (StringUtils.isBlank(pr.getAct().getComment())){
			addMessage(model, "请填写审核意见。");
			return form(pr, model, request);
		}*/
		String messageType = "success";
		try{
			prService.auditSave(pr);
			
			addMessage(redirectAttributes, DictUtils.getLanguageLabel("提交成功"));
		}
		catch(Exception e)
		{
			messageType= "danger";
			addMessage(redirectAttributes, DictUtils.getLanguageLabel("实在抱歉，系统出现了错误，您可以尝试重新操作，或者联系系统管理人员！"));
		}
		
		return "redirect:" + adminPath + "/act/task?MType="+messageType;
		
		//return form(prService.get(pr.getId()), model, request);
	}
	
	/**
	 * 工单执行（修改任务）
	 * @param testAudit
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "saveModify")
	public String saveModify(Pr pr,Map<String, Object> vars, Model model, RedirectAttributes redirectAttributes,
			@RequestParam("file1") MultipartFile[] file1,@RequestParam("file2") MultipartFile[] file2,@RequestParam("file3") MultipartFile[] file3,
			@RequestParam("file") MultipartFile[] file,HttpServletRequest request) {
		//保存报价单1,2,3
		if(file1.length>0)
		pr.setAttachment(UploadQuotation(file1, pr, request));
		if(file2.length>0)
		pr.setAttachment2(UploadQuotation(file2, pr, request));
		if(file3.length>0)
		pr.setAttachment3(UploadQuotation(file3, pr, request));
		
		//保存附件
		String attachment = pr.getAttachment4();
		int count=0;//记录附件数量
		
		String messageType = "success";
		for(int i=0;i<file.length;i++)
		{
			String fileName = saveFiletoAzure(file[i],request,Integer.toString(i+1),pr.getApplicationNo());			
			if(StringUtils.isNotBlank(fileName))
			{
				
				if(StringUtils.isNotBlank(attachment))
				{
					attachment = attachment+","+fileName;
				}else{
					attachment = fileName;
				}
				
			}				
		}
		pr.setAttachment4(attachment);
		if(StringUtils.isNotBlank(attachment))
			count = Arrays.asList(attachment.split(",")).size();
		if(!validateAttachmentQuantity(pr,count))
		{
			//prService.UpdatePr(pr);
			//addMessage(redirectAttributes, DictUtils.getLanguageLabel("由于您的报价单数量不够，系统已经为您保存, 请补充上传报价单，申请号为")+ " :" + pr.getApplicationNo());
			//return "redirect:" + adminPath + "/act/task";
			addMessage(redirectAttributes, DictUtils.getLanguageLabel("您的报价单数量不足，可能会影响审批结果。"));
		}

		prService.modifySave(pr);
		return "redirect:" + adminPath + "/act/task?MType="+messageType;
	}
	/**
	 * 任务列表
	 * @param pr	
	 */
	@RequestMapping(value = {"list/task",""})
	public String taskList(HttpSession session, Model model) {
		String userId = UserUtils.getUser().getLoginName();//ObjectUtils.toString(UserUtils.getUser().getId());
		List<Pr> results = prService.findTodoTasks(userId);
		model.addAttribute("prs", results);
		return "modules/oa/prTask";
	}

	/**
	 * 读取所有流程
	 * @return
	 */
	@RequestMapping(value = {"list"})
	public String list(Pr pr, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<Pr> page = prService.find(new Page<Pr>(request, response), pr); 
        model.addAttribute("page", page);
		return "modules/oa/prList";
	}
	
	/**
	 * 读取详细数据
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "detail/{id}")
	@ResponseBody
	public String getPr(@PathVariable("id") String id) {
		Pr pr = prService.get(id);
		return JsonMapper.getInstance().toJson(pr);
	}

	/**
	 * 读取详细数据
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "detail-with-vars/{id}/{taskId}")
	@ResponseBody
	public String getPrWithVars(@PathVariable("id") String id, @PathVariable("taskId") String taskId) {
		Pr pr = prService.get(id);
		Map<String, Object> variables = taskService.getVariables(taskId);
		pr.setVariables(variables);
		return JsonMapper.getInstance().toJson(pr);
	}

	
	
	private String saveFile(MultipartFile file,HttpServletRequest request,String type) {  
		
        // 判断文件是否为空  
        if (!file.isEmpty()) {  
        	
            try {  
            	String sequenceAuto = DictUtils.getSqlDictLabel("GET_PR_SEQ_BYYEAR", "", "num");
        		String PRPO = "PRPO-MIS-" + UserUtils.getUser().getCompany().getCode()+"-"+ DateUtils.getYear() + "-"+ sequenceAuto;
        		String fileName =PRPO +type + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        		
        		String webiste2 = request.getContextPath();
                String filePath = request.getSession().getServletContext().getRealPath("/") + "upload/"  
                        //+ file.getOriginalFilename();
                		+ fileName;
               
                file.transferTo(new File(filePath));  
                return filePath;  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
        return "";  
    }  
	
	private String saveFiletoAzure(MultipartFile file,HttpServletRequest request,String type,String Prefix) {  
		
       AzureUpload azureUpload = new AzureUpload();
       
       String result = azureUpload.uploadToBlob(file,BLOBNAME,Prefix);
       
       return result;
    }  
	/**
	 * 获取所有申请表单
	 */
	@RequestMapping(value = {"overview"})
	public String OverViewList(Pr pr, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		Page<Pr> page = prService.OverViewListPage(new Page<Pr>(request, response),pr);
		model.addAttribute("page", page);
		
		//获取时区
		Subject subject = SecurityUtils.getSubject(); 
		Session session = subject.getSession(); 
		
		model.addAttribute("timeZone", session.getAttribute(FormAuthenticationFilter.TIMEZONE));
		
		if (UserUtils.getPrincipal().isMobileLogin()){
			return renderString(response, page);
		}
		return "modules/act/actTaskOverViewList";
	}
	/**
	 * 获取用户申请表单的状态
	 * @param pr
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = {"tracing"})
	public String tracingList(Pr pr, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		
		pr.setLoginName(UserUtils.getUser().getLoginName());
		Page<Pr> page = prService.OverViewListPage(new Page<Pr>(request, response),pr);
		
		model.addAttribute("page", page);
		//获取时区
		Subject subject = SecurityUtils.getSubject(); 
		Session session = subject.getSession(); 
		model.addAttribute("timeZone", session.getAttribute(FormAuthenticationFilter.TIMEZONE));
		model.addAttribute("MType", request.getParameter("MType"));
		if (UserUtils.getPrincipal().isMobileLogin()){
			return renderString(response, page);
		}
		return "modules/act/actTaskTracingList";
	}
	@RequestMapping(value = {"printDetail"})
	public void export(Pr pr,HttpServletRequest request, HttpServletResponse response,String ids) {
		if(!StringUtils.isNotBlank(ids))
		{
			exportPRDetail(pr.getApplicationNo(),request,response);
			return;
		}
		
		
		exportPRDetailAll(ids,request,response);
		
	}
		
	private void exportPRDetail(String prNo,HttpServletRequest request, HttpServletResponse response)
	{
		Pr pr;
		String filePath = request.getSession().getServletContext().getRealPath("/excel/PRTemplate.xls"); 
		String pic= request.getSession().getServletContext().getRealPath("/excel/signature2.png");  
		if(StringUtils.isNotBlank(DictUtils.getDictValue("SignatureIT", "Signature", "")))
		pic= request.getSession().getServletContext().getRealPath("/excel/"+DictUtils.getDictValue("SignatureIT", "Signature", ""));   
		//ExportExcel exportExcel = new ExportExcel(pr.getApplicationNo()+"-Detail",Pr.class);
		try {
			HSSFWorkbook workbook = fillExcel(prNo,filePath,pic);
			//sheet1.getRow(6).getCell(2).setCellValue(pr.getApplicationNo());
//			/sheet1.getRow(7).getCell(3).setCellValue(pr.getApplicationNo());
			OutputStream output=response.getOutputStream();  
		    response.reset();  
		    response.setHeader("Content-disposition", "attachment; filename="+prNo+".xls");  
		    response.setContentType("application/msexcel");          
		    workbook.write(output);  
		    output.flush();
		    output.close();  
		    workbook.close();
		    
           
			//write(response.getOutputStream());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void exportPRDetailAll(String prNo,HttpServletRequest request, HttpServletResponse response)
	{
		String idArray[] =prNo.split(",");
		if(idArray.length==1){exportPRDetail(idArray[0],request,response);return;}
		 File srcfile[] = new File[idArray.length];
		 File zip = new File(request.getSession().getServletContext().getRealPath("/excel/test.zip") );// 压缩文件  
		 String pic= request.getSession().getServletContext().getRealPath("/excel/signature2.png");
		 if(StringUtils.isNotBlank(DictUtils.getDictValue("SignatureIT", "Signature", "")))
			 pic= request.getSession().getServletContext().getRealPath("/excel/"+DictUtils.getDictValue("SignatureIT", "Signature", ""));   
		 ZipOutputStream out;
		try {
			out = new ZipOutputStream(new FileOutputStream(  
			    		zip));
			byte[] buf = new byte[1024];  
			for(String id : idArray){
				int i=0;
				Pr pr;
				String filePath = request.getSession().getServletContext().getRealPath("/excel/PRTemplate.xls");  
				//ExportExcel exportExcel = new ExportExcel(pr.getApplicationNo()+"-Detail",Pr.class);
				
					int celIndex =2;
					NumberFormat numberFormat = NumberFormat.getNumberInstance();  
					pr = prService.findbyPRPO(id);
					HSSFWorkbook workbook = fillExcel(id,filePath,pic);
					
					FileOutputStream o = new FileOutputStream(filePath);  
					workbook.write(o);
					
					FileInputStream in = new FileInputStream(filePath); 
						    
					out.putNextEntry(new ZipEntry(id+".xls"));  
					int len;  
					while ((len = in.read(buf)) > 0) {  
			                    out.write(buf, 0, len);  
			         }  
						    
			         out.closeEntry(); 
			         in.close();  

		           i++;
		
			}
			out.close();
			//写入输出流
			OutputStream output2=response.getOutputStream();
			response.reset();// 清空输出流  
            response.setContentType("application/octet-stream;charset=UTF-8");  
            response.setHeader("Content-Disposition", "attachment;filename="  
                    +"PRFiles-"+DateUtils.formatDate(new Date())
                    + ".zip");  
            //response.addHeader("Pargam", "no-cache");  
            //response.addHeader("Cache-Control", "no-cache");
            FileInputStream inStream = new FileInputStream(zip);  
            byte[] buf2 = new byte[4096];  
            int readLength;  
            while (((readLength = inStream.read(buf2)) != -1)) {  
            	output2.write(buf2, 0, readLength);  
            }  
            
            inStream.close();  
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		 
	}

	private HSSFWorkbook fillExcel(String prNo,String filePath, String PicPath)
	{
		try{
			Pr pr;
			int celIndex =2;
			NumberFormat numberFormat = NumberFormat.getNumberInstance();  
			
			pr = prService.findbyPRPO(prNo);
			FileInputStream fileInputStream = new FileInputStream(filePath);
			HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
			HSSFSheet sheet1 = workbook.getSheetAt(0);
			HSSFCellStyle cellStyle=workbook.createCellStyle(),cellStyleUnBold=workbook.createCellStyle();         
			//cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);  
			cellStyle.setAlignment(HorizontalAlignment.RIGHT);
			cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyleUnBold.setAlignment(HorizontalAlignment.RIGHT);
			cellStyleUnBold.setVerticalAlignment(VerticalAlignment.CENTER);
			HSSFFont font=workbook.createFont(),fontUnBold = workbook.createFont();
			String tt = sheet1.getRow(6).getCell(celIndex).getStringCellValue();
			
			
	          font.setBold(true);
	          fontUnBold.setBold(false);
	         //把字体应用到当前的样式
	          cellStyle.setFont(font);
	          cellStyleUnBold.setFont(fontUnBold);
			if(StringUtils.isNotBlank(pr.getApplySiteCode()))
			sheet1.getRow(0).getCell(0).setCellValue(officeService.getByCode(pr.getApplySiteCode())!=null?officeService.getByCode(pr.getApplySiteCode()).getName():"");
			sheet1.getRow(6).createCell(celIndex).setCellValue(pr.getApplicationNo());
			sheet1.getRow(2).createCell(celIndex).setCellValue(pr.getRequestUserDepartment());
			sheet1.getRow(3).createCell(celIndex).setCellValue(pr.getCostCenter());
			sheet1.getRow(6).createCell(8).setCellValue(pr.getCreateDate()!=null?DateUtils.formatDate(pr.getCreateDate()):"");
			
			sheet1.getRow(12).getCell(celIndex).setCellValue(pr.getEquipment());
			sheet1.getRow(14).getCell(celIndex).setCellValue(pr.getBrand());
			sheet1.getRow(16).getCell(celIndex).setCellValue(pr.getModelNo());
			sheet1.getRow(18).getCell(celIndex).setCellValue(pr.getSupplier());
			sheet1.getRow(20).getCell(celIndex).setCellValue(numberFormat.format(pr.getUnitPrice()) +" ("+ pr.getPriceCurrency()+")");
			sheet1.getRow(20).getCell(7).setCellValue(pr.getQuantity());
			sheet1.getRow(22).getCell(celIndex).setCellValue(numberFormat.format(pr.getPriceAmount())+" ("+ pr.getPriceCurrency()+")");
			sheet1.getRow(27).getCell(celIndex).setCellValue(pr.getRequestArrivalDate()!=null?DateUtils.formatDate(pr.getRequestArrivalDate()):"");
			String teString =  sheet1.getRow(35).getCell(3).getStringCellValue();
			if(pr.getBudgetType()!=null)
			{
				if(pr.getBudgetType().equals("1"))
				{
				sheet1.getRow(35).getCell(3).setCellValue("X");
				sheet1.getRow(35).getCell(3).setCellStyle(cellStyle);
				
				sheet1.getRow(35).getCell(6).setCellValue("口");
				sheet1.getRow(35).getCell(6).setCellStyle(cellStyleUnBold);
				}
				else if(pr.getBudgetType().equals("0"))
				{
					sheet1.getRow(35).getCell(6).setCellValue("X");
					sheet1.getRow(35).getCell(6).setCellStyle(cellStyle);
					
			          sheet1.getRow(35).getCell(3).setCellValue("口");
						sheet1.getRow(35).getCell(3).setCellStyle(cellStyleUnBold);
				}
			
			}
			
			
			//如果流程结束，则添加签名
			if(validateIfEnd(pr))
			addPic(PicPath,sheet1,workbook);
			      
			
             
			HSSFSheet sheet2 = workbook.getSheetAt(1);
			if(StringUtils.isNotBlank(pr.getApplySiteCode()))
			sheet2.getRow(0).getCell(0).setCellValue(officeService.getByCode(pr.getApplySiteCode())!=null?officeService.getByCode(pr.getApplySiteCode()).getName():"");
			sheet2.getRow(4).createCell(celIndex).setCellValue(pr.getApplicationNo());
			sheet2.getRow(4).createCell(8).setCellValue(pr.getCreateDate()!=null?DateUtils.formatDate(pr.getCreateDate()):"");
			sheet2.getRow(6).createCell(celIndex).setCellValue(pr.getEquipment());
			sheet2.getRow(7).createCell(celIndex).setCellValue(pr.getBrand());
			sheet2.getRow(8).createCell(celIndex).setCellValue(pr.getModelNo());
			sheet2.getRow(9).getCell(celIndex).setCellValue(pr.getQuantity());
			//sheet2.getRow(12).getCell(0).setCellStyle(cellStyle);
			sheet2.getRow(12).getCell(0).setCellValue(pr.getPurchasePurpose());
			//sheet2.getRow(22).getCell(0).setCellStyle(cellStyle);
			sheet2.getRow(22).getCell(0).setCellValue(pr.getReturnOnInvestment());
			
			
			fileInputStream.close();
			return workbook;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			return null;
		}
		
	}
	private void addPic(String pic,HSSFSheet sheet1,HSSFWorkbook wb)
	{
		 BufferedImage bufferImg = null;  
		 ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();     
         try {
			bufferImg = ImageIO.read(new File(pic));
			ImageIO.write(bufferImg, "png", byteArrayOut);  

	         //画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）  
	         HSSFPatriarch patriarch = sheet1.createDrawingPatriarch();     
	         //anchor主要用于设置图片的属性  
	         
	         HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 255, 255,(short) 0, 48, (short) 3, 52);     
	         anchor.setAnchorType(3);     
	         //插入图片    
	         patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG)); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}     
         
	}
	private boolean validateIfEnd(Pr pr)
	{
		TaskQuery taskQuery = taskService.createTaskQuery().processInstanceId(pr.getProcessInstanceId());
		List<Task> check =  taskQuery.list();
		if(check.size()==0)
		{
			HistoricTaskInstanceQuery historyTaskQuery =  historyService.createHistoricTaskInstanceQuery().processInstanceId(pr.getProcessInstanceId()).includeTaskLocalVariables();
			List<HistoricTaskInstance> historicVList= historyTaskQuery.list();
			
			for(int i=0;i<historicVList.size();i++)
			{
				HistoricTaskInstance item = historicVList.get(i);
				if(item.getTaskLocalVariables().get("pass")!=null)
				{
					String action = item.getTaskLocalVariables().get("pass").toString();
					if(action.equals("4"))return false;
				}
			}
			return true;
			
		}
		//	return true;
		return false;
	}
	
}
