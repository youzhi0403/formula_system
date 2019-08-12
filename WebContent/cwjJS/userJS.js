var addIdentifier = "addUserIdentifier";
// 分页变量
var currentPage = 1; // 当前页
var currentCount = 20; // 当前数据条数
var totalCount; // 数据总条数
var totalPage; // 数据总页数

// 存储权限的变量

var product_showlist;

var formula_showlist;

var material_showlist;

var supplier_showlist;

var system_all;

var user_add;
var user_delete;
var user_update;
var user_showlist;

var role_add;
var role_delete;
var role_update;
var role_showlist;

var productManage_showlist;

// 存储input框的变量
var userName = ""; // 用户昵称
var account = ""; // 用户账户

function welcomeLink() {
	location.href = "welcome.html";
}

function productLink() {
	location.href = "product.html";
}

function materialLink() {
	location.href = "material.html";
}

function supplierLink() {
	location.href = "supplier.html";
}

function formulaLink() {
	location.href = "formula.html";
}

function systemLink() {
	if (user_showlist) {
		location.href = "user.html";
	} else if (!user_showlist && role_showlist) {
		location.href = "role.html";
	} else if (!role_showlist && productManage_showlist) {
		location.href = "productManage.html";
	}
}

function userLink() {
	location.href = "user.html";
}

function roleLink() {
	location.href = "role.html";
}

function productManageLink() {
	location.href = "productManage.html";
}

function exit() {
	var r = confirm("确定退出系统?");
	if (r == true) {
		var action = "EXIT";
		$.post("/FormulaSystem/api/user/login", {
			"action" : action
		}, function(result) {
			if (result.code == 200) {
				alert(result.data);
				location.href = "login.html";
			} else {
				alert("退出失败");
			}
		}, "json");
	}
}

function setAuthorityAndName() {
	// 先显示用户名
	document.getElementById("userName").innerHTML = $.cookie("userName");
	getAuthority(JSON.parse($.cookie("userPrivilege")));
}

function getAuthority(result) {

	product_showlist = result.product_showlist;

	formula_showlist = result.formula_showlist;

	material_showlist = result.material_showlist;

	supplier_showlist = result.supplier_showlist;

	system_all = result.system;

	user_add = result.user_add;
	user_delete = result.user_delete;
	user_update = result.user_update;
	user_showlist = result.user_showlist;

	role_showlist = result.role_showlist;

	productManage_showlist = result.product_SC_showlist;

	// 先判断侧边导航栏的显示权限
	if (!product_showlist) {
		document.getElementById("productPage").style.display = "none";
	}
	if (!formula_showlist) {
		document.getElementById("formulaPage").style.display = "none";
	}
	if (!material_showlist) {
		document.getElementById("materialPage").style.display = "none";
	}
	if (!supplier_showlist) {
		document.getElementById("supplierPage").style.display = "none";
	}
	if (!system_all
			|| (!user_showlist && !role_showlist && !productManage_showlist)) {
		document.getElementById("systemPage").style.display = "none";
	} else {
		if (!user_showlist) {
			document.getElementById("userPage").style.display = "none";
		}
		if (!role_showlist) {
			document.getElementById("rolePage").style.display = "none";
		}
		if (!productManage_showlist) {
			document.getElementById("productManagePage").style.display = "none";
		}
	}
	// 动态生成table的头部
	setHead();
	// 增加新增按钮
	setAddButton();

	showUser(true);
	
	//初始化模态框的下拉框
	createSelect();
}

function setHead() {
	if (!user_delete && !user_update) {
		$("#theadId")
				.append(
						"<tr class='first'><td width='10%'>序号</td><td width='20%'>账户</td><td width='20%' style='display:none'>密码</td><td width='15%'>用户名</td><td width='15%'>用户角色</td></tr>");
	} else {
		$("#theadId")
				.append(
						"<tr class='first'><td width='10%'>序号</td><td width='20%'>账户</td><td width='20%' style='display:none'>密码</td><td width='15%'>用户名</td><td width='15%'>用户角色</td> <td width='20%'>操作</td></tr>");
	}
}

function setAddButton() {
	if (user_add) {
		$("#buttonDiv")
				.append(
						"<input type='button' value='新增' onclick='loadData(addIdentifier)' class='push-button'id='addButton' data-toggle='modal' data-target='#myModal'/>");
	}
}

