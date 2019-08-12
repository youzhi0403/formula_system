var currentPage;

//用于保存原始加密后的密码变量
var originPassword;

function loadData(userIdentifier){
	//如果是新增按钮跳转的页面，则不执行myFunction函数
	if(userIdentifier=="addUserIdentifier"){
		return;
	}

	var updateRowId = userIdentifier.parentNode.parentNode.firstChild.innerHTML;
	
	$.ajax({
		url : "/FormulaSystem/api/user?action=showlist",
		type : "POST",
		contentType : "application/json",
		dataType : "json",
		data : "{'id':" + updateRowId + "}",
		async : true,
		success : function(result) {
			var data_id = changeUndefined(result.data.list[0].id);
			var account = changeUndefined(result.data.list[0].account);
			var pwd = changeUndefined(result.data.list[0].password);
			var userName = changeUndefined(result.data.list[0].name);
			originPassword = pwd;
			
			document.getElementById("userIdOfModelBox").value=data_id;
			document.getElementById("accountOfModelBox").value=account;
			document.getElementById("pwdOfModelBox").value=pwd;
			document.getElementById("userNameOfModelBox").value=userName;
			setOptions(result);
		}
	});
	
}

function setOptions(result){
	
	cancelSelected();
	//for循环：将sid与select框的option的value值做比较，如果匹配，则设置该option的属性selected，否则，设置第一个option为selected
	var isFlag = false;
	var arr = [];
	for(var i=0;i<result.data.list[0].roles.length;i++){
		arr.push(result.data.list[0].roles[i].id);
	}
	for(var i=0;i<arr.length;i++){
		$("#theSelectIdOfModelBox option").each(function(){
			if($(this).attr("value")==arr[i]){
				$(this).prop("selected",true);
			}
		});
	}
	
}

function save(){
	//通过判断id是否为空来判定
	var userId = $("#userIdOfModelBox").val();
	var account = $("#accountOfModelBox").val();
	var pwd = $("#pwdOfModelBox").val();
	//判断密码是否更改，更改则取md5加密后的密码，否则取原始密码
	pwd = pwd === originPassword ? originPassword : $.md5(pwd);
	var userName = $("#userNameOfModelBox").val();


	//遍历所有的option,获取选中的option的val()
	var theSelect = document.getElementById("theSelectIdOfModelBox");
	var optionElements = theSelect.getElementsByTagName("option");
	//arr用来存储被选中的option的value值
	var arr = [];
	for(var i = 0; i < optionElements.length; i++) {
		if(optionElements[i].selected){
			arr.push(optionElements[i].getAttribute("value"));
		}
	}

	var roleIds = "[";
	for(var i=0;i<arr.length;i++){
		roleIds = roleIds + '{"id":' + arr[i] +'},'
	}
	roleIds = roleIds.substring(0,roleIds.length-1)+"]";

	if(account=="" || pwd=="" || userName==""){
		alert("输入框不能为空");
		return;
	}
	if(arr.length==0){
		alert("至少要选一种角色");
		return;
	}
	
	if(isEmpty(userId)){
		//添加操作
		var action = "save";
		$.ajax({
			url : "/FormulaSystem/api/user?action="+action,
			type : "POST",
			contentType : "application/json",
			dataType : "json",
			data : "{'account':" + account + ",'password':" + pwd
					+ ",'name':" + userName + ",'roles':"
					+ roleIds  + "}",
			async : true,
			success : function(result) {
				if(result.code==200){
					alert(result.data);
					/*currentPage = 1;*/
					/*userName = "";
					account = "";*/
					showUser(true);
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
			url : "/FormulaSystem/api/user?action="+action,
			type : "POST",
			contentType : "application/json",
			dataType : "json",
			data : "{'id':" + userId + ",'account':" + account + ",'password':" + pwd
					+ ",'name':" + userName + ",'roles':"
					+ roleIds  + "}",
			async : true,
			success : function(result) {
				if(result.code==200){
					alert(result.data);
					/*currentPage = 1;*/
					/*userName = "";
					account = "";*/
					showUser(true);
					$('#myModal').modal('hide');
				}else{
					alert(result.data);
				}
				
			}
		});
	}
}

function resetForm(){
	$("#userIdOfModelBox").val("");
	$("#accountOfModelBox").val("");
	$("#pwdOfModelBox").val("");
	$("#userNameOfModelBox").val("");
	//重置的时候应该把select框的option全部设为false
	$("#theSelectIdOfModelBox option").each(function(){
		$(this).prop('selected', false);
	});

}

function isEmpty(str){
	if(str===""||str===undefined){
		return true;
	}else{
		return false;
	}
}


function changeUndefined(str){
	if(str===undefined){
		str = "暂无此数据";
	}
	return str;
}

$(function() {
	$('#myModal').on('hide.bs.modal', function() {
		resetForm();
	})
});

//写一个方法,遍历select框所有的option取消所有的selected
function cancelSelected(){
	$("#theSelectIdOfModelBox option").each(function(){
		$(this).attr('selected', false);
	});
}






