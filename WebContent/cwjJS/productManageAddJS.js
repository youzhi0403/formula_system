var divNum = 0;   //div的总数
var i = 1+divNum; //下标
function editProductManage(addIdentifier){
	if(addIdentifier=="productAddIdentifier"){
		return;
	}
	
	var updateRowId = addIdentifier.parentNode.parentNode.firstChild.innerHTML;
	
	$.ajax({
		url : "/FormulaSystem/api/product?action=showSC",
		type : "POST",
		contentType : "application/json",
		dataType : "json",
		data : "{\"id\":"+updateRowId+"}",
		async : true,
		success : function(result) {
			
			var data_id = changeUndefined(result.data.id);
			var data_name = changeUndefined(result.data.s_name);
			
			document.getElementById("form_pid").value=data_id;
			document.getElementById("form_name").value=data_name;
			
			for(var j=0;j<result.data.p_catogeris.length;j++){
				
				addDiv();
				$("#text"+(j+1)).val(result.data.p_catogeris[j].name);
				
			}
		}
	});
}

function addDiv(){
	$("#totalDiv").append("<div id='div"+i+"'><input type='text' id='text"+i+"'/><input type='button' id='button"+i+"' value='删除' onclick='deleteDiv("+i+")'/></div>");
	i++;
}

function deleteDiv(index){
	document.getElementById("totalDiv").removeChild(document.getElementById("div"+index));
}

function save() {
	//通过判断id是否为空来判定
	var form_seriesId = $("#form_pid").val();
	var form_seriesName = $("#form_name").val();
	var arr = [];
	for(var j=1;j<i;j++){
		if($("#text"+j).val()==undefined){
			continue;
		}
		arr.push($("#text"+j).val());
	}

	//判断输入框是否为空
	if (form_seriesName == "" ) {
		alert("输入框不能为空");
		return;
	}
	for(var j=1;j<i;j++){
		if($("#text"+j).val()==undefined){
			continue;
		}
		if ($("#text"+j).val() == "" ) {
			alert("输入框不能为空");
			return;
		}
	}
	//判断系列框是否相同
	if(isCommom()){
		alert("系列不可以相同");
		return;
	}
	
	var form_typeNames = "[";
	for(var j=0;j<arr.length;j++){
		if(j==arr.length-1){
			form_typeNames += "{\"name\":"+arr[j] +"}";
		}else{
			form_typeNames += "{\"name\":"+arr[j] +"},";
		}
		
	}

	form_typeNames += "]";
	if (isEmpty(form_seriesId)) {
		//添加操作
		$.ajax({
			url : "/FormulaSystem/api/product?action=addSC",
			type : "POST",
			contentType : "application/json",
			dataType : "json",
			data : "{\"s_name\":" + form_seriesName + ",\"p_catogeris\":" + form_typeNames  + "}",
			async : true,
			success : function(result) {
				if(result.code==200){
					alert(result.data);
					showSeriesAndCategory();
					$('#myModal').modal('hide');
				}else{
					alert(result.data);
				}
				
			}
		});
		
	} else {
		//更新操作
		$.ajax({
			url : "/FormulaSystem/api/product?action=updateSC",
			type : "POST",
			contentType : "application/json",
			dataType : "json",
			data : "{\"id\":" + form_seriesId + ",\"s_name\":" + form_seriesName + ",\"p_catogeris\":" + form_typeNames  + "}",
			async : true,
			success : function(result) {
				if(result.code==200){
					alert(result.data);
					showSeriesAndCategory();
					$('#myModal').modal('hide');
				}else{
					alert(result.data);
				}
			}
		});

	}
}

function isCommom(){
	var arr = new Array();
	var index = 0;
	//判断select框中option的值是否相同
	//先排序，相邻的数两两比较，如果相同返回false，否则返回true
	for(var k=1;k<i;k++){
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

//判断变量是否为空
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
	$("#form_pid").val("");
	$("#form_name").val("");
	
	//remove掉totalDiv的所有内容，将i置1
	$("#totalDiv div").each(function(){
		$(this).remove();
	});
	i = 1;
}

$(function() {
	$('#myModal').on('hide.bs.modal', function() {
		resetForm();
	});
});

