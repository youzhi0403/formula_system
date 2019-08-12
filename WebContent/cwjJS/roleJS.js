var addRoleIdentifier = "addRoleIdentifier";

//分页变量
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

var user_showlist;

var role_add;
var role_delete;
var role_update;
var role_showlist;
var role_privilege_setting;

var productManage_showlist;

// 存储input框的变量
var roleName; // 用户昵称

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

	user_showlist = result.user_showlist;

	role_add = result.role_add;
	role_delete = result.role_delete;
	role_update = result.role_update;
	role_showlist = result.role_showlist;
	role_privilege_setting = result.role_privilege_setting;

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
	showRole(true);
}

function setHead() {
	if (!role_delete && !role_update && !role_privilege_setting) {
		$("#theadId")
				.append(
						"<tr class='first'><td width='10%'>序号</td><td width='20%'>角色名字</td><td width='30%'>角色描述</td></tr>");
	} else {
		$("#theadId")
				.append(
						"<tr class='first'><td width='10%'>序号</td><td width='20%'>角色名字</td><td width='30%'>角色描述</td><td width='20%'>操作</td></tr>");
	}

}

function setAddButton() {
	if (role_add) {
		$("#buttonDiv")
				.append(
						"<input type='button' value='新增' data-toggle='modal' data-target='#myModal' onclick='editRole(addRoleIdentifier)' class='push-button' id='addButton'/>");
	}
}

function showRole(isReloadPagination) {
	//判断当前页是否还有数据，如果没有则回到上一页，在删除记录的时候，会出现这种情况
	if(totalCount > 0 && (totalCount - currentCount*totalPage) == 0){
		currentPage -= 1; 
	}
	
	var action = "showlist";
	$.post("/FormulaSystem/api/role", {
		"name" : roleName,
		"currentPage" : currentPage,
		"action" : action
	}, function(result) {
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
	}, "json");
}

// 显示分页
function showPagination(role_page) {
	$("#pagination > div").remove();
	$("#pagination").pagination({
		currentPage : role_page.data.currentPage,
		totalPage : role_page.data.totalPage,
		count : 5, // 只显示5页
		callback : function(current) {
			currentPage = current;
			(false);
		}
	});
}

// 移除分页模块的内容
function removePageDiv() {
	var pageDiv = $("#pageDiv");
	pageDiv.remove();
}

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
		var add4 = row.insertCell(3);

		// 将undefined该成暂无此数据
		var data_id = changeUndefined(result.data.list[i].id);
		var data_name = changeUndefined(result.data.list[i].name);
		var data_description = changeUndefined(result.data.list[i].desc);

		add1.innerHTML = data_id;
		add2.innerHTML = i + 1 + (currentPage - 1) * currentCount;
		add3.innerHTML = data_name;
		add4.innerHTML = data_description;

		
		console.log(role_delete);
		if (role_update && role_delete) {
			var add5 = row.insertCell(4);
			add5.innerHTML = "<input type='button' class='push-button' value='修改' data-toggle='modal' data-target='#myModal' onclick='editRole(this)'><input type='button' class='push-button' value='删除' onclick='deleteRow(this)'>";
			// 添加设置权限按钮
			if (role_privilege_setting) {
				add5.innerHTML = add5.innerHTML
						+ "<input type='button' class='push-button' value='设置权限'  data-toggle='modal' data-target='#myModal_setAuthority' onclick='setAuthorityOfRole(this)'/>";
			}

		} else if (!role_update && role_delete) {
			var add5 = row.insertCell(4);
			add5.innerHTML = "<input type='button' class='push-button' value='删除' onclick='deleteRow(this)'>";
			// 添加设置权限按钮
			if (role_privilege_setting) {
				add5.innerHTML = add5.innerHTML
						+ "<input type='button' class='push-button' value='设置权限'  data-toggle='modal' data-target='#myModal_setAuthority' onclick='setAuthorityOfRole(this)'>";
			}
		} else if (role_update && !role_delete) {
			var add5 = row.insertCell(4);
			add5.innerHTML = "<input type='button' class='push-button' value='修改' data-toggle='modal' data-target='#myModal' onclick='editRole(this)'>";
			// 添加设置权限按钮
			if (role_privilege_setting) {
				add5.innerHTML = add5.innerHTML
						+ "<input type='button' class='push-button' value='设置权限' data-toggle='modal' data-target='#myModal_setAuthority' onclick='setAuthorityOfRole(this)'>";
			}
		} else if (!role_update && !role_delete) {
			var add5 = row.insertCell(4);
			if (role_privilege_setting) {
				add5.innerHTML = "<input type='button' class='push-button' value='设置权限' data-toggle='modal' data-target='#myModal_setAuthority' onclick='setAuthorityOfRole(this)'";
			}
		}

	}

}

function deleteRow(deleteBtn){
	var r = confirm("是否删除该角色？");
	if (r == true) {
		// 获取该行的产品id
		var roles = new Array();
		var role = new Object();
		roles.push(role);
		role.id = $($(deleteBtn).parent().parent().children()[0]).text();
		$.ajax({
			url : "/FormulaSystem/api/role?action=delete",
			type : "POST",
			contentType : "application/json",
			dataType : "json",
			data : JSON.stringify(roles),
			async : true,
			success : function(result) {
				if (result.code == 200) {
					// 删除成功后，刷新页面
					totalCount -= 1; //删除一条记录，则totalCount减一
					showRole(true);
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
	roleName = $("#roleName").val();
	currentPage = 1;
	showRole(true);
}

