
var divNum = 0;
var i = 1+divNum;  //值为已创建div数(包括被删除的)的值+1

//开始加载所有的数据
function editProduct(theIdentefier){
	
	//新增页面的处理情况
	if(theIdentefier=="addProductIdentifier"){
		//把系列类别的数据加载出来
		loadTheSeriesAndCategory();
		return;
	}
	//根据theIndentefier获得id
	var updateRowId = theIdentefier.parentNode.parentNode.firstChild.innerHTML;

	$.post("/FormulaSystem/api/product?action=showlist",
			{"id":updateRowId},
			function(result){
				var data_id = changeUndefined(result.data.list[0].id);
				var data_name = changeUndefined(result.data.list[0].name);
				var data_number = changeUndefined(result.data.list[0].number);
				var data_formula_desc = changeUndefined(result.data.list[0].formula_desc);
				var data_sample_quantity = changeUndefined(result.data.list[0].sample_quantity);
				var data_major_composition = changeUndefined(result.data.list[0].major_composition);
				var data_price = changeUndefined(result.data.list[0].price);
				
				document.getElementById("form_pid").value=data_id;
				document.getElementById("form_name").value=data_name;
				document.getElementById("form_number").value=data_number;
				document.getElementById("form_formula_desc").value=data_formula_desc;
				document.getElementById("form_sample_quantity").value=data_sample_quantity;
				document.getElementById("form_major_composition").value=data_major_composition;
				document.getElementById("form_formulaPrice").value=data_price;
				
				//使用ztree处理下拉框
				dealSeriesCategory(result);
				
			},
			"json");
}

function loadTheSeriesAndCategory(){
	$.post(
			"/FormulaSystem/api/product?action=showSCztree",
			function(result){
				$.fn.zTree.init($("#treeDemo"), setting, result.data);
				setCheck();
				$("#py").bind("change", setCheck);
				$("#sy").bind("change", setCheck);
				$("#pn").bind("change", setCheck);
				$("#sn").bind("change", setCheck);
			},
			"json");
}

//设置该产品属于哪一个配方
function setOption(result){
	//获取option的个数：
	var selectElement = document.getElementById("selectId");
	$(selectElement).val(result.data.list[0].fid);
}

function dealSeriesCategory(result){
	$.fn.zTree.init($("#treeDemo"), setting, result.data.list[0].ztree);
	setCheck();
	$("#py").bind("change", setCheck);
	$("#sy").bind("change", setCheck);
	$("#pn").bind("change", setCheck);
	$("#sn").bind("change", setCheck);
	/*$.fn.zTree.getZTreeObj("treeDemo").expandAll(true);*/
}

var setting = {
	check: {
		enable: true
	},
	data: {
		simpleData: {
			enable: true
		}
	},
	callback: {
		onAsyncSuccess: zTreeOnAsyncSuccess
	}
};

function zTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
	alert("异步加载完了");
    alert(msg);
};

function setCheck() {
	var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
	type = { "Y":"ps", "N": "ps"};
	zTree.setting.check.chkboxType = type;
}

function saveProduct(){
	 //通过判断id是否为空来判定
	var data_id = $("#form_pid").val();
	var data_name = $("#form_name").val();
	var data_number = $("#form_number").val();
	var data_formula_desc = $("#form_formula_desc").val();
	var data_sample_quantity = $("#form_sample_quantity").val();
	var data_major_composition = $("#form_major_composition").val();
	var data_price = $("#form_formulaPrice").val();
	
	
	//判断4个input框是否为空，有其中一个为空return
	if(data_name=="" || data_number==""|| data_formula_desc=="" || data_sample_quantity=="" || data_major_composition=="" || data_price==""){
		alert("输入框不能为空");
		return;
	}
	
	if(!isNumeric(data_sample_quantity)){
		alert("留样量需为非负数");
		return;
	}
	if(!isNumeric(data_price)){
		alert("配方成本需为非负数");
		return;
	}

	var json_product = receive1("treeDemo",data_id,data_name,data_number,data_formula_desc,data_sample_quantity,data_major_composition,data_price);
	if(JSON.parse(json_product).p_series.length === 0){
		alert("产品系列和类别不能为空！");
		return;
	}
	
	if (isEmpty(data_id)) {
		//添加操作
		$.ajax({
			url : "/FormulaSystem/api/product?action=add",
			type : "POST",
			contentType : "application/json",
			dataType : "json",
			data : json_product,
			async : true,
			success : function(result) {
				if(result.code==200){
					alert(result.data);
					/*currentPage = 1;*/
					/*productName = "";
					number = "";
				    formula = "";
					seriesId = "";
					categoryId = "";*/
					showProduct(true);
				
					$('#myModal').modal('hide');
				}else{
					alert(result.data);
				}
				
				
			}
		});
	} else {
		//更新操作
		$.ajax({
			url : "/FormulaSystem/api/product?action=update",
			type : "POST",
			contentType : "application/json",
			dataType : "json",
			data : json_product,
			async : true,
			success : function(result) {
				if(result.code==200){
					alert(result.data);
					/*productName = "";
					number = "";
				    formula = "";
					seriesId = "";
					categoryId = "";*/
					showProduct(true);
				
					$('#myModal').modal('hide');
				}else{
					alert(result.data);
				}
			}
		});
	}  
}

