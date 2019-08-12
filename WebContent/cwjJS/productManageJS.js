//分页变量
var currentPage = 1; // 当前页
var currentCount = 20; // 当前数据条数
var totalCount; // 数据总条数
var totalPage; // 数据总页数
var isPaginationShow = false; // 分页空间是否已经展示

//存储权限的变量
var productAddIdentifier = "productAddIdentifier";

var product_showlist;

var formula_showlist;

var material_showlist;

var supplier_showlist;

var system_all;

var user_showlist;

var role_showlist;

var productManage_showlist;
var productManage_add;
var productManage_delete;
var productManage_update;

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

	role_showlist = result.role_showlist;

	productManage_showlist = result.product_SC_showlist;
	productManage_add = result.product_SC_add;
	productManage_delete = result.product_SC_delete;
	productManage_update = result.product_SC_update;

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
	if (!system_all|| (!user_showlist && !role_showlist && !productManage_showlist)) {
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

	showSeriesAndCategory();
}

function showSeriesAndCategory(isReloadPagination) {
	//判断当前页是否还有数据，如果没有则回到上一页，在删除记录的时候，会出现这种情况
	if(totalCount > 0 && (totalCount - currentCount*totalPage) == 0){
		if(currentPage>1){
			currentPage -= 1; 
		}
	}
	
	$.post("/FormulaSystem/api/product?action=showPSAndPC", function(result) {
		totalPage = result.data.totalPage;
		currentPage = result.data.currentPage;
		removeTbodyContent();

		showData(result);
		if (result.data.totalPage > 0) {
			$($("#page1").parent().parent()).show();
			$("#page1").text(currentPage + "/" + totalPage);
		} else {
			$($("#page1").parent().parent()).hide();
		}
		if (totalCount !== result.data.totalCount) {
			isReloadPagination = true;
		}
		if (isReloadPagination) {
			isReloadPagination = false;
			totalCount = result.data.totalCount;
			showPagination(result);
		}
	}, "json");
}

//显示分页
function showPagination(product_page) {
	$("#pagination > div").remove();
	$("#pagination").pagination({
		currentPage : product_page.data.currentPage,
		totalPage : product_page.data.totalPage,
		count : 5, // 只显示5页
		callback : function(current) {
			currentPage = current;
			showSeriesAndCategory(false);
		}
	});
}

function setHead() {
	if (!productManage_delete && !productManage_update) {
		$("#theadId")
				.append(
						"<tr class='first'><td width='22%'>序号</td><td width='24%'>产品系列</td><td width='32%'>产品类型</td></tr>");
	} else {
		$("#theadId")
				.append(
						"<tr class='first'><td width='22%'>序号</td><td width='24%'>产品系列</td><td width='32%'>产品类型</td><td width='22%'>操作</td></tr>");
	}
}

function setAddButton() {
	if (productManage_add) {
		$("#buttonDiv")
				.append(
						"<input type='button' value='新增' class='push-button' id='addButton' data-toggle='modal' data-target='#myModal' onclick='editProductManage(productAddIdentifier)'/>");
	}
}


function removeTbodyContent() {
	var tbody = document.getElementById("tbodyId");
	var index = tbody.childNodes.length;
	for (var i = 0; i < index; i++) {
		tbody.removeChild(tbody.childNodes[0]);
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

		var data_id = changeUndefined(result.data.list[i].id);
		var data_name = changeUndefined(result.data.list[i].s_name);
		var data_categories = "";
		for (var j = 0; j < result.data.list[i].p_catogeris.length; j++) {
			data_categories += result.data.list[i].p_catogeris[j].name + ", ";
		}
		data_categories = changeUndefined(data_categories);

		add1.innerHTML = data_id;
		add2.innerHTML = i + 1 + (currentPage - 1) * currentCount;
		add3.innerHTML = data_name;
		add4.innerHTML = data_categories;

		if (productManage_update && productManage_delete) {
			var add5 = row.insertCell(4);
			add5.innerHTML = "<input type='button' class='push-button' value='修改' data-toggle='modal' data-target='#myModal' onclick='editProductManage(this)'/><input type='button' class='push-button' value='删除' onclick='deleteRow(this)' >";
		} else if (!productManage_update && productManage_delete) {
			var add5 = row.insertCell(4);
			add5.innerHTML = "<input type='button' class='push-button' value='删除' onclick='deleteRow(this)' >";
		} else if (productManage_update && !productManage_delete) {
			var add5 = row.insertCell(4);
			add5.innerHTML = "<input type='button' class='push-button' value='修改' data-toggle='modal' data-target='#myModal' onclick='editProductManage(this)'/>";
		}

	}
}

/**
 * 删除一条记录
 */
function deleteRow(deleteBtn){
	var r = confirm("是否删除该系列？");
	if (r == true) {
		// 获取该行的产品id
		var seriesArr = new Array();
		var Series = new Object();
		seriesArr.push(Series);
		Series.id = $($(deleteBtn).parent().parent().children()[0]).text();
		$.ajax({
			url : "/FormulaSystem/api/product?action=deleteSC",
			type : "POST",
			contentType : "application/json",
			dataType : "json",
			data : JSON.stringify(seriesArr),
			async : true,
			success : function(result) {
				if (result.code == 200) {
					// 删除成功后，刷新页面
					totalCount -= 1; //删除一条记录，则totalCount减一
					showSeriesAndCategory(true);
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

function changeUndefined(str) {
	if (str == undefined) {
		str = "暂无此数据";
	}
	return str;
}

// 手风琴菜单
$(document).ready(function() {
	var $submenu = $('.submenu');
	var $mainmenu = $('.mainmenu');
	$submenu.hide();
	$submenu.on('click', 'li', function() {
		$submenu.siblings().find('li').removeClass('chosen');
		$(this).addClass('chosen');
	});
	$mainmenu.on('click', 'li', function() {
		$(this).next('.submenu').slideToggle().siblings('.submenu').slideUp();
	});

});

function refreshPage() {
	showSeriesAndCategory();
}
