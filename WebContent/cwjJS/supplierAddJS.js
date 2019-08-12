//更新添加之后都show第一页
var currentPage;

function editSupplier(row){
	var arrNodes = row.parentNode.parentNode.childNodes;
	document.getElementById("form_id").value=arrNodes[0].innerHTML;
	document.getElementById("form_contact").value=arrNodes[2].innerHTML;
	document.getElementById("form_company").value=arrNodes[3].innerHTML;
	document.getElementById("form_address").value=arrNodes[4].innerHTML;
	document.getElementById("form_telephone").value=arrNodes[5].innerHTML;
}

//保存数据
function save(){
	//通过判断id是否为空来判定
	var form_id = $("#form_id").val();
	var form_contact = $("#form_contact").val();
	var form_telephone = $("#form_telephone").val();
	var form_address = $("#form_address").val();
	var form_company = $("#form_company").val();

	//判断4个input框是否为空，有其中一个为空return
	if(form_contact=="" || form_telephone=="" || form_address=="" || form_company==""){
		alert("输入框不能为空");
		return;
	}
	
	if(isEmpty(form_id)){
		//添加操作
		$.post(
			"/FormulaSystem/api/supplier/add",
			{"contact":form_contact,"telephone":form_telephone,"address":form_address,"companyName":form_company},
			function(result){
				if(result.code==200){
					alert(result.data);
					
					/*currentPage = 1;*/
					/*sId = "";
					address = "";
					telephone = "";
					contact = "";
					companyName = "";*/
					showSupplier(true);
					
					$('#myModal').modal('hide');
					
				}else{
					alert("添加失败");
				}
			},
			"json"
		);
	}
	else{
		//更新操作
		$.post(
			"/FormulaSystem/api/supplier/update",
			{"id":form_id,"contact":form_contact,"telephone":form_telephone,"address":form_address,"companyName":form_company},
			function(result){
				if(result.code==200){
					alert(result.data);
					
					/*currentPage = 1;*/
					/*sId = "";
					address = "";
					telephone = "";
					contact = "";
					companyName = "";*/
					showSupplier(true);
					
					$('#myModal').modal('hide');
				}else{
					alert("更新失败");
				}
			},
			"json"
		);
	}
}

//重置表单
function resetForm(){
	$("#form_id").val("");
	$("#form_contact").val("");
	$("#form_telephone").val("");
	$("#form_address").val("");
	$("#form_company").val("");
}

$(function() {
	$('#myModal').on('hide.bs.modal', function() {
		resetForm();
	})
});

//判断变量是否为空
function isEmpty(str){
	if(str==""||str==undefined){
		return true;
	}else{
		return false;
	}
}