function showUser(isReloadPagination) {
	//判断当前页是否还有数据，如果没有则回到上一页，在删除记录的时候，会出现这种情况
	if(totalCount > 0 && (totalCount - currentCount*totalPage) == 0){
		currentPage -= 1; 
	}
	
	var json = new Object();
	json.name = userName;
	json.account = account;
	var str = JSON.stringify(json);
	$.ajax({
		url : "/FormulaSystem/api/user?action=showlist&currentPage="
				+ currentPage,
		type : "POST",
		contentType : "application/json",
		dataType : "json",
		data : str,
		async : true,
		success : function(result) {
			// 要删掉之前的table，创建一个新的table：
			removeTbodyContent();

			showData(result);
			if (result.data.totalPage > 0) {
				$($("#page1").parent().parent()).show();
				$("#page1").text(
						result.data.currentPage + "/" + result.data.totalPage);
			} else {
				$($("#page1").parent().parent()).hide();
			}
			if (totalCount !== result.data.totalCount) {
				isReloadPagination = true;
			}
			if (isReloadPagination) {
				totalCount = result.data.totalCount;
				showPagination(result);
			}
		}
	});
}

// 显示分页
function showPagination(user_page) {
	$("#pagination > div").remove();
	$("#pagination").pagination({
		currentPage : user_page.data.currentPage,
		totalPage : user_page.data.totalPage,
		count : 5, // 只显示5页
		callback : function(current) {
			currentPage = current;
			showUser(false);
		}
	});
}

// 移除分页模块的内容
/*
 * function removePageDiv(){ var pageDiv = $("#pageDiv"); pageDiv.remove(); }
 */

// 移除tbody里面的内容
function removeTbodyContent() {
	var tbody = document.getElementById("tbodyId");
	var index = tbody.childNodes.length;
	for (var i = 0; i < index; i++) {
		tbody.removeChild(tbody.childNodes[0]);
	}
}

// 根据页数创建分页模块的内容
function createPageDiv(pageNum) {
	var form_pageDiv = $("#form_pageDiv");
	form_pageDiv.append("<div id='pageDiv'></div>");
	var pageDiv = $("#pageDiv");
	pageDiv
			.append("<span><a href='#' id='firstPage' onclick='jumpByPage(this)' class='push-page'>首页</a></span>");
	pageDiv
			.append("<span><a href='#' id='prePage' onclick='jumpByPage(this)' class='push-page'>上一页</a></span>");
	for (var i = 1; i <= pageNum; i++) {
		pageDiv.append("<span><a href='#' id='" + i
				+ "' onclick='jumpByPage(this)' class='push-page'>" + i
				+ "</a></span>");
	}
	pageDiv
			.append("<span><a href='#' id='nextPage' onclick='jumpByPage(this)' class='push-page'>下一页</a></span>");
	pageDiv
			.append("<span><a href='#' id='lastPage' onclick='jumpByPage(this)' class='push-page'>尾页</a></span>");

}

function createFivePageDiv(pageNum) {
	var form_pageDiv = $("#form_pageDiv");
	form_pageDiv.append("<div id='pageDiv'></div>");
	var pageDiv = $("#pageDiv");
	pageDiv
			.append("<span><a href='#' id='firstPage' onclick='jumpByPage(this)' class='push-page'>首页</a></span>");
	pageDiv
			.append("<span><a href='#' id='prePage' onclick='jumpByPage(this)' class='push-page'>上一页</a></span>");
	// 如果currentPage是1，2的情况
	if (currentPage == 1 || currentPage == 2) {
		for (var i = 1; i <= 5; i++) {
			pageDiv.append("<span><a href='#' id='" + i
					+ "' onclick='jumpByPage(this)' class='push-page'>" + i
					+ "</a></span>");
		}
	} else if (currentPage == pageNum || currentPage == pageNum - 1) {
		for (var i = pageNum - 4; i <= pageNum; i++) {
			pageDiv.append("<span><a href='#' id='" + i
					+ "' onclick='jumpByPage(this)' class='push-page'>" + i
					+ "</a></span>");
		}
	} else {
		for (var i = currentPage - 2; i <= (currentPage + 2); i++) {
			pageDiv.append("<span><a href='#' id='" + i
					+ "' onclick='jumpByPage(this)' class='push-page'>" + i
					+ "</a></span>");
		}
	}

	pageDiv
			.append("<span><a href='#' id='nextPage' onclick='jumpByPage(this)' class='push-page'>下一页</a></span>");
	pageDiv
			.append("<span><a href='#' id='lastPage' onclick='jumpByPage(this)' class='push-page'>尾页</a></span>");
}

