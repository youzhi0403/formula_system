var addIdentifier = "addProductIdentifier";

// 分页变量
var currentPage = 1; // 当前页
var currentCount = 20; // 当前数据条数
var totalCount; // 数据总条数
var totalPage; // 数据总页数
var isPaginationShow = false; // 分页空间是否已经展示

// 存储参数的变量
var pId;
var productName;
var number;
var formula;
var seriesId;
var categoryId;
var retention;
// 存储权限的变量

var product_add;
var product_delete;
var product_update;
var product_showlist;

var formula_showlist;

var material_showlist;

var supplier_showlist;

var system_all;

var user_showlist;

var role_showlist;

var productManage_list;

var theSeriesType;

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

// 进入页面后，获取token
function setAuthorityAndName() {
	// 先显示用户名
	document.getElementById("userName").innerHTML = $.cookie("userName");
	console.log($.cookie("userPrivilege"));
	getAuthority(JSON.parse($.cookie("userPrivilege")));
}

function getAuthority(result) {
	product_add = result.product_add;
	product_delete = result.product_delete;
	product_update = result.product_update;
	product_showlist = result.product_showlist;

	formula_showlist = result.formula_showlist;

	material_showlist = result.material_showlist;

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
	// 动态生成导航栏
	setNavigation();

	/*addAutoComplete();*/
}

function setHead() {
	// 权限判断生成表头
	if (!product_delete && !product_update) {
		$("#theadId")
				.append(
						"<tr class='first'><td width='2%'>序号</td><td width='7%'>产品名称</td><td width='7%'>配方编号</td><td width='10%'>主要成分</td><td width='18%'>产品介绍</td><td width='4%'>产品报价</td><td width='5%'>留样量(kg)</td></tr>");
	} else {
		$("#theadId")
				.append(
						"<tr class='first'><td width='2%'>序号</td><td width='7%'>产品名称</td><td width='7%'>配方编号</td><td width='10%'>主要成分</td><td width='18%'>产品介绍</td><td width='4%'>产品报价</td><td width='5%'>留样量(kg)</td><td width='6%'>操作</td></tr>");
	}	

}

function setAddButton() {
	if (product_add) {
		$("#buttonDiv")
				.append(
						"<input type='button' value='新增' onclick='editProduct(addIdentifier)' class='push-button' id='addButton' data-toggle='modal' data-target='#myModal'/><input type='button' onclick='downloadTemplate()' class='push-button' value='导入模板下载' style='float:right'><div style='float:right;margin-left:5px;'><input id='upload' class='leading-in' type='button' value='导入'/></div>");
		// 创建导入按钮的方法。
		$("#upload").upload({
			name : 'file', // <input name="file" />
			action : '/FormulaSystem/api/product?action=import', // 提交请求action路径
			enctype : 'multipart/form-data', // 编码格式
			onComplete : function(response) {// 请求完成时 调用函数
				if (JSON.parse(response).code == 200) {
					alert(JSON.parse(response).data);
					showProduct(true);
				} else {
					alert("导入失败，" + JSON.parse(response).data);
				}
			}
		});
	}
}
function downloadTemplate(){
	window.location.href = "/FormulaSystem/api/product?action=template";
}


// 动态生成左侧的导航栏
function setNavigation() {
	$.post(
					"/FormulaSystem/api/product?action=showPSAndPC",
					function(result) {
						if (result.code == "200") {
							// 遍历有多少个系列
							// 这里加
							$("#productSeriesList")
									.append(
											"<li class='list lista' onclick='searchAll()'><span>全部</span></li><ul class='submenu'></ul>");
							for (var i = 0; i < result.data.list.length; i++) {
								$("#productSeriesList").append(
										"<li onclick='searchBySeries(this)' class='list'><span id='"
												+ result.data.list[i].id + "'>"
												+ result.data.list[i].s_name
												+ "</span></li>");
								/* if(result.data[i].p_catogeris.length>0){ */
								$("#productSeriesList").append(
										"<ul class='submenu' style='display:none' id='series"
												+ result.data.list[i].id
												+ "'></ul>");
								/* } */
								// 二级菜单
								for (var j = 0; j < result.data.list[i].p_catogeris.length; j++) {
									$("#series" + result.data.list[i].id)
											.append(
													"<li class='list-a' ><span><a href='#' class='list-c' onclick='searchByCategory(this)'>"
															+ result.data.list[i].p_catogeris[j].name
															+ "<span style='display:none'>"
															+ result.data.list[i].p_catogeris[j].id
															+ "</span></a></span></li>");
								}
							}
							// 创建click方法
							$(".list").click(function() {
								$(this).removeClass("list");
								$(this).addClass("lista");
								$(this).siblings().removeClass("lista");
								$(this).siblings().addClass("list");
							});
							$(document)
									.ready(
											function() {
												$(".list-a")
														.each(
																function(index) {
																	$(this)
																			.click(
																					function() {
																						$(
																								".list-a")
																								.removeClass(
																										"list-b");
																						$(
																								".list-a")
																								.eq(
																										index)
																								.addClass(
																										"list-b");
																					});
																});
											});
							// a标签字体颜色改变
							$(document).ready(function() {
								$("a").each(function(index) {
									$(this).click(function() {
										$("a").removeClass("list-d");
										$("a").eq(index).addClass("list-d");
									});
								});
							});

							// 把系列类别信息存储到变量里
							theSeriesType = result;

							// 设置联动框

							createFirstSelect(result);
							// 展示产品
							showProduct(true);

						} else {
							alert("导航栏加载失败");
						}
					}, "json");
}

