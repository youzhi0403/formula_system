var code;
var saveId;

var setting = {
	check: {
		enable: true
	},
	data: {
		simpleData: {
			enable: true
		}
	}
};

function setAuthorityOfRole(row){
	var rid = row.parentNode.parentNode.firstChild.innerHTML;
	saveId = rid;
	var action = "showPrivilegeTree";
	$.post(
		"/FormulaSystem/api/role",
		{"action":action,"rid":rid},
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

function setCheck() {
	var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
	type = { "Y":"ps", "N": "s"};
	zTree.setting.check.chkboxType = type;
}

//保存的方法
function saveAuthority(){
	var rid = saveId;
	receive(rid);
}

function receive(rid){
	var treeObj=$.fn.zTree.getZTreeObj("treeDemo");  
    var nodes=treeObj.getCheckedNodes(true);  
	var json = "{\"id\":"+ rid +",\"privileges\":[";
	for(var i=0;i<nodes.length;i++){  
		if(i==nodes.length-1){
			json +="{\"id\":"+ nodes[i].id + "}";
		} else {
			json +="{\"id\":"+ nodes[i].id + "},";
		}
		
		console.log(nodes[i].id, nodes[i].name, nodes[i].checked, nodes[i].open);
	} 
	json += "]}";
	console.log("json:"+json);


	var action = "savePrivileges";
	$.ajax({
		url : "/FormulaSystem/api/role?action="+action,
		type : "POST",
		contentType : "application/json",
		dataType : "json",
		data : json,
		async : true,
		success : function(result) {
			/*alert(result.data);*/
			if(result.code==200){
				alert(result.data);
				$('#myModal_setAuthority').modal('hide');
			}else{
				alert(result.data);
			}
			
		}
	});	
}


