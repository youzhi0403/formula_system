var divNum = 0; //页面的div数量
var i = 1+divNum; //新增div时，i为div的id

var materialInfo;//存储所有原料的变量



function loadData(){
	getMaterialInfo();
	
	var id = window.location.search;
	//如果是新增按钮跳转的页面，则不执行myFunction函数
	if(isEmpty(id)){
		document.getElementById("formulaAddSpan").innerHTML = "添加";
		return;
	}
	document.getElementById("formulaAddSpan").innerHTML = "修改";
	var updateRowId = id.substring(4);
	
	$.post(
		"/FormulaSystem/api/formula/material-name",
		function(result){
			
			$.post(
				"/FormulaSystem/api/formula?action=showlist",
				{"id":updateRowId},
				function(result){
					$("#form_id").val(result.data.list[0].id); 
					$("#form_name").val(result.data.list[0].name);
					$("#form_number").val(result.data.list[0].number);
					$("#form_formulaAmount").val(result.data.list[0].formulaAmount);
					$("#form_technology").val(result.data.list[0].technology);
					$("#form_temperature").val(result.data.list[0].temperature);
					
					//根据配方的原料个数创建div的个数
					createDiv(result,materialInfo);
				},
				"json"
			);
		},
		"json"
	);
}

function getMaterialInfo(){
	$.post(
		"/FormulaSystem/api/formula/material-name",
		function(result){
			materialInfo = result;
		},
		"json"
	);	
}

function createDiv(result,materialResult){
	divNum = result.data.list[0].fmaterial.length;
	i = i+divNum;
	for(var j=1;j<=divNum;j++){
		$("#totalDiv").append("<div id='div"+j+"'><select id='materialSelect"+j+"' onchange='getPrices()'></select><input type='text' id='text"+j+"' onpropertychange='compute(event)' oninput='compute(event)'/><select id='unitSelect"+j+"' onchange='getPrices()'><option value='两'>两</option><option value='克'>克</option><option value='千克'>千克</option><option value='吨'>吨</option></select><input type='button' id='button"+j+"' value='删除' onclick='deleteDiv("+j+")'/></div>");
		
		//初始化option的值：
		var theSelect = $("#materialSelect"+j);
		var optionNum = materialResult.data.length;
		for(var k=0;k<optionNum;k++){
			theSelect.append("<option value='"+materialResult.data[k].id+"'>"+materialResult.data[k].commodity+"</option>");
		}
		
		//设置第一个select(原料下拉框)的值：
		setOption1(result,j);
		//设置第二个select(单位下拉框)的值：
		setOption2(result,j);
		//设置input(单位量)的值：
		setInput(result,j); 
	}
	//点击修改按钮事，把数据load进来后，计算价格
	getPrices();
}

function setOption1(result,k){
	//for循环：将sid与select框的option的value值做比较，如果匹配，则设置该option的属性selected，否则，设置第一个option为selected
	var index = k-1;
	var isFlag = false;
	var optionValue = result.data.list[0].fmaterial[index].mid;
	//获取option的个数：
	var selectElement = document.getElementById("materialSelect"+k);
	var optionElements = selectElement.getElementsByTagName("option");
	for(var j=0;j<optionElements.length;j++){
		//获取到每个option元素
		var optionElement = optionElements[j];
		var temp = optionElement.getAttribute("value");
		
		if(optionValue == temp){
			optionElement.setAttribute("selected","selected");
			isFlag = true;
			break;
		}
	}
	if(isFlag==false){
		//如果找不到匹配的,就把第一个option设为selected
		var optionElement = optionElements[0];
		optionElement.setAttribute("selected","selected");
	}
	
}

function setOption2(result,k){
	var index = k-1;
	var isFlag = false;
	var optionValue = result.data.list[0].fmaterial[index].unit;
	//获取option的个数：
	var selectElement = document.getElementById("unitSelect"+k);
	var optionElements = selectElement.getElementsByTagName("option");
	for(var j=0;j<optionElements.length;j++){
		//获取到每个option元素
		var optionElement = optionElements[j];
		var temp = optionElement.getAttribute("value");
		
		if(optionValue == temp){
			optionElement.setAttribute("selected","selected");
			isFlag = true;
			break;
		}
	}
	if(isFlag==false){
		//如果找不到匹配的,就把第一个option设为selected
		var optionElement = optionElements[0];
		optionElement.setAttribute("selected","selected");
	}
}


