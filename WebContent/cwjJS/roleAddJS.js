var currentPage;

function editRole(addRoleIdentifier){
	if(addRoleIdentifier=="addRoleIdentifier"){
		return;
	}
	

	var updateRowId = addRoleIdentifier.parentNode.parentNode.firstChild.innerHTML;

	var action = "showlist";
	$.post("/FormulaSystem/api/role",
			{"id":updateRowId,"action":action},
			function(result){
				var data_id = changeUndefined(result.data.list[0].id);
				var data_name = changeUndefined(result.data.list[0].name);
				var data_price = changeUndefined(result.data.list[0].desc);
				
				document.getElementById("form_roleId").value=data_id;
				document.getElementById("form_roleName").value=data_name;
				document.getElementById("form_roleDesc").value=data_price;

			},
			"json");
	
}

function save(){
	//通过判断id是否为空来判定
	var roleId = $("#form_roleId").val();
	var roleName = $("#form_roleName").val();
	var roleDesc = $("#form_roleDesc").val();

	if(roleName=="" || roleDesc==""){
		alert("输入框不能为空");
		return;
	}
	var addJson = new Object();
	addJson.name = roleName;
	addJson.desc = roleDesc;
	
	var updateJson = new Object();
	updateJson.id = roleId;
	updateJson.name = roleName;
	updateJson.desc = roleDesc;
	
	if(isEmpty(roleId)){
		//添加操作
		var action = "save";
		$.ajax({
			url : "/FormulaSystem/api/role?action="+action,
			type : "POST",
			contentType : "application/json",
			dataType : "json",
			data : JSON.stringify(addJson),
			async : true,
			success : function(result) {
				if(result.code==200){
					alert(result.data);
					
					/*currentPage = 1;*/
					/*roleName = "";*/
					showRole(true);
					$('#myModal').modal('hide');
					
				}else{
					alert(result.data);
				}
				
			}
		});
			
	}
	else{
		//更新操作
		var action = "update";
		$.ajax({
			url : "/FormulaSystem/api/role?action="+action,
			type : "POST",
			contentType : "application/json",
			dataType : "json",
			data : JSON.stringify(updateJson),
			async : true,
			success : function(result) {
				if(result.code==200){
					alert(result.data);
					
					/*currentPage = 1;*/
					/*roleName = "";*/
					showRole(true);
					$('#myModal').modal('hide');
					
				}else{
					alert(result.data);
				}
			}
		});
	}
}

function resetForm(){
	$("#form_roleId").val("");
	$("#form_roleName").val("");
	$("#form_roleDesc").val("");

}

$(function() {
	$('#myModal').on('hide.bs.modal', function() {
		resetForm();
	});
});


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