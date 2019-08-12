//分页变量
var currentPage = 1; // 当前页
var currentCount = 20; // 当前数据条数
var totalCount; // 数据总条数
var totalPage; // 数据总页数

// 存储参数的变量
var sId;
var address;
var telephone;
var contact;
var companyName;

// 存储权限的变量

var product_showlist;

var formula_showlist;

var material_showlist;

var supplier_add;
var supplier_delete;
var supplier_update;
var supplier_showlist;

var system_all;

var user_showlist;

var role_showlist;

var productManage_showlist;

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

	supplier_add = result.supplier_add;
	supplier_update = result.supplier_update;
	supplier_delete = result.supplier_delete;
	supplier_showlist = result.supplier_showlist;

	system_all = result.system;

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
	}
	/*
	 * else{ if(!user_showlist){
	 * document.getElementById("userPage").style.display = "none"; }
	 * if(!role_showlist){ document.getElementById("rolePage").style.display =
	 * "none"; } if(!productManage_showlist){
	 * document.getElementById("productManagePage").style.display = "none"; } }
	 */
	// 动态生成table的头部
	setHead();
	// 增加新增按钮
	setAddButton();

	showSupplier(true);
}

function setHead() {
	// 权限判断生成表头
	if (!supplier_delete && !supplier_update) {
		$("#theadId")
				.append(
						"<tr class='first'><td width='4%'>序号</td><td width='12%'>联系人</td><td width='20%'>公司名称</td><td width='25%'>地址</td><td width='10%'>电话</td></tr>");
	} else {
		$("#theadId")
				.append(
						"<tr class='first'><td width='4%'>序号</td><td width='12%'>联系人</td><td width='20%'>公司名称</td><td width='25%'>地址</td><td width='10%'>电话</td><td width='10%'>操作</td></tr>");
	}
}

function setAddButton() {
	if (supplier_add) {
		$("#buttonDiv")
				.append(
						"<input type='button' value='新增' class='push-button' id='addButton' data-toggle='modal' data-target='#myModal'/><input type='button' onclick='downloadTemplate()' class='push-button' value='导入模板下载' style='float:right'><div style='float:right;margin-left:5px;'><input id='upload' class='leading-in' type='button' value='导入'/></div>");
		// 创建导入按钮的方法。
		$("#upload").upload({
			name : 'file', // <input name="file" />
			action : '/FormulaSystem/api/supplier?action=import', // 提交请求action路径
			enctype : 'multipart/form-data', // 编码格式
			onComplete : function(response) {// 请求完成时 调用函数
				if (JSON.parse(response).code == 200) {
					alert(JSON.parse(response).data);
					showSupplier(true);
				} else {
					alert("导入失败，" + JSON.parse(response).data);
				}
			}
		});
	}
}
function downloadTemplate(){
	window.location.href = "/FormulaSystem/api/supplier?action=template";
}