function showData(result) {
	var tbody = document.getElementById("tbodyId");

	for (var i = 0; i < result.data.list.length; i++) {
		var row = tbody.insertRow(-1);
		row.setAttribute("class","table-row");
		// 用一个隐藏的列来保存每一条记录的id
		var add1 = row.insertCell(0);
		add1.style.display = "none";

		var add2 = row.insertCell(1);
		var add3 = row.insertCell(2);
		// 隐藏密码这一行
		var add4 = row.insertCell(3);
		add4.style.display = "none";
		var add5 = row.insertCell(4);
		var add6 = row.insertCell(5);

		// 将undefined该成暂无此数据
		var data_id = changeUndefined(result.data.list[i].id);
		var data_account = changeUndefined(result.data.list[i].account);
		var data_pwd = changeUndefined(result.data.list[i].password);
		var data_userName = changeUndefined(result.data.list[i].name);

		add1.innerHTML = data_id;
		add2.innerHTML = i + 1 + (currentPage - 1) * currentCount;
		add3.innerHTML = data_account;
		add4.innerHTML = data_pwd;
		add5.innerHTML = data_userName;
		// add5的话是多个数据，需要进行解析再展示

		if (result.data.list[i].roles == undefined) {
			add6.innerHTML = "暂无此数据";
		} else {
			var data_roles = "";
			for (var j = 0; j < result.data.list[i].roles.length; j++) {
				data_roles = data_roles + result.data.list[i].roles[j].name
						+ "<br/>";
			}
			add6.innerHTML = data_roles;
		}

		
		if (user_update && user_delete) {
			var add7 = row.insertCell(6);
			add7.innerHTML = "<input type='button' class='push-button' value='修改' onclick='loadData(this)' data-toggle='modal' data-target='#myModal'><input type='button' class='push-button' value='删除' onclick='deleteRow(this)'>";
		} else if (!user_update && user_delete) {
			var add7 = row.insertCell(6);
			add7.innerHTML = "<input type='button' class='push-button' value='删除' onclick='deleteRow(this)'>";
		} else if (user_update && !user_delete) {
			var add7 = row.insertCell(6);
			add7.innerHTML = "<input type='button' class='push-button' value='修改' onclick='loadData(this)' data-toggle='modal' data-target='#myModal'>";
		}
	}

}

function deleteRow(deleteBtn){
	var r = confirm("是否删除该账号？");
	if (r == true) {
		// 获取该行的产品id
		var users = new Array();
		var user = new Object();
		users.push(user);
		user.id = $($(deleteBtn).parent().parent().children()[0]).text();
		$.ajax({
			url : "/FormulaSystem/api/user?action=delete",
			type : "POST",
			contentType : "application/json",
			dataType : "json",
			data : JSON.stringify(users),
			async : true,
			success : function(result) {
				if (result.code == 200) {
					// 删除成功后，刷新页面
					totalCount -= 1; //删除一条记录，则totalCount减一
					showUser(true);
				} else {
					alert(result.data);
				}
			}
		});
	}
}

function changeUndefined(str) {
	if (str == undefined) {
		str = "暂无此数据";
	}
	return str;
}

function isEmpty(str) {
	if (str == "" || str == undefined) {
		return true;
	} else {
		return false;
	}
}

function searchByCondition() {
	userName = $("#uName").val();
	account = $("#account").val();
	currentPage = 1;
	showUser(true);
}



function createSelect(){
	//先创建供应商id的下拉框：
	var action = "showNameList";
	$.post(
			
			"/FormulaSystem/api/role",
			{"action":action},
			function(result){
				var theSelect = $("#theSelectIdOfModelBox");
				optionNum = result.data.length;
				for(var i=0;i<optionNum;i++){
					theSelect.append("<option value='"+result.data[i].id+"'>"+result.data[i].name+"</option>");
				}
				
			},
			"json"
	);
}


