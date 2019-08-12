var currentPage;

function editMaterial(row){
	if(row=="materialAddIdentifier"){
		return;
	}else{
		loadDataOfMaterial(row);
	}
}
//注释
function loadDataOfMaterial(row){
	var arrNodes = row.parentNode.parentNode.childNodes;
	
	var data_id = changeUndefined(arrNodes[0].innerHTML);
	var data_commodity = changeUndefined(arrNodes[2].innerHTML);
	var data_chineseName = changeUndefined(arrNodes[3].innerHTML);
	
	var data_price = changeUndefined(arrNodes[5].innerHTML);
	var data_unit = changeUndefined(arrNodes[6].innerHTML);
	var data_packingWay = changeUndefined(arrNodes[12].innerHTML);
	var data_origin = changeUndefined(arrNodes[7].innerHTML);
	var data_MApparentState = changeUndefined(arrNodes[8].innerHTML);//外观状态
	var data_supplier = changeUndefined(arrNodes[13].firstChild.data);    
	var data_code = changeUndefined(arrNodes[4].innerHTML);
	var data_inci_cn = changeUndefined(arrNodes[10].innerHTML);
	var data_inci_en = changeUndefined(arrNodes[11].innerHTML);
	var data_application = changeUndefined(arrNodes[9].innerHTML);
	var data_sid = changeUndefined(arrNodes[13].lastChild.innerHTML);
	
	document.getElementById("materialIdOfModelBox").value=data_id;
	document.getElementById("nameOfModelBox").value=data_commodity;
	document.getElementById("chineseNameOfModelBox").value=data_chineseName;
	document.getElementById("codeOfModelBox").value=data_code;
	document.getElementById("priceOfModelBox").value=data_price;
	document.getElementById("originOfModelBox").value=data_origin;
	document.getElementById("packingWayOfModelBox").value=data_packingWay;
	document.getElementById("mApparentStateOfModelBox").value=data_MApparentState;
	
	document.getElementById("inciOfModelBox_cn").value=data_inci_cn;
	document.getElementById("inciOfModelBox_en").value=data_inci_en;
	
	document.getElementById("applicationOfModelBox").value=data_application;
	
	//设置原料的单位
	setUnit(data_unit);
	
	//设置供应商
	setOption(data_sid);
}

//设置原料单位的方法
function setUnit(data_unit){
	var theSelect = document.getElementById("unitOfModelBox");
	$(theSelect).val(data_unit);
	
}


//设置供应商
function setOption(data_sid){
	var selectElement = document.getElementById("selectIdOfModelBox");
	$(selectElement).val(data_sid);
	
}

function save(){
	//通过判断id是否为空来判定
	var materialId = $("#materialIdOfModelBox").val();
	var name = $("#nameOfModelBox").val();
	var chineseName = $("#chineseNameOfModelBox").val();
	var price = $("#priceOfModelBox").val();
	var unit = $("#unitOfModelBox").val();
	var origin = $("#originOfModelBox").val();
	var code = $("#codeOfModelBox").val();
	var packingWay = $("#packingWayOfModelBox").val();
	var mApparentState = $("#mApparentStateOfModelBox").val();
	var inciOfCN = $("#inciOfModelBox_cn").val();
	var inciOfEN = $("#inciOfModelBox_en").val();
	var application = $("#applicationOfModelBox").val();
	var supplierId = $("#selectIdOfModelBox option:selected").val(); //获取选中的项

	if(name=="" || price=="" || origin=="" || code=="" || packingWay=="" || mApparentState=="" || inciOfCN=="" || inciOfEN=="" || application==""){
		alert("输入框不能为空");
		return;
	}
	
	if(supplierId=="" || supplierId==""){
		alert("下拉框不能为空");
		return;
	}
	if(!isNum(price)){
		alert("原料价格填写错误");
		return;
	}
	var material = new Object();
	if(isEmpty(materialId)){
		//添加操作
		material.name = name;
		material.chineseName = chineseName;
		material.price = price;
		material.unit = unit;
		material.origin = origin;
		material.sid = supplierId;
		material.packingWay = packingWay;
		material.MApparentState = mApparentState;
		material.code = code;
		material.inci_cn = inciOfCN;
		material.inci_en = inciOfEN;
		material.application = application;
		$.ajax({
			url : "/FormulaSystem/api/material?action=add",
			type : "POST",
			contentType : "application/json",
			dataType : "json",
			data : JSON.stringify(material),
			async : true,
			success : function(result) {
				if(result.code==200){
					alert(result.data);
					
					/*currentPage = 1;*/
					/*name1 = "";
					code = "";
					inci_cn = "";*/
					showMaterial(true);
					$('#myModal').modal('hide');
					
					
				}else{
					alert("添加失败");
				}
			}
		});
	}else{
		//更新操作
		material.id = materialId;
		material.name = name;
		material.chineseName = chineseName;
		material.price = price;
		material.unit = unit;
		material.origin = origin;
		material.sid = supplierId;
		material.packingWay = packingWay;
		material.MApparentState = mApparentState;
		material.code = code;
		material.inci_cn = inciOfCN;
		material.inci_en = inciOfEN;
		material.application = application;
		$.ajax({
			url : "/FormulaSystem/api/material?action=update",
			type : "POST",
			contentType : "application/json",
			dataType : "json",
			data : JSON.stringify(material),
			async : true,
			success : function(result) {
				if(result.code==200){
					alert(result.data);
					
					/*currentPage = 1;*/
					/*name1 = "";
					code = "";
					inci_cn = "";*/
					showMaterial(true);
					$('#myModal').modal('hide');
					
				}else{
					alert(result.data);
				}
			}
		});
	}
}

function isNum(num) {
	var reNum = /^\d+(\.\d+)?$/;
	return(reNum.test(num));
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

function resetForm(){
	
	$("#materialIdOfModelBox").val("");
	$("#nameOfModelBox").val("");
	$("#chineseNameOfModelBox").val("");
	$("#priceOfModelBox").val("");
	//将单位重置为g
	setUnit("克");
	$("#originOfModelBox").val("");
	$("#codeOfModelBox").val("");
	$("#packingWayOfModelBox").val("");
	$("#mApparentStateOfModelBox").val("");
	$("#inciOfModelBox_cn").val("");
	$("#inciOfModelBox_en").val("");
	$("#applicationOfModelBox").val("");
	//将option框设置为第一个
	if(document.getElementById("selectIdOfModelBox").firstChild!=undefined){
		var firstId = document.getElementById("selectIdOfModelBox").firstChild.getAttribute("value");
		/*setOption(firstId);*/
	}
	
}

$(function() {
	$('#myModal').on('hide.bs.modal', function() {
		resetForm();
	});
});

$(function() {
	$('#myModal').on('shown.bs.modal', function() {
		//判断select框的子元素是否为0,为0则关闭 
		if(document.getElementById("selectIdOfModelBox").firstChild==undefined){
			alert("请先添加供应商");
			$('#myModal').modal('hide');
		}
	});
});

