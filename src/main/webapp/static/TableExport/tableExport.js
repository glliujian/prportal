var idTmr;
        function  getExplorer() {
            var explorer = window.navigator.userAgent ;
            
            //ie|| explorer.indexOf("Edge")>=0
            if (explorer.indexOf("MSIE") >= 0 || explorer.indexOf("Edge") >= 0) {
                return 'ie';
            }
            if (!!window.ActiveXObject || "ActiveXObject" in window)
                return 'ie';
            //firefox
            else if (explorer.indexOf("Firefox") >= 0) {
                return 'Firefox';
            }
            //Chrome
            else if(explorer.indexOf("Chrome") >= 0){
                return 'Chrome';
            }
            //Opera
            else if(explorer.indexOf("Opera") >= 0){
                return 'Opera';
            }
            //Safari
            else if(explorer.indexOf("Safari") >= 0){
                return 'Safari';
            }
        }
        
        function stringConvertToDom(str){
        	
        	var objE = document.createElement("div");
        	
     	　　 objE.innerHTML = str;

     	　　 return objE;
        }
        function ExportToExcelFormat(tableid) {
        	var table = ConvertTable(tableid);
        	console.log(table)
            if(getExplorer()=='ie')
            {           	
            	 //var curTbl = document.getElementById(tableid[0]);
            	 //console.log(curTbl)
                 var oXL = new ActiveXObject("Excel.Application");
                 var curTbl = stringConvertToDom(table);
                 console.log(curTbl.childNodes[0])
                 //创建AX对象excel
                 var oWB = oXL.Workbooks.Add();
                 //获取workbook对象
                 var xlsheet = oWB.Worksheets(1);
                 //激活当前sheet
                 var sel = document.body.createTextRange();
                 document.getElementById("inputForm").appendChild(curTbl);
                 sel.moveToElementText(curTbl);
                 
                 //把表格中的内容移到TextRange中
                 sel.select;
                 //全选TextRange中内容
                 sel.execCommand("Copy");
                 //复制TextRange中内容
                 xlsheet.Paste();
                 //粘贴到活动的EXCEL中
                 oXL.Visible = true;
                 //设置excel可见属性
                 document.getElementById("inputForm").removeChild(curTbl);
                 try {
                     var fname = oXL.Application.GetSaveAsFilename("Excel.xls", "Excel Spreadsheets (*.xls), *.xls");
                 } catch (e) {
                     print("Nested catch caught " + e);
                 } finally {
                     oWB.SaveAs(fname);

                     oWB.Close(savechanges = false);
                     //xls.visible = false;
                     oXL.Quit();
                     oXL = null;
                     //结束excel进程，退出完成
                     //window.setInterval("Cleanup();",1);
                     idTmr = window.setInterval("Cleanup();", 1);

                 }

             }
            else
            {
                //tableToExcel(tableid)
            	tableToExcel(table);
            }
        }
        function Cleanup() {
            window.clearInterval(idTmr);
            CollectGarbage();
        }
        var tableToExcel = (function() {  
            var uri = 'data:application/vnd.ms-excel;base64,',  
                    template = '<html><head><meta charset="UTF-8"></head><body>{table}</body></html>',  
                    base64 = function(s) { return window.btoa(unescape(encodeURIComponent(s))) },  
                    format = function(s, c) {  
                        return s.replace(/{(\w+)}/g,  
                                function(m, p) { return c[p]; }) }  
            return function(table, name) {  
                //if (!table.nodeType) table = document.getElementById(table)  
                var ctx = {worksheet: name || 'Worksheet', table: table}  
                window.location.href = uri + base64(format(template, ctx))  
            }  
        })()  
        
        function ConvertTable(tableid)
        {
        	var result="";
        	for(var j=0;j<tableid.length;j++)
        	{
        		var tableExcel = $("#"+tableid[j]).clone()
            	var td = tableExcel.find("td");
            	  
            	for(var i=0;i<td.length;i++)
            	{
            	  if(td[i].children.length>0)
            	  {        	      
            	      var nodeName = td[i].children[0].nodeName; 
            	      var node = td[i].children[0];    
            	      
            	      switch(nodeName.toLocaleUpperCase())
            	       {
            	          case "INPUT":        	           
            	           td[i].innerHTML = node.value
            	           break;
            	          case "LABEL":
            	            td[i].innerHTML = node.innerHTML  
            	        	  break;
            	          case "SELECT":
            	        	  td[i].innerHTML = $(node).find("option:selected").text();
            	        	  break;
            	          case "TEXTAREA":
            	        	  td[i].innerHTML =  node.innerHTML;
            	        	  break;
            	       }
            	  }
            	  
            	}
            	if(tableExcel[0])
            	result = result + "<table id='tabletest"+j+"' border=1>"+tableExcel[0].innerHTML+"</table><table><tr><td></td></tr></table>"
            	
        	}
        	
        	return result;
        	
        }
        
       