function receive1(ztreeId,theRealId,p_name,p_number,f_descripiton,p_sample_quantity,p_major_composition,p_price) {
	var treeObj = $.fn.zTree.getZTreeObj(ztreeId);
	var nodes = treeObj.getCheckedNodes(true);
	var product = new Object();
	if(!isEmpty(theRealId)){
		product.id = theRealId;
	}
	
	product.name = p_name;
	product.number = p_number;
	product.formula_desc = f_descripiton;
	product.sample_quantity = p_sample_quantity;
	product.major_composition = p_major_composition;
	product.price = p_price;
	
	
	product.p_series = new Array();
	var sid = 0;
	var cid = 0;
	//第一遍循环，找出所有打钩的系列
	for(var i = 0; i < nodes.length; i++) {
		console.log(nodes[i].id);
		if(typeof(nodes[i].sid) != "undefined") {
			product.p_series[sid] = new Object();
			product.p_series[sid].name = nodes[i].name;
			product.p_series[sid].id = nodes[i].id;
			product.p_series[sid].sid = nodes[i].sid;
			sid++;
			cid = 0;
		}
		if(typeof(nodes[i].cid) != "undefined"){
			if(typeof(product.p_series[sid-1].p_catogeris) == "undefined") {
				product.p_series[sid-1].p_catogeris = new Array();
			}
			product.p_series[sid-1].p_catogeris[cid] = new Object();
			product.p_series[sid-1].p_catogeris[cid].name = nodes[i].name;
			product.p_series[sid-1].p_catogeris[cid].cid = nodes[i].cid;
			cid++;
		}
	}
	sid = 0;
	//把cid和sid转化成id，然后将cid和sid置为undefined
	for(var i = 0; i < product.p_series.length; i++){
		product.p_series[i].id = product.p_series[i].sid;
		product.p_series[i].sid = undefined;
		if(typeof(product.p_series[i].p_catogeris) != "undefined")
			for(var j = 0; j < product.p_series[i].p_catogeris.length; j++){
				product.p_series[i].p_catogeris[j].id = product.p_series[i].p_catogeris[j].cid;
				product.p_series[i].p_catogeris[j].cid = undefined;
			}
	}
	return JSON.stringify(product);
}

function isEmpty(str){
	if(str==""||str==undefined){
		return true;
	}else{
		return false;
	}
}

function changeUndefined(str){
	if(str==undefined){
		str = "暂无此数据";
	}
	return str;
}

function openFormulaAddDialog(){
	window.open ('formulaAdd.html','_blank','height=500,width=450,top=300,left=700,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
}

function addDivAndLoadValueOfSeriesSelect(){
	addDiv();
	createFirstSelectOptions(i-1);
	createSecondSelect($("#seriesSelect"+(i-1)).val(),$("#typeSelect"+(i-1)));
}

function deleteDiv(index){
	document.getElementById("totalDiv").removeChild(document.getElementById("div"+index));
}

function resetForm(){
	$("#form_pid").val("");
	$("#form_name").val("");
	$("#form_number").val("");
	$("#form_formula_desc").val("");
	$("#form_sample_quantity").val("");
	$("#form_major_composition").val("");
	$("#form_formulaPrice").val("");
	//产品类别，取消所有checkbox的勾选
	
}

$(function() {
	$('#myModal').on('shown.bs.modal', function() {
		//判断select框的子元素是否为0,为0则关闭 
		
	});
});

$(function() {
	$('#myModal').on('hide.bs.modal', function() {
		resetForm();
	});
});

function isNumeric(str) {
	var reNum = /^\d+(\.{0,1}\d+){0,1}$/;
	if(reNum.test(str)) {
		return true;
	} else {
		return false;
	}
}