function setInput(result,k){
	var index = k-1;
	$("#text"+k).val(result.data.list[0].fmaterial[index].weight);
}

//计算价格的方法
function getPrices(){
	var totalPrice = 0;
	for(var k=1;k<=20;k++){
		if($("#materialSelect"+k).val()==undefined){
			continue;
		}
		//拿到option的id去materialInfo进行匹配，获取价格，之后再和input框的值进行相乘
		//遍历匹配：
		var tempId = $("#materialSelect"+k).val();
		for(var h=0;h<materialInfo.data.length;h++){
			if(tempId == materialInfo.data[h].id){
				if($("#text"+k).val()==""){
					var materialNumber = 0;
				}else{
					materialNumber = parseFloat($("#text"+k).val());
					if(materialNumber<0){
						alert("原料数量不能为负值");
						return;
					}
				}
				
				totalPrice = totalPrice + computePriceWithUnit(materialNumber,$("#unitSelect"+k).val(),materialInfo.data[h]);

			}
		}
	}
	$("#priceText").val(totalPrice);
	
}

//进行单位换算的方法
function computePriceWithUnit(number,selectVal,data){
	var thePrice;
	var price = parseFloat(data.price);
	var inputVal = parseFloat(number);

	if(data.unit=="克" && selectVal =="克"){
		thePrice = inputVal * price;
		
	}else if(data.unit=="克" && selectVal =="千克"){
		thePrice = inputVal * price *1000;
		
	}else if(data.unit=="克" && selectVal =="吨"){
		thePrice = inputVal * price * 1000000;
		
	}else if(data.unit=="千克" && selectVal =="克"){
		thePrice = inputVal * price /1000;
		
	}else if(data.unit=="千克" && selectVal =="千克"){
		thePrice = inputVal * price;
		
	}else if(data.unit=="千克" && selectVal =="吨"){
		thePrice = inputVal * price *1000;
		
	}else if(data.unit=="吨" && selectVal =="克"){
		thePrice = inputVal * price /1000000;
		
	}else if(data.unit=="吨" && selectVal =="千克"){
		thePrice = inputVal * price /1000;
		
	}else if(data.unit=="吨" && selectVal =="吨"){
		thePrice = inputVal * price;
	}else{
		$("#priceText").val("无此单位");
		return;
	}
	return thePrice;
}

function changeUndefined(str){
	if(str==undefined){
		str = "暂无此数据";
	}
	return str;
}


function isEmpty(str){
	if(str==""||str==undefined){
		return true;
	}else{
		return false;
	}
}

function resetForm(){
	$("#form_name").val("");
	$("#form_number").val("");
	$("#form_formulaAmount").val("");
	$("#form_technology").val("");
	$("#form_temperature").val("");
	
	//可以获取最下面框的id，然后作为循环的上界
	for(var j=0;j<(divNum+1);j++){
		if($("#text"+j).val()!=undefined){
			$("#text"+j).val("");
		}	
	}
}

//原料单位的数量input框的监听，当input内的值发生改变的时候，就执行getPrices()方法
function compute(event){
	getPrices();
}

function addDiv(){
	$("#totalDiv").append("<div id='div"+i+"'><select id='materialSelect"+i+"' onchange='getPrices()'></select><input type='text' id='text"+i+"' onpropertychange='compute(event)' oninput='compute(event)'/><select id='unitSelect"+i+"' onchange='getPrices()'><option value='克'>克</option><option value='千克'>千克</option><option value='吨'>吨</option></select><input type='button' id='button"+i+"' value='删除' onclick='deleteDiv("+i+")'/></div>");
	createSelectByI(i);
	i++;
}

