function caculateTotal()
		{
			
			if(!isNaN($("#unitPrice").val()) &&!isNaN($("#quantity").val()))
				{
					
					$("#priceAmountDisplay").val(fmoney($("#unitPrice").val() * $("#quantity").val()));
					$("#priceAmount").val($("#unitPrice").val() * $("#quantity").val());
					
				}
			else{
				$("#priceAmountDisplay").val("");
				$("#priceAmount").val("");
				
			}
		}
		
		function fmoney(s, n) { 
			n = n > 0 && n <= 20 ? n : 2; 
			s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + ""; 
			var l = s.split(".")[0].split("").reverse(), r = s.split(".")[1]; 
			t = ""; 
			for (i = 0; i < l.length; i++) { 
			t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : ""); 
			} 
			return t.split("").reverse().join("") + "." + r; 
			} 
		$(document).ready(function(){
			
			  var priceDisplay = $("#priceAmountDisplay").val();
			  if(priceDisplay=="" ||priceDisplay == null ||priceDisplay == undefined)
				  {
				  var priceAmount = $("#priceAmount").val();
				  if(priceAmount!=""&&priceAmount!=null&&priceAmount!=undefined)
					  {
					  $("#priceAmountDisplay").val(fmoney(priceAmount));
					  }
				  
				  }
			});
		
		function validateIsExistQuotation()
		{
			var flag = false;
			for(var i=1;i<=3;i++)
				{
					var obj_file = document.getElementById("uploadFile"+i);
					if(obj_file.value!=""){flag = true;break;}
				}
			return flag;
		}
		function clickValidateQuotation()
		{
			if(!validateIsExistQuotation())
			var result =confirmx('<spring:message code="您的报价单数量不足，请确认是否需要继续 ？"></spring:message>');
			
			return result;
		}		
		
		function refreshTabLocal(dataId)
		{
			console.log(dataId);
			if(!dataId)return;
			 var target = $('.J_iframe[data-id="' + dataId + '"]', window.parent.document);
			    if(!target)return;
			    var url = target.attr('src');
			    //console.log(url);
			    //显示loading提示
			    //var loading = layer.load();
			    target.attr('src', url).load(function () {
			        //关闭loading提示
			       //layer.close(loading);
			    });
		}
		
		
		