function showSupplier(isReloadPagination) {
	//判断当前页是否还有数据，如果没有则回到上一页，在删除记录的时候，会出现这种情况
	if(totalCount > 0 && (totalCount - currentCount*totalPage) == 0){
		currentPage -= 1; 
	}

	$.post("/FormulaSystem/api/supplier/showlist", {
		"id" : sId,
		"address" : address,
		"telephone" : telephone,
		"contact" : contact,
		"companyName" : companyName,
		"currentPage" : currentPage
	}, function(result) {
		// 要删掉之前的table，创建一个新的table：
		removeTbodyContent();
		showDataWithNotId(result);
		if (result.data.totalPage > 0){
			$($("#page1").parent().parent()).show();
			$("#page1").text(
					result.data.currentPage + "/" + result.data.totalPage);
		} else{
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
function showPagination(supllier_page) {
	// 将分页插件生成的分页元素全部删除，然后重写生成新的分页元素（解决分页插件不能二次初始化的bug）
	$("#pagination > div").remove();
	$("#pagination").pagination({
		currentPage : supllier_page.data.currentPage,
		totalPage : supllier_page.data.totalPage,
		count : 5, // 只显示5页
		callback : function(current) {
			currentPage = current;
			showSupplier(false);
		}
	});
}

// 移除tbody里面的内容
function removeTbodyContent() {
	var tbody = document.getElementById("tbodyId");
	var index = tbody.childNodes.length;
	for (var i = 0; i < index; i++) {
		tbody.removeChild(tbody.childNodes[0]);
	}
}

// 移除分页模块的内容
/*
 * function removePageDiv(){ var pageDiv = $("#pageDiv"); pageDiv.remove(); }
 */

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

function showDataWithId(result) {
	var row = tbody.insertRow(-1);
	row.setAttribute("class","table-row");
	// 用一个隐藏的列来保存每一条记录的id
	var add1 = row.insertCell(0);
	add1.style.display = "none";

	var add2 = row.insertCell(1);
	var add3 = row.insertCell(2);
	var add4 = row.insertCell(3);
	var add5 = row.insertCell(4);
	var add6 = row.insertCell(5);

	add1.innerHTML = result.data.list[0].id;
	add2.innerHTML = 1 + (currentPage - 1) * currentCount;
	add3.innerHTML = result.data.list[0].contact;
	add4.innerHTML = result.data.list[0].companyName;
	add5.innerHTML = result.data.list[0].address;
	add6.innerHTML = result.data.list[0].telephone;

	//修改 删除
	if (supplier_update && supplier_delete) {
		var add7 = row.insertCell(6);
		add7.innerHTML = "<input type='button' class='push-button' value='修改' data-toggle='modal' data-target='#myModal' onclick='editSupplier(this)'><input type='button' value='删除' class='push-button' onclick='deleteRow(this)' >";
	} else if (!supplier_update && supplier_delete) {
		var add7 = row.insertCell(6);
		add7.innerHTML = "<input type='button' class='push-button' value='删除' onclick='deleteRow(this)'>";
	} else if (supplier_update && !supplier_delete) {
		var add7 = row.insertCell(6);
		add7.innerHTML = "<input type='button' class='push-button' value='修改' data-toggle='modal' data-target='#myModal' onclick='editSupplier(this)'>";
	}

}

function showDataWithNotId(result) {
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
		var add5 = row.insertCell(4);
		var add6 = row.insertCell(5);

		add1.innerHTML = result.data.list[i].id;
		//当前数据条数 + 1 + 之前的条数（之前页数的总条数）
		add2.innerHTML = i + 1 + (currentPage - 1) * currentCount;
		add3.innerHTML = result.data.list[i].contact;
		add4.innerHTML = result.data.list[i].companyName;
		add5.innerHTML = result.data.list[i].address;
		add6.innerHTML = result.data.list[i].telephone;

		if (supplier_update && supplier_delete) {
			var add7 = row.insertCell(6);
			add7.innerHTML = "<input type='button' class='push-button' value='修改' data-toggle='modal' data-target='#myModal' onclick='editSupplier(this)'><input type='button' value='删除' class='push-button' onclick='deleteRow(this)' >";
		} else if (!supplier_update && supplier_delete) {
			var add7 = row.insertCell(6);
			add7.innerHTML = "<input type='button' class='push-button' value='删除' onclick='deleteRow(this)'>";
		} else if (supplier_update && !supplier_delete) {
			var add7 = row.insertCell(6);
			add7.innerHTML = "<input type='button' class='push-button' value='修改' data-toggle='modal' data-target='#myModal' onclick='editSupplier(this)'>";
		}

	}

}

function deleteRow(deleteBtn) {
	var r = confirm("是否删除该供应商？");
	if (r == true) {
		// 获取该行的产品id
		var supplier = new Object();
		supplier.id = $($(deleteBtn).parent().parent().children()[0]).text();
		$.ajax({
			url : "/FormulaSystem/api/supplier/delete",
			type : "POST",
			contentType : "application/x-www-form-urlencoded",
			dataType : "json",
			data : supplier,
			async : true,
			success : function(result) {
				if (result.code == 200) {
					// 删除成功后，刷新页面
					totalCount -= 1; //删除一条记录，则totalCount减一
					showSupplier(true);
				} else {
					alert(result.data);
				}
			}
		});
	}
}

function isEmpty(str) {
	if (str == "" || str == undefined) {
		return true;
	} else {
		return false;
	}
}

function searchByCondition() {
	sId = $("#supplierId").val();
	address = $("#address").val();
	contact = $("#contact").val();
	telephone = $("#telephone").val();
	companyName = $("#companyName").val();

	currentPage = 1;
	showSupplier(true);
}

// 分页跳转方法
function jumpByPage(a) {
	var idValue = a.getAttribute("id");
	// currentPage的值可能是undefined，
	if (idValue == 'firstPage') {
		currentPage = 1;
	} else if (idValue == 'lastPage') {
		currentPage = totalPage;
	} else if (idValue == 'nextPage') {
		if (currentPage == totalPage) {
			currentPage = totalPage;
		} else {
			// currentPage = currentPage+1 1被解析为字符串，整型+字符串类型=字符串类型，所以报错
			currentPage = currentPage + 1;
		}
	} else if (idValue == 'prePage') {
		if (currentPage == 1 || isEmpty(currentPage)) {
			currentPage = 1;
		} else {
			currentPage = currentPage - 1;
		}
	} else {
		currentPage = parseInt(idValue);
	}

	showSupplier(false);
}