function createFirstSelect(result) {
	var index = 0;
	var firstSelect = $("#seriesSelect");
	// 添加请选择的框
	firstSelect.append("<option value=''>-全部-</option>");

	for (var i = 0; i < result.data.list.length; i++) {
		firstSelect.append("<option value='" + index + "' id='first"
				+ result.data.list[i].id + "'>" + result.data.list[i].s_name
				+ "</option>");
		index++;
	}

}

function createSecondSelect(optionVal) {
	/*
	 * alert(typeof optionVal); alert(optionVal);
	 * 
	 * return;
	 */
	// 先清除第二个select框的option
	// 为空的情况
	if (optionVal === "") {
		removeOptions();
		return;
	}
	removeOptions();
	var secondSelect = $("#typeSelect");
	secondSelect.append("<option value=''>-全部-</option>");
	for (var k = 0; k < theSeriesType.data.list[optionVal].p_catogeris.length; k++) {
		secondSelect.append("<option id='second"
				+ theSeriesType.data.list[optionVal].p_catogeris[k].id + "'>"
				+ theSeriesType.data.list[optionVal].p_catogeris[k].name
				+ "</option>");
	}

}

function removeOptions() {
	var secondSelect = document.getElementById("typeSelect");
	var len = secondSelect.options.length;

	for (var i = 0; i < len; i++) {
		secondSelect.options[0] = null;
	}
}

