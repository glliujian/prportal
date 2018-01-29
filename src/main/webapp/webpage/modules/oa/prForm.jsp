<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>PR申请</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $("#inputForm").submit();
			  return true;
		  }
	
		  return false;
		}
		var newComponent=5;
		function newComponentFunction()
		{
			
			var appendNode="<div>"
								+"<input type='button' value='<spring:message code='选择文件'></spring:message>'  OnClick='javascript:$(\"#uploadFile"+newComponent+"\").click()'/><input style='width:80%;border-style: none;background-color: rgba(0,0,0,0);' id='showSrc"+newComponent+"' type='text' readonly='readonly'/>"
								+"<input id='uploadFile"+newComponent+"' style='display:none'  name='file'  type='file' OnChange='checkfile("+newComponent+",5)' />"
							+"</div>";
			console.log(appendNode);
			$("#Appendix").before(appendNode);
			$("#uploadFile"+newComponent).click();
			newComponent++;
		}
		
		$(document).ready(function() {
			//判断输入的数字是否为小数点后两位：
			/*$("#unitPrice").on('input',function(e){  
			   var value = $("#unitPrice").val();
			   if(value.toString().split(".")[1].length>2)
			   {
				  	alert("<spring:message code='请保留小数点后两位！'></spring:message>");
				  	$("#unitPrice").val("");
			   }
			});  */
			
			
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					var submitMessage="<spring:message code='正在提交，请稍等...'></spring:message>";
					if($('#flag').val()==0){submitMessage = "<spring:message code='正在保存，请稍等...'></spring:message>"}
					
					if($("#unitPrice").val() * $("#quantity").val()>=100000000)
					{
						alert('<spring:message code="金额必须小于100,000,000"></spring:message>');
						$("#priceAmountDisplay").val("");
						$("#priceAmount").val("");
						
					}
					else if(!validateIsExistQuotation())
						{
							confirmx('<spring:message code="您的报价单数量不足，请确认是否需要继续 ？"></spring:message>',function(){
								loading(submitMessage);
								form.submit();
								//防止用戶多點
								$('#btnSave').attr('disabled','disabled');
								$('#btnSubmit').attr('disabled','disabled');
							});
								
						}
					
					else{
						var index = loading(submitMessage);
						form.submit();
						//防止用戶多點
						$('#btnSave').attr('disabled','disabled');
						$('#btnSubmit').attr('disabled','disabled');
					}
					
					
					
					
					//uploadFileSubmit(1);
					//uploadFileSubmit(2);
					//uploadFileSubmit(3);
					//uploadFileSubmit(4);
					
					
					
				},
				
				messages: {  
					applyUserDepartmentName: { required: "<spring:message code='这是必填字段。'></spring:message>" }  ,
	                  unitPrice: { required: "<spring:message code='这是必填字段。'></spring:message>",number:"<spring:message code='请输入有效的数字。'></spring:message>",round:"<spring:message code='请保持小数点后两位。'></spring:message>"},  
	                  equipment: { required: "<spring:message code='这是必填字段。'></spring:message>"},  
	                  brand: { required: "<spring:message code='这是必填字段。'></spring:message>" }  ,
	                  quantity: { required: "<spring:message code='这是必填字段。'></spring:message>",digits: "<spring:message code='请输入整数。'></spring:message>" }  ,
	                  equipmentDesciption:{ required: "<spring:message code='这是必填字段。'></spring:message>" }  ,
	                  purchasePurpose:{ required: "<spring:message code='这是必填字段。'></spring:message>" }  ,
	                  expenseType:{ required: "<spring:message code='这是必填字段。'></spring:message>" },
	                  priceAmountDisplay:{ required: "<spring:message code='这是必填字段。'></spring:message>"}
	              },
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("<spring:message code='输入有误，请先更正。'></spring:message>");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						
						error.insertAfter(element);
					}
				}
			});
			
					laydate({
			            elem: '#requestArrivalDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
			            event: 'focus', //响应事件。如果没有传入event，则按照默认的click
			            min: laydate.now(),	
			        });
					laydate({
			            elem: '#actualArrivalDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
			            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
			        });
					
					laydate({
			            elem: '#lastTimeBuySameEquipment', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
			            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
			        });
					
					
					
		});
		
		function uploadFileSubmit(id){
			
				  
			    $.ajaxFileUpload({  
			        url : "${ctx}/oa/pr/import?type="+id,  
			        secureuri : false,// 一般设置为false  
			        fileElementId : "uploadFile" + id,// 文件上传表单的id <input type="file" id="fileUpload" name="file" />  
			        //dataType : 'json',// 返回值类型 一般设置为json  
			        
			        type:"POST",
			        success : function(data) // 服务器成功响应处理函数  
			        {  
			        	console.log(data)
			                },  
			        error : function(data)// 服务器响应失败处理函数  
			        {  
			            console.log("服务器异常");  
			            
			        }  
			    });  
			    return false;  
			
		}
		
		
		function checkfile(id,maxSize){
			
			
			var maxsize = maxSize*1024*1024;//2M
			var errMsg = "<spring:message code='上传的附件文件不能超过'></spring:message>"+maxSize+"M!";
			var tipMsg = "<spring:message code='您的浏览器暂不支持计算上传文件的大小，确保上传文件不要超过'></spring:message>"+maxSize+"M,"+"<spring:message code='建议使用IE、FireFox、Chrome浏览器。'></spring:message>";
			var  browserCfg = {};
			var ua = window.navigator.userAgent;
			if (ua.indexOf("MSIE") >= 0 || ua.indexOf("Edge") >= 0) {
			    browserCfg.ie = true;
			}
			if (!window.ActiveXObject || "ActiveXObject" in window){
			    browserCfg.ie = true;
			}
			if (ua.indexOf("MSIE")>=1){
				browserCfg.ie = true;
			}else if(ua.indexOf("Firefox")>=1){
				browserCfg.firefox = true;
			}else if(ua.indexOf("Chrome")>=1){
				browserCfg.chrome = true;
			}
		
			try{
			 	var obj_file = document.getElementById("uploadFile"+id);
			 
			 	
			 	if(obj_file.value==""){
			 		$("#showSrc"+id).val("");
			 		alert("<spring:message code='请先选择上传文件'></spring:message>");
			 		return;
			 	}
			 	var filesize = 0;
			 	if(browserCfg.firefox || browserCfg.chrome ){
			 		filesize = obj_file.files[0].size;
			 	}else if(browserCfg.ie){
			 		/*var obj_img = document.getElementById('tempimg');
			 		obj_img.dynsrc=obj_file.value;
			 		filesize = obj_img.fileSize;*/
			 		
			 		/*var fso = new ActiveXObject("Scripting.FileSystemObject");			 	    
			 	    filesize = fso.GetFile(obj_file.value).size;
			 	    */
			 	    
			 	   filesize = obj_file.files[0].size;
			 	    
			 	}else{
			 		alert(tipMsg);
			   	return;
			 	}
			 	if(filesize==-1){
			 		alert(tipMsg);
			 		$("#showSrc"+id).val("");
			 		return;
			 	}else if(filesize>=maxsize){
			 		alert(errMsg);
			 		obj_file.value="";
			 		$("#showSrc"+id).val("");
			 		return;
				}else{
					//alert("文件大小符合要求");
					$("#showSrc"+id).val(obj_file.files[0].name);
			 		return;
				}
			}catch(e){
				alert("<spring:message code='非常抱歉, 附件上传仅支持IE 浏览器10+, 谷歌浏览器, 火狐浏览器上进行。'></spring:message>");
				$("#showSrc"+id).val("");
			}
		}
		
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5><spring:message code="采购申请表"></spring:message></h5>
		<div class="ibox-tools">
			<a class="collapse-link">
				<i class="fa fa-chevron-up"></i>
			</a>
			<!-- <a class="dropdown-toggle" data-toggle="dropdown" href="#">
				<i class="fa fa-wrench"></i>
			</a>
			<ul class="dropdown-menu dropdown-user">
				<li><a href="#">选项1</a>
				</li>
				<li><a href="#">选项2</a>
				</li>
			</ul> -->
			<a class="close-link" onclick="history.go(-1)">
				<i class="fa fa-times"></i>
			</a>
		</div>
	</div>
	
	
	<div class="ibox-content">
	
	<form:form id="inputForm" modelAttribute="pr"  action="${ctx}/oa/pr/save" method="post"  class="form-horizontal"  enctype="multipart/form-data">
		<form:hidden path="id"/>
		<form:hidden id="flag" path="act.flag"/>
		<form:hidden path="docuType"/>
		<sys:message hideType="1" content="${message}"/>
		<div style="text-align:center"><font size="6pt">			
			Win Hanverky Group
		</font><br>
			<font size="4pt">
			
			<c:if test="${pr.docuType==1}">
			<spring:message code="采购申请表（IT 设备）"></spring:message>
			</c:if> 
			<c:if test="${pr.docuType==2}">
			<spring:message code="采购申请表（OEM）"></spring:message> 
			</c:if>
			</font></div>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
				    <td class="width-15 active"><label class="pull-right"><font color="red">*</font><spring:message code="部门名称"></spring:message>:</label></td>
					<td colspan=3 class="width-35">
						<form:input path="applyUserDepartmentName" id="applyUserDepartmentName" htmlEscape="false" readonly="true" value="${fns:getUser().office}" class="form-control required"/>
					</td> 
					<!-- <td class="width-15 active"><label class="pull-right"><spring:message code="成本中心编号"></spring:message>:</label></td>
					<td class="width-35">
						
						<form:input path="costCenter" maxlength="50" htmlEscape="false"  class="form-control "/>
					</td> -->
					<td class="width-15 active"><label class="pull-right"><spring:message code="成本中心编号"></spring:message>:</label></td>
					<td >
						<form:select path="costCenter" cssClass="form-control" >
							<form:option value="" label="-"/>
							<form:options items="${fns:getDictListWithKey('cost_center')}" itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select>
					</td>
					
				</tr>
				<tr>	
					<td class="width-15 active"><label class="pull-right"><spring:message code="供应商名称"></spring:message>:</label></td>
					<td  colspan=3  class="width-35">
						<form:input path="supplier" htmlEscape="false"   maxlength="50" class="form-control "/>
					</td> 		
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font><spring:message code="费用类型"></spring:message>:</label></td>
					<td class="width-35">
						<form:select path="expenseType" id="expenseType"  cssClass="form-control input-sm required" >
							<%--<form:option value="" label="--Please select--"/> --%>
							<form:options items="${fns:getDictList('EXPENSE_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select>
					</td>
					
				</tr>
				<tr>
					
					<td class="width-15 active"><label class="pull-right"><spring:message code="请求者姓名"></spring:message>:</label></td>
					<td colspan=3 class="width-35">
						<form:input path="requesterName" htmlEscape="false"  maxlength="50" class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right"><spring:message code="采购地区"></spring:message>:</label></td>
					<td  class="width-35">
					<%--
					<c:if test="${not empty fns:getUser().company}">
						<form:input path="applySiteCode" htmlEscape="false" readonly="true" value="${fns:getUser().company.code}" class="form-control "/>
					</c:if>
					<c:if test="${empty fns:getUser().company}">
						<form:input path="applySiteCode" htmlEscape="false" readonly="true" value="" class="form-control "/>
					</c:if>
					 --%>
					 	<form:select path="applySiteCode" cssClass="form-control required" >
							
							<form:options items="${fns:getDictList('Apply_Site_Code')}" itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select>
						<!--
						<form:select path="applySiteCode"  cssClass="form-control input-sm required" >
							<form:option value="" label="--Please select--"/>
							<form:options items="${fns:getDictList('Apply_Site_Code')}" itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select>
						-->
					</td>
					
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><spring:message code="请求者部门"></spring:message>:</label></td>
					<td colspan=3  class="width-35">
						<form:input path="requestUserDepartment" htmlEscape="false"  maxlength="50" class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right"><spring:message code="和供应商签约方公司"></spring:message>:</label></td>
					<td >
						<form:select path="contractCompany" cssClass="form-control" >
							<form:option value="" label="-"/>
							<form:options items="${fns:getDictListWithKey('all_company')}" itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><spring:message code="入账及使用方公司"></spring:message>:</label></td>
					<td colspan=3  class="width-35">
						
						<form:select path="finalBookCompany" cssClass="form-control" >
							<form:option value="" label="-"/>
							<form:options items="${fns:getDictListWithKey('all_company')}" itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right"><spring:message code="固定资产类别"></spring:message>:</label></td>
					<td >
						<form:select path="assetGroup" cssClass="form-control" >
							<form:option value="" label="-"/>
							<form:options items="${fns:getDictListWithKey('asset_Group')}" itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select>
					</td>
				</tr>
				<tr>
					<td colspan=6><label class="pull-left"><font size="4pt"><spring:message code="具体申购内容"></spring:message></font></label></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font><spring:message code="设备名称"></spring:message>:</label></td>
					<td  colspan=3 class="width-35">
						<form:input path="equipment" id="equipment" htmlEscape="false"  maxlength="50"  class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font><spring:message code="品牌"></spring:message>:</label></td>
					<td class="width-35">
						<form:input path="brand" id="brand" htmlEscape="false"  maxlength="50"   class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><spring:message code="型号"></spring:message>:</label></td>
					<td  colspan=3 class="width-35">
						<form:input path="modelNo" htmlEscape="false"  maxlength="50"   class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right"><spring:message code="要求到货日期"></spring:message>:</label></td>
					<td colspan=3 class="width-35">
						<input id="requestArrivalDate" name="requestArrivalDate" type="text" maxlength="20" class="laydate-icon form-control layer-date "
							value="<fmt:formatDate value="${pr.requestArrivalDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" onchange="validateDate()"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font><spring:message code="单价"></spring:message>:</label></td>
					<td class="width-15">
						<form:input path="unitPrice" id="unitPrice" htmlEscape="false"  onblur="caculateTotal()"  class="form-control required number round"/>
					</td>
					<td class="width-10 active"><label class="pull-right"><font color="red">*</font><spring:message code="币种"></spring:message>:</label></td> 
					<td class="width-15">						
						<form:select path="priceCurrency" cssClass="form-control required" >
							
							<form:options items="${fns:getDictList('PR_CURRENCY')}" itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font><spring:message code="数量"></spring:message>:</label></td>
					<td class="width-35">
						<form:input path="quantity" id="quantity" htmlEscape="false"  onblur="caculateTotal()"  class="form-control required digits"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font><spring:message code="总金额"></spring:message>:</label></td>
					<td  colspan=3 class="width-35">
						<form:input path="" id="priceAmountDisplay"  htmlEscape="false"  readonly="true"  class="form-control required  "/>
						<form:input path="priceAmount"  id="priceAmount"  htmlEscape="false"  readonly="true"  class="hide form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font><spring:message code="描述"></spring:message></label></td>
					<td class="width-35">
						<form:input path="equipmentDesciption" id="equipmentDesciption" htmlEscape="false"  maxlength="50" class="form-control required"/>
					</td>
				</tr>
				<!-- 
				<tr>
					<td class="width-15 active"><label class="pull-right"> Actual arrival date：</label></td>
					<td colspan=3 class="width-35">
						<input id="actualArrivalDate" name="actualArrivalDate" type="text" maxlength="20" class="laydate-icon form-control layer-date "
							value="<fmt:formatDate value="${pr.actualArrivalDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
					</td>
				</tr>
				 -->
				<tr>
					<td colspan=6><label class="pull-left"><font size="4pt"><spring:message code="账务处理"></spring:message></font></label></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><spring:message code="预算"></spring:message>:</label></td>
					<td  colspan=3 class="width-35">
						<%---<form:select path="budgetType"  cssClass="form-control input-sm " >
							<form:option value="" label="--Please select--"/>
							<form:options items="${fns:getDictList('IT_PR_BUDGET')}" itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select> --%>
						<form:radiobuttons cssClass="i-checks "  path="budgetType" items="${fns:getDictList('IT_PR_BUDGET')}" itemLabel="label" itemValue="value" htmlEscape="false" />
					</td>
					<td class="width-15 active"><label class="pull-right"><!--<spring:message code="购买地点"></spring:message>:--></label></td>
					<td class="width-35">
						<!--<form:select path="purchaseLocation"  cssClass="form-control input-sm " >
							<form:option value="" label="--Please select--"/>
							<form:options items="${fns:getDictList('IT_PR_PURCHASE_LOCATION')}" itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select>-->
					</td>
				</tr>
				<%--<tr>
					<td class="width-15 active"><label class="pull-right"><spring:message code="财务承担"></spring:message>:</label></td>
					<td colspan=3 class="width-35">
						<form:select path="paidByFinance"  cssClass="form-control input-sm " >
							<form:option value="" label="--Please select--"/>
							<form:options items="${fns:getDictList('IT_PR_PAID')}" itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select>
					</td>
				</tr> --%>
				<!-- add by j 2017/6/8 begin -->
				<tr>
					<td colspan=6><label class="pull-left"><font size="4pt"><spring:message code="申请原因"></spring:message></font></label></td>
				</tr>
				<tr>
					<td  colspan=6 class="width-15 active"><label class="pull-left"><spring:message code="付款特別事項 (如有)"></spring:message>:</label></td>
					
				</tr>
				
				<tr>
					<td colspan=6 class="width-85">
						<form:textarea path="paymentSpecial" class="form-control "  rows="3" maxlength="1000"/>
					</td>
				</tr>
				
				<tr>
					<td  colspan=6 class="width-15 active"><label class="pull-left"><font color="red">*</font><spring:message code="请详述申请原因，并提供相关的数据"></spring:message>:</label></td>
					
				</tr>
				
				<tr>
					<td colspan=6 class="width-85">
						<form:textarea path="purchasePurpose" id="purchasePurpose" class="form-control required" rows="3" maxlength="1000" htmlEscape="false"/>
					</td>
				</tr>
				<!-- 
				<tr>
					<td  class="width-15 active"><label class="pull-right">The date of last purchase same features of equipment:</label></td>
					<td colspan=5 class="width-45">
						
						<input id="lastTimeBuySameEquipment" name="lastTimeBuySameEquipment" type="text" maxlength="20" class=" laydate-icon form-control layer-date "
							value="<fmt:formatDate value="${pr.lastTimeBuySameEquipment}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
					</td>
				</tr> -->
				<tr>
					<td colspan=6 class="width-15 active"><label class="pull-left"><spring:message code="购买效益及汇报情况"></spring:message>:</label></td>
					
				</tr>
				<tr>
					<td colspan=6 class="width-85">
						<form:textarea path="returnOnInvestment" class="form-control" rows="3" maxlength="1000" htmlEscape="false"/>
					</td>
				</tr>
				<tr>
					<td colspan=6 class="width-15 active"><label class="pull-left"><spring:message code="预算使用情况和余额，如不包括于预算内，请详述原因"></spring:message>:</label></td>
					
				</tr>
				<tr>
					<td colspan=6 class="width-85">
						<form:textarea path="budgetRestSituation" class="form-control" rows="3" maxlength="1000" htmlEscape="false"/>
					</td>
				</tr>
				
				<!-- end -->
			
				<tr>
					<td colspan=6><label class="pull-left"><font size="4pt"><spring:message code="报价单上传"></spring:message>:</font></label>
					<br/><br/><label class="pull-left"><font color="red" size="2pt">(<spring:message code="导入文件不能超过5M"></spring:message>)</font></label></td>					
				</tr>
					<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red"></font><spring:message code='Quotation'></spring:message>1: </label></td>
					<td colspan=5 class="width-15 active">
					<input type='button' value="<spring:message code='选择文件'></spring:message>"  OnClick='javascript:$("#uploadFile1").click();'/><input style="width:80%;border-style: none;background-color: rgba(0,0,0,0);" id='showSrc1' type='text' readonly="readonly"/>
					<input id='uploadFile1' style="display:none" name="file1"  type='file' OnChange='checkfile(1,5)' />
					</td>
					</tr>
					<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red"></font><spring:message code='Quotation'></spring:message>2: </label></td>
					<td colspan=5 class="width-15 active">
					<input type='button' value="<spring:message code='选择文件'></spring:message>"  OnClick='javascript:$("#uploadFile2").click();'/><input style="width:80%;border-style: none;background-color: rgba(0,0,0,0);" id='showSrc2' type='text' readonly="readonly"/>
					<input id='uploadFile2' style="display:none"  name="file2"  type='file' OnChange='checkfile(2,5)' />
					</td>
					</tr>
					<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red"></font><spring:message code='Quotation'></spring:message>3: </label></td>
					<td colspan=5 class="width-15 active">
					<input type='button' value="<spring:message code='选择文件'></spring:message>"  OnClick='javascript:$("#uploadFile3").click();'/><input style="width:80%;border-style: none;background-color: rgba(0,0,0,0);" id='showSrc3' type='text' readonly="readonly"/>
					<input id='uploadFile3' style="display:none" name="file3"  type='file' OnChange='checkfile(3,5)' />
					</td>
					</tr>
					<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red"></font><spring:message code='Remarks/Appendix'></spring:message>: </label></td>
					<td colspan=5 class="width-15 active">
					<div>
						<input type='button' value="<spring:message code='选择文件'></spring:message>"  OnClick='javascript:$("#uploadFile4").click();'/><input style="width:80%;border-style: none;background-color: rgba(0,0,0,0);" id='showSrc4' type='text' readonly="readonly"/>
						<input id='uploadFile4' style="display:none"  name="file"  type='file' OnChange='checkfile(4,5)' />
					</div>
					<input type="hidden" id="Appendix"/>
					<a onclick="newComponentFunction()" ><i class="fa fa-plus"></i><spring:message code='添加'></spring:message></a>					
					
					</td>
					</tr>
				
		 	</tbody>
		</table>
		<img id="tempimg" class="hide" />
		
		<div class="form-actions" style="padding:0;text-align:center;">
		<input id="btnSave" class="btn btn-primary" type="submit" onclick="$('#flag').val(0);" value="<spring:message code='保存'></spring:message>"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input id="btnSubmit" class="btn btn-primary" type="submit" onclick="$('#flag').val(1);" value="<spring:message code='提交'></spring:message>"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input id="btnCancel" class="btn" type="button" value="<spring:message code='取消'></spring:message>" onclick="history.go(-1)"/>
		</div>
	</form:form>
</div>
	</div>
	</div>
</body>
</html>

