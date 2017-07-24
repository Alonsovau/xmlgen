<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- <title>自然人开户</title> -->

<script src="${ctx}/static/js/jquery-1.12.4.js"></script>
<script>

$(document).ready(function(){
	  $("#b01").click(function(){
	  
	  var data = { seqno: $('#seqno').val(), 
			  	   processid: $('#processid').val(),
			  	   processdate: $('#processdate').val(), 
			  	   processtype: $('#processtype').val(),
			  	   clienttype: $('#clienttype').val(), 
			  	   clientregion: $('#clientregion').val(),
			  	   nationality: $('#nationality').val(), 
			  	   excompanyid: $('#excompanyid').val(),
			  	   exclientidtype: $('#exclientidtype').val(), 
			  	   clientname: $('#clientname').val(), 
			  	   idtype: $('#idtype').val(),
			  	   id_transformed: $('#id_transformed').val(),
			  	   classify: $('#classify').val(),
			  	   clientnum: $('#clientnum').val(), 
			  	   option_id: $('#option_id').val() ,
	  			   xmlnum: $('#xmlnum').val() };
	  $.post("/naturalPerson/preview", data,function(data) {
		  	//删除除了行首的所有行，清除上次preview的行数据，只保留表头
		  	$("#preview_table  tr:not(:first)").remove();
		  //循环取返回list的值，并用jquery 操作table插入进去
		  	data.forEach(function(val,index,arr){
		  		var newRow = "<tr><td>"+val['seqno']+"</td><td>"+val['processid']+"</td><td>"+val['processdate']+"</td><td>"
		  		+val['processtype']+"</td><td>"+val['clienttype']+"</td><td>"+val['excompanyid']+"</td><td>"+val['exclientidtype']+"</td><td>"+""+"</td><td>"
		  		+val['clientname']+"</td><td>"+val['clientregion']+"</td><td>"+val['nationality']+"</td><td>"+val['idtype']+"</td><td>"
		  		+val['id_transformed']+"</td><td>"+val['classify']+"</td><td>"+val['option_id']+"</td></tr>";
		  		//在表格最后插入新行
		  		$("#preview_table tr:last").after(newRow);
			})  	
		});
	  });
	});
</script>

</head>
<body>
	<div style="width: 1400px;">
<a href="/">首页</a>
<a href="/naturalPerson">自然人</a>
<a href="/generalCorporate">一般法人</a>
<a href="/specialCorporate">特殊法人</a>
<a href="/assetMgrClient">资管客户</a>
		<style type="text/css">
           .text1 {width: 400px; height: 30px}
        </style>

		<form target="down-iframe" method="post" action="${ctx}/naturalPerson/download">
			<fieldset>
				<table border="1" cellspacing="0" cellpadding="0"
					style="width: 100%">
					<tr>
						<td>序列号：</td>
						<td><input type="text" id="seqno" name="seqno" class="text1" value="0"/></td>
						<td>业务流水号：</td>
						<td><input type="text" id="processid" name="processid" class="text1" placeholder="输入格式0001-W-yymmdd-XXXXXXX"/></td>
					</tr>
					<tr>
						<td>业务类型：</td>
						<td><input type="text" id="processtype" name="processtype" class="text1" readonly="readonly" value="1"/></td>
						<td>流程日期：</td>
						<td><input type="text" id="processdate" name="processdate" class="text1" placeholder="输入格式yyyy-mm-dd"/></td>
					</tr>
					<tr>
						<td>客户类型：</td>
						<td><input type="text" id="clienttype" name="clienttype" readonly="readonly"  class="text1" value="1"/></td>
						<td>客户名称：</td>
						<td><input type="text" id="clientname" name="clientname" class="text1"/></td>
					</tr>
					<tr>
						<td>客户地域：</td>
						   <td> <select name="clientregion" id="clientregion" style="width: 400px; height: 30px"  onchange="check(this)">
    							 <option value="1">1（境内客户）</option>
    							 <option value="2">2（港澳台客户）</option>
    							 <option value="3">3（境外客户）</option>
    							 <option value="4">4（在中国永久居留客）</option>
    							</select>
    					   </td>
						<td>证件类型：</td>
							<td> <select name="idtype" id="idtype" style="width: 400px; height: 30px"  onchange="check(this)">
    							 <option value="1">1（身份证）</option>
    							 <option value="15">15（在中国永久居留证）</option>
    							</select>
    					   </td>	
					</tr>
					<tr>
						<td>国家或地区：</td>
							<td> <select name="nationality" id="nationality" style="width: 400px; height: 30px"  onchange="check(this)">
    							 <option value="CHN">CHN（境内、港澳台）</option>
    							 <option value="GBR">GBR（境外、在中国永久居留客）</option>
    							</select>
    					   </td>
						<td>证件号码：</td>
						<td><input type="text" id="id_transformed" name="id_transformed" class="text1" /></td>
					</tr>
					<tr>
						<td>会员号：</td>
						<td><input type="text" id="excompanyid" name="excompanyid" class="text1" /></td>
						<td>营业部代码：</td>
						<td><input type="text" id="option_id" name="option_id" class="text1" /></td>
					</tr>
					<tr>
						<td>交易类型：</td>
						<td> <select name="exclientidtype" id="exclientidtype" style="width: 400px; height: 30px"  onchange="check(this)">
    							 <option value="1">1（套保）</option>
    							 <option value="2">2（套利）</option>
    							 <option value="3">3（投机）</option>
    							 <option value="4">4（做市商）</option>
    							</select>
    					  </td>
						<td>开户数：</td>
						<td><input type="text" id="clientnum" name="clientnum" class="text1" /></td>
					</tr>
					<tr>
						<td>投资者类型：</td>
						<td><input type="text" id="classify" name="classify" class="text1" readonly="readonly" value="01"/></td>
						<td>xml数：</td>
						<td><input type="text" id="xmlnum" name="xmlnum" class="text1" /></td>
					</tr>
				</table>
			</fieldset>
			<br>	
		   <table style="width: 100%">	
		         <tr>
					<td>
						<button id="b01" type="button" style="width:100px;height:30px;float: right">预览</button>
					</td>					
					<td><button type="submit" value="下载" style="width:100px;height:30px">下载</td>
				</tr>
		   </table>		
       </form>
       	<form action="servlet/UploadServlet" method="post" enctype="multipart/form-data"> 	     
               <div align="center"><br/>
                 <fieldset style="width:90%">
                  <div class='line'>
                   <div align='left' class="leftDiv">替换已有的模版</div>
                    <div align='left' class="rightDiv">
                      <input type="file" name="file1" class="text">
                    </div>
                   </div>
                 </fieldset>
               </div>	
		</form>
		<br> 预览结果： <br>
		<table border = "1" width="100%" id="preview_table">
		<tr>
			<th>seqno</th>
			<th>processid</th>
			<th>processdate</th>
			<th>processtype</th>
			<th>clienttype</th>
			<th>excompanyid</th>
			<th>exclientidtype</th>
			<th>exclientid</th>
			<th>clientname</th>
			<th>clientregion</th>
			<th>nationality</th>
			<th>idtype</th>
			<th>id_transformed</th>
			<th>classify</th>
			<th>option_id</th>
		</tr>	
          
		</table>
		
		<br>
		<br>
		<br>
<%-- 		  <textarea rows="20" cols="200" id="reviewtextarea"><c:out --%>
<%-- 				value=" ${ xml } " escapeXml="true" /></textarea> --%>
		<iframe id="down-iframe" style="display: none"/>
	</div>
</body>
</html>