function showProduct(isReloadPagination) {
	// 判断当前页是否还有数据，如果没有则回到上一页，在删除记录的时候，会出现这种情况
	if (totalCount > 0 && (totalCount - currentCount * totalPage) == 0) {
		if (currentPage > 1) {
			currentPage -= 1;
		}

	}

	// 先请求后台获取用户权限(把信息存在全局变量里)
	$.post("/FormulaSystem/api/product?action=showlist", {
		"currentPage" : currentPage,
		"id" : pId,
		"name" : productName,
		"number" : number,
		"formula" : formula,
		"sid" : seriesId,
		"cid" : categoryId
	}, function(result) {
		totalPage = result.data.totalPage;
		currentPage = result.data.currentPage;
		// 要删掉之前的table，创建一个新的table：
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

// 显示分页
function showPagination(product_page) {
	$("#pagination > div").remove();
	$("#pagination").pagination({
		currentPage : product_page.data.currentPage,
		totalPage : product_page.data.totalPage,
		count : 5, // 只显示5页
		callback : function(current) {
			currentPage = current;
			showProduct(false);
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
function removePageDiv() {
	var pageDiv = $("#pageDiv");
	pageDiv.remove();
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

function showData(result) {
	var tbody = document.getElementById("tbodyId");

	for (var i = 0; i < result.data.list.length; i++) {
		var row = tbody.insertRow(-1);
		row.setAttribute("class", "table-row");
		// 用一个隐藏的列来保存每一条记录的id
		var add1 = row.insertCell(0);
		add1.style.display = "none";

		var add2 = row.insertCell(1);
		var add3 = row.insertCell(2);
		var add4 = row.insertCell(3);
		var add5 = row.insertCell(4);
		var add6 = row.insertCell(5);
		var add7 = row.insertCell(6);
		var add8 = row.insertCell(7);

		var data_id = changeUndefined(result.data.list[i].id);
		var data_name = changeUndefined(result.data.list[i].name);
		var data_number = changeUndefined(result.data.list[i].number);
		var data_major_composition = changeUndefined(result.data.list[i].major_composition);
		var data_formula_desc = changeUndefined(result.data.list[i].formula_desc);
		var data_price = changeUndefined(result.data.list[i].price);
		var data_sample_quantity = changeUndefined(result.data.list[i].sample_quantity);

		add1.innerHTML = data_id;
		add2.innerHTML = i + 1 + (currentPage - 1) * currentCount;
		add3.innerHTML = data_name;
		add4.innerHTML = data_number;
		add5.innerHTML = data_major_composition;
		add6.innerHTML = data_formula_desc;
		add7.innerHTML = data_price;
		add8.innerHTML = data_sample_quantity;
		var add9;
		if (product_update && product_delete) {
			add9 = row.insertCell(8);
			add9.innerHTML = "<input type='button' class='push-button' value='修改' onclick='editProduct(this)' data-toggle='modal' data-target='#myModal'><input type='button' value='删除' class='push-button' onclick='deleteRow(this)'>";
		} else if (!product_update && product_delete) {
			add9 = row.insertCell(8);
			add8.innerHTML = "<input type='button' class='push-button' value='删除' onclick='deleteRow(this)'>";
		} else if (product_update && !product_delete) {
			add9 = row.insertCell(8);
			add8.innerHTML = "<input type='button' class='push-button' value='修改' onclick='editProduct(this)' data-toggle='modal' data-target='#myModal'>";
		}

		// 设置表格的第五、六列的样式(包括隐藏列)
		$('tbody tr').find('td:eq(5)').css({
			"text-align" : "left"
		});
		$('tbody tr').find('td:eq(4)').css({
			"text-align" : "left"
		});
		$('tbody tr').find('td:eq(5)').css({
			"text-indent" : "30px"
		});
		$('tbody tr').find('td:eq(4)').css({
			"text-indent" : "30px"
		});

	}
}

// 删除一行
function deleteRow(deleteBtn) {
	var r = confirm("是否删除该产品？");
	if (r == true) {
		// 获取该行的产品id
		var products = new Array();
		var product = new Object();
		products.push(product);
		product.id = $($(deleteBtn).parent().parent().children()[0]).text();
		$.ajax({
			url : "/FormulaSystem/api/product?action=delete",
			type : "POST",
			contentType : "application/json",
			dataType : "json",
			data : JSON.stringify(products),
			async : true,
			success : function(result) {
				if (result.code == 200) {
					// 删除成功后，刷新页面
					totalCount -= 1; // 删除一条记录，则totalCount减一
					showProduct(true);
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
	pId = $("#pId").val();
	productName = $("#pName").val();
	number = $("#pNumber").val();
	formula = $("#formula_name").val();

	seriesId = dealId2(getCurrentSeriesId());
	categoryId = dealId(getCurrentCategoryId());

	currentPage = 1;
	showProduct(true);
}

function getCurrentSeriesId() {
	var theSelect = document.getElementById("seriesSelect");
	var index = theSelect.selectedIndex;
	if (index != -1) {
		return theSelect[index].id;
	} else {
		return "";
	}

}

function getCurrentCategoryId() {
	var theSelect = document.getElementById("typeSelect");
	var index = theSelect.selectedIndex;
	if (index != -1) {
		return theSelect[index].id;
	} else {
		return "";
	}

}

function dealId(str) {
	if (str == "") {
		return "";
	} else {
		return str.substring(6, str.length);
	}
}

function dealId2(str) {
	if (str == "") {
		return "";
	} else {
		return str.substring(5, str.length);
	}
}

function resetInput() {
	// 需要把分页也重置到第一页,先不写
	$("#pId").val("");
	$("#pName").val("");
	$("#pNumber").val("");
	$("#fId").val("");
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

	showProduct();

}

// 手风琴菜单列表
$(document).ready(function() {
	var $submenu = $('.submenu');
	var $mainmenu = $('.mainmenu');
	$submenu.hide();
	$submenu.first().delay(400).slideDown(700);
	$submenu.on('click', 'li', function() {
		$submenu.siblings().find('li').removeClass('chosen');
		$(this).addClass('chosen');
	});
	$mainmenu.on('click', 'li', function() {
		$(this).next('.submenu').slideToggle().siblings('.submenu').slideUp();
	});
});

// 分为两种情况，点击li标签的(系列)，点击a标签的(类别)
// 获取参数值，改参数后，showProduct

function searchAll() {
	currentPage = 1;
	pId = "";
	productName = "";
	number = "";
	formula = "";
	categoryId = "";
	seriesId = "";
	showProduct(true);
	$("#seriesSelect option:first").prop('selected', true);
	removeOptions();
}

function searchBySeries(liTag) {
	// 重置参数
	seriesId = liTag.firstChild.getAttribute("id");
	currentPage = 1;
	pId = "";
	productName = "";
	number = "";
	formula = "";
	categoryId = "";

	showProduct(true);

	// 根据系列设置联动框(在点击产品的时候，加载完body体，已经设置联动框了)
	// 先选中第一个select指定的option(根据firstselect的id)
	setFirstSelect(seriesId);
	var theIndex = findIndexOfSeriesId(seriesId);
	createSecondSelect(theIndex);
}

function setFirstSelect(sId) {
	var firstSelect = document.getElementById("seriesSelect");
	// 重置下select框
	resetSelect(firstSelect);
	var optionElements = firstSelect.getElementsByTagName("option");
	for (var k = 0; k < optionElements.length; k++) {

		if (("first" + sId) == optionElements[k].getAttribute("id")) {
			/* optionElements[k].setAttribute("selected","selected"); */
			optionElements[k].selected = true;
		}
	}
}

function searchByCategory(aTag) {
	seriesId = aTag.parentNode.parentNode.parentNode.getAttribute("id");
	if (containSeries(seriesId)) {
		seriesId = seriesId.substring(6, seriesId.length);
	}
	categoryId = aTag.lastChild.innerHTML;
	currentPage = 1;
	pId = "";
	productName = "";
	number = "";
	formula = "";
	showProduct(true);
	// 根据系列，类别设置联动框

	setFirstSelect(seriesId);

	var theIndex = findIndexOfSeriesId(seriesId);
	createSecondSelect(theIndex);
	setSecondSelect(categoryId);
}

function setSecondSelect(cId) {
	var secondSelect = document.getElementById("typeSelect");
	var optionElements = secondSelect.getElementsByTagName("option");
	for (var k = 0; k < optionElements.length; k++) {
		var strTemp = optionElements[k].getAttribute("id");
		if (strTemp !== null) {
			strTemp = strTemp.substring(6, strTemp.length);
		}
		if (strTemp === cId) {
			optionElements[k].setAttribute("selected", "selected");
		}
	}
}

function findIndexOfSeriesId(sId) {
	var isFlag = false;
	var tempId = sId;
	// 通过类别来查询，要截取掉series字符串
	/* alert(tempId.substring(0,1)); */
	if (tempId.substring(0, 1) == "s") {
		if (containSeries(sId)) {
			var tempId = tempId.substring(6, tempId.length);
		}
	}

	for (var k = 0; k < theSeriesType.data.list.length; k++) {
		if (tempId == theSeriesType.data.list[k].id) {
			isFlag = true;
			return k;
		}
	}
	if (!isFlag) {
		return -1;
	}
}

// 参数：select框对象;作用：将所有option的selected置为""
function resetSelect(theSelectOfJS) {
	var optionElements = theSelectOfJS.getElementsByTagName("option");
	for (var k = 0; k < optionElements.length; k++) {
		if (optionElements[k].getAttribute("selected") == "selected") {
			optionElements[k].selected = false;
		}
	}
}

function containSeries(str) {
	if (str.length >= 7) {
		// 可能是包含字符串series的情况
		if (str.substring(0, 6) == "series") {
			return true;
		} else {
			return false;
		}
	} else {
		// 不可能是
		return false;
	}
}

/*
 * function createSelectForSupplier(){ //先创建配方的下拉框： $.post(
 * "/FormulaSystem/api/product/formula-name", function(result){ var theSelect =
 * $("#selectId"); optionNum = result.data.length; for(var i=0;i<optionNum;i++){
 * theSelect.append("<option
 * value='"+result.data[i].id+"'>"+result.data[i].name+"</option>"); } },
 * "json" ); }
 */

// 一开始要给模态框 配方编号input框 添加autoComplete功能
/*var availableTags = [ "ActionScript", "AppleScript", "Asp", "BASIC", "C",
		"C++", "Clojure", "COBOL", "ColdFusion", "Erlang", "Fortran", "Groovy",
		"Haskell", "Java", "JavaScript", "Lisp", "Perl", "PHP", "Python",
		"Ruby", "Scala", "Scheme" ];*/

function addAutoComplete() {
	$.post("/FormulaSystem/api/product?action=showFormulaNumberList", function(
			result) {
		if (result.code == 200) {
			$("#form_number").autocomplete({
				/*source: availableTags,*/
				source : result.data
			});
		}
	}, "json");
}
