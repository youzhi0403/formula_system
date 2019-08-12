//分页变量
var currentPage = 1; // 当前页
var currentCount = 20; // 当前数据条数
var totalCount; // 数据总条数
var totalPage; // 数据总页数

// 存储参数的变量
var f_name;
var f_number;
var id;
var p_name;
var p_code;

// 存储权限的变量

var product_showlist;

var formula_import;
var formula_export;
var formula_showlist;
var formula_delete;

var material_showlist;

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
	document.getElementById("userName").innerHTML = $.cookie("userName");
	// 获得该用户的权限
	getAuthority(JSON.parse($.cookie("userPrivilege")));
}

function getAuthority(result) {
	product_showlist = result.product_showlist;

	formula_import = result.formula_import;
	formula_export = result.formula_export;
	formula_showlist = result.formula_showlist;
	formula_delete = result.formula_delete;

	material_showlist = result.material_showlist;

	supplier_showlist = result.supplier_showlist;

	system_all = result.system;

	user_showlist = result.user_showlist;

	role_showlist = result.role_showlist;

	productManage_showlist = result.productManage_showlist;

	// 先判断侧边导航栏的显示权限
	if (!formula_showlist) {
		document.getElementById("formulaPage").style.display = "none";
	}
	if (!material_showlist) {
		document.getElementById("materialPage").style.display = "none";
	}
	if (!supplier_showlist) {
		document.getElementById("supplierPage").style.display = "none";
	}
	if (!product_showlist) {
		document.getElementById("productPage").style.display = "none";
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
	setImportButton();
	// 动态生成导航栏
	showFormula(true);
}

function setHead() {
	$("#theadId")
			.append(
					"<tr class='first'><td width='3%'>序号</td><td width='10%'>产品名称</td><td width='8%'>配方编号</td><td width='8%'>配方成本</td><td width='10%'>操作</td></tr>");
}

function setImportButton() {
	if (formula_import) {
		$("#searchDiv")
				.append(
						"<input type='button' onclick='downloadTemplate()' class='push-button' value='导入模板下载' style='float:right'><div style='float:right'><input id='upload' class='leading-in' type='button' value='导入'/></div>");

		// 创建导入按钮的方法。
		$("#upload").upload({
			name : 'file', // <input name="file" />
			action : '/FormulaSystem/api/formula?action=import', // 提交请求action路径
			enctype : 'multipart/form-data', // 编码格式
			onComplete : function(response) {// 请求完成时 调用函数
				if (JSON.parse(response).code == 200) {
					
					alert(JSON.parse(response).data);
					showFormula(true);
				} else {
					alert("导入失败，" + JSON.parse(response).data);
				}
			}
		});
	}
}

function downloadTemplate(){
	window.location.href = "/FormulaSystem/api/formula?action=template";
}

function showFormula(isReloadPagination) {
	//判断当前页是否还有数据，如果没有则回到上一页，在删除记录的时候，会出现这种情况
	if(totalCount > 0 && (totalCount - currentCount*totalPage) == 0){
		currentPage -= 1; 0
	}
	
	$.post("/FormulaSystem/api/formula?action=showlist", {
		"id" : id,
		"f_number" : f_number,
		"f_name" : f_name,
		"p_name" : p_name,
		"p_code" : p_code,
		"currentPage" : currentPage
	}, function(result) {
		removeTbodyContent();
		// 展示数据
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
function showPagination(formula_page) {
	$("#pagination > div").remove();
	$("#pagination").pagination({
		currentPage : formula_page.data.currentPage,
		totalPage : formula_page.data.totalPage,
		count : 5, // 只显示5页
		callback : function(current) {
			currentPage = current;
			showFormula(false);
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

function isEmpty(str) {
	if (str == "" || str == undefined) {
		return true;
	} else {
		return false;
	}
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
		var add5 = row.insertCell(4);


		var data_id = changeUndefined(result.data.list[i].id);
		//var data_f_name = changeUndefined(result.data.list[i].f_name);
		var data_f_number = changeUndefined(result.data.list[i].f_number);
		var data_price = changeUndefined(result.data.list[i].price);
		var data_p_name = changeUndefined(result.data.list[i].p_name);
		//var data_p_code = changeUndefined(result.data.list[i].p_code);

		add1.innerHTML = data_id;
		add2.innerHTML = i + 1 + (currentPage - 1) * currentCount;
		//add3.innerHTML = data_f_name;
		add3.innerHTML = data_p_name;
		add4.innerHTML = data_f_number;
		add5.innerHTML = data_price;
		
		//add7.innerHTML = data_p_code;
		var add6 = row.insertCell(5);
		//
		if (formula_export && formula_delete) {
			add6.innerHTML = "<input type='button' class='push-button' value='查看' onclick='updateRow(this)'><input id='exportButton' type='button' value='导出' onclick='exportExcel(this)' class='push-button'><input type='button' class='push-button'value='删除' onclick='deleteRow(this)'>";
		} else if(!formula_export && formula_delete){
			add6.innerHTML = "<input type='button' class='push-button' value='查看' onclick='updateRow(this)'><input type='button' class='push-button'value='删除' onclick='deleteRow(this)'>";
		} else if(!formula_export && !formula_delete){
			add6.innerHTML = "<input type='button' class='push-button' value='查看' onclick='updateRow(this)'>";
		} else if(formula_export && !formula_delete){
			add6.innerHTML = "<input type='button' class='push-button' value='查看' onclick='updateRow(this)'><input id='exportButton' type='button' value='导出' onclick='exportExcel(this)' class='push-button'>";
		}

	}

}

function deleteRow(deleteBtn){
	var r = confirm("是否删除该配方？");
	if (r == true) {
		// 获取该行的产品id
		var formulas = new Array();
		var formula = new Object();
		formulas.push(formula);
		formula.id = $($(deleteBtn).parent().parent().children()[0]).text();
		$.ajax({
			url : "/FormulaSystem/api/formula?action=delete",
			type : "POST",
			contentType : "application/json",
			dataType : "json",
			data : JSON.stringify(formulas),
			async : true,
			success : function(result) {
				if (result.code == 200) {
					// 删除成功后，刷新页面
					totalCount -= 1; //删除一条记录，则totalCount减一
					showFormula(true);
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

function searchByCondition() {
	id = $("#fId").val();
	f_name = $("#fName").val();
	f_number = $("#number").val();
	p_name = $("#p_name").val();
	p_code = $("#p_code").val();

	currentPage = 1;
	showFormula(true);
}

/*
 * function AddDialog(){ window.open
 * ('formulaDetail.html','newwindow','height=500,width=450,top=300,left=700,toolbar=no,menubar=no,scrollbars=no,
 * resizable=no,location=no, status=no'); }
 */

var windowNum = 0;
function updateRow(input) {
	var id = input.parentNode.parentNode.cells[0].innerHTML;
	window
			.open('formulaDetail.html?id=' + id, 'newwindow' + (windowNum++),
					'toolbar=no,menubar=no,scrollbars=yes, resizable=no,location=no, status=no');

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

	showFormula(true);

}

// 绑定导出的方法
function exportExcel(tagTemp) {
	var idTemp = tagTemp.parentNode.parentNode.firstChild.innerHTML;
	window.location.href = "/FormulaSystem/api/formula?action=output&id="
			+ idTemp + "&format=xls";
}