//新增的时候创建select框的方法
function createSelectByI(index){
	//先创建原料id的下拉框：
	var result = materialInfo;
	var theSelect = $("#materialSelect"+index);
	var optionNum = result.data.length;
	for(var i=0;i<optionNum;i++){
		theSelect.append("<option value='"+result.data[i].id+"'>"+result.data[i].commodity+"</option>");
	}
}

function deleteDiv(index){
	document.getElementById("totalDiv").removeChild(document.getElementById("div"+index));
}

function openMaterialAddDialog(){
	window.open ('materialAdd.html','_blank','height=500,width=450,top=300,left=700,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
}

function save() {
	//通过判断id是否为空来判定
	var form_id = $("#form_id").val();
	var form_name = $("#form_name").val();
	var form_number = $("#form_number").val();
	var form_formulaAmount = $("#form_formulaAmount").val();
	var form_technology = $("#form_technology").val();
	var form_temperature = $("#form_temperature").val();
	
	//load数据的时候，把原料id设为option的value,把原料的名字设为option的HTML
	var form_fmaterial = "[";
	for(var j=1;j<=20;j++){
		if($("#text"+j).val()!=undefined){
			//拿到option的value和input框的值进行拼接
			form_fmaterial = form_fmaterial + "{"+"mid:"+$("#materialSelect"+j).val()+","+"weight:"+$("#text"+j).val()+","+"unit:"+$("#unitSelect"+j).val()+"},";
		}	
	}
	form_fmaterial = form_fmaterial.substring(0,form_fmaterial.length-1)+"]";

	if (form_name == "" || form_number == "" || form_formulaAmount == ""
			|| form_technology == "" || form_temperature=="") {
		alert("输入框不能为空");
		return;
	}
	
	for(var j=0;j<(divNum+1);j++){
		if($("#text"+j).val()!=undefined){
			if($("#text"+j).val()==""){
				alert("原料输入框不能为空");
				return;
			}
		}	
	}

	if(haveCommonMaterial()){
		alert("原料不能相同");
		return;
	}

	if (isEmpty(form_id)) {
		//添加操作
		$.ajax({
			url : "/FormulaSystem/api/formula/add",
			type : "POST",
			contentType : "application/json",
			dataType : "json",
			data : "{'number':" + form_number + ",'name':" + form_name
					+ ",'formulaAmount':" + form_formulaAmount + ",'technology':"
					+ form_technology + ",'temperature':" + form_temperature
					+ ",'fmaterial':" + form_fmaterial + "}",
			async : true,
			success : function(result) {
				alert(result.data);
			}
		});
		
	} else {
		//更新操作
		$.ajax({
			url : "/FormulaSystem/api/formula/update",
			type : "POST",
			contentType : "application/json",
			dataType : "json",
			data : "{'id':" + form_id + ",'number':" + form_number + ",'name':" + form_name
					+ ",'formulaAmount':" + form_formulaAmount + ",'technology':"
					+ form_technology + ",'temperature':" + form_temperature
					+ ",'fmaterial':" + form_fmaterial + "}",
			async : true,
			success : function(result) {
				alert(result.data);
			}
		});

	}
}

function haveCommonMaterial(){
	var arr = new Array();
	var index = 0;
	//判断select框中option的值是否相同
	//先排序，相邻的数两两比较，如果相同返回false，否则返回true
	for(var k=1;k<=divNum;k++){
		if($("#materialSelect"+k).val()==undefined){
			continue;
		}
		arr[index++] = $("#materialSelect"+k).val();
	}
	
	for(var k=0;k<index;k++){
		for(var h=k+1;h<index;h++){
			if(arr[k]==arr[h]){
				return true;
			}
		}
	}
	
	return false;
}

function resetForm(){
	$("#form_name").val("");
	$("#form_number").val("");
	$("#form_formulaAmount").val("");
	$("#form_technology").val("");
	$("#form_temperature").val("");
	
	//可以获取最下面框的id，然后作为循环的上界
	for(var j=0;j<(divNum+1);j++){
		if($("#text"+j).val()!=undefined){
			$("#text"+j).val("");
		}	
	}
}








