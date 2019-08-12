var materialAddOrUpdate = "materialAddIdentifier";

//分页变量
var currentPage = 1; // 当前页
var currentCount = 20; // 当前数据条数
var totalCount; // 数据总条数
var totalPage; // 数据总页数

//注释
// 存储参数的变量
var id;
var name1;

var code;
var inci_cn;
var inci_en;

var application;

// 存储权限的变量
var product_showlist;

var formula_showlist;

var material_add;
var material_delete;
var material_update;
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
	// 先显示用户名
	document.getElementById("userName").innerHTML = $.cookie("userName");
	// 拿到各个权限值
	getAuthority(JSON.parse($.cookie("userPrivilege")));
}

function getAuthority(result) {
	product_showlist = result.product_showlist;

	formula_showlist = result.formula_showlist;

	material_add = result.material_add;
	material_delete = result.material_delete;
	material_update = result.material_update;
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
	// 设置table头部
	setHead();
	// 增加新增按钮
	setAddButton();
	// 设置导航栏
	showMaterial(true);
	
	//初始化好模态框的select框
	createAndLoadSelectOfModelBox();
}

function setHead() {
	if (!material_delete && !material_update) {
		$("#theadId")
				.append(

						"<tr class='first' id='theadTR'><td width='2%'>序号</td><td width='6%'>商品名</td><td width='6%'>中文名</td><td width='3%'>代码</td><td width='3%'>单价</td><td width='2%'>单位</td><td width='6%'>产地</td><td width='7%'>外观状态</td><td width='5%'>用途</td><td width='8%'>中文INCI</td><td width='8%'>英文INCI</td><td width='4%'>包装方式</td><td width='8%'>供应商名字</td></tr>");
	} else {
		$("#theadId")
				.append(
						"<tr class='first' id='theadTR'><td width='2%'>序号</td><td width='6%'>商品名</td><td width='6%'>中文名</td><td width='3%'>代码</td><td width='3%'>单价</td><td width='2%'>单位</td><td width='6%'>产地</td><td width='7%'>外观状态</td><td width='5%'>用途</td><td width='8%'>中文INCI</td><td width='8%'>英文INCI</td><td width='4%'>包装方式</td><td width='8%'>供应商名字</td><td width='6%'>操作</td></tr>");
	}

}

function setAddButton() {
	if (material_add) {
		$("#buttonDiv")
				.append(
						"<input type='button' value='新增' data-toggle='modal' data-target='#myModal' class='push-button' id='addButton' onclick='editMaterial(materialAddOrUpdate)'/><input type='button' onclick='downloadTemplate()' class='push-button' value='导入模板下载' style='float:right'><div style='float:right;margin-left:5px;'><input id='upload' class='leading-in' type='button' value='导入'/></div>");
		// 创建导入按钮的方法。
		$("#upload").upload({
			name : 'file', // <input name="file" />
			action : '/FormulaSystem/api/material?action=import', // 提交请求action路径
			enctype : 'multipart/form-data', // 编码格式
			onComplete : function(response) {// 请求完成时 调用函数
				if (JSON.parse(response).code == 200) {
					alert(JSON.parse(response).data);
					showMaterial(true);
				} else {
					alert("导入失败，" + JSON.parse(response).data);
				}
			}
		});
	}
}

function downloadTemplate(){
	window.location.href = "/FormulaSystem/api/material?action=template";
}

function showMaterial(isReloadPagination) {
	//判断当前页是否还有数据，如果没有则回到上一页，在删除记录的时候，会出现这种情况
	if(totalCount > 0 && (totalCount - currentCount*totalPage) == 0){
		currentPage -= 1; 
	}
	
	$.post("/FormulaSystem/api/material?action=showlist", {
		"id" : id,
		"name" : name1,
		"code" : code,
		"inci_cn" : inci_cn,
		"currentPage" : currentPage
	}, function(result) {

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
function showPagination(metarial_page) {
	$("#pagination > div").remove();
	$("#pagination").pagination({
		currentPage : metarial_page.data.currentPage,
		totalPage : metarial_page.data.totalPage,
		count : 5, // 只显示5页
		callback : function(current) {
			currentPage = current;
			showMaterial(false);
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


function showData(result) {

	if (result.code != 200)
		return;
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
		var add7 = row.insertCell(6);
		var add8 = row.insertCell(7);
		var add9 = row.insertCell(8);
		var add10 = row.insertCell(9);
		var add11 = row.insertCell(10);
		var add12 = row.insertCell(11);
		var add13 = row.insertCell(12);
		var add14 = row.insertCell(13);

		// 将undefined该成暂无此数据
		var data_id = changeUndefined(result.data.list[i].id);
		var data_commodity = changeUndefined(result.data.list[i].name);
		var data_chineseName = changeUndefined(result.data.list[i].chineseName);
		
		var data_code = changeUndefined(result.data.list[i].code);
		var data_price = changeUndefined(result.data.list[i].price);
		var data_unit = changeUndefined(result.data.list[i].unit);
		var data_origin = changeUndefined(result.data.list[i].origin);
		var data_MApparentState = changeUndefined(result.data.list[i].MApparentState);
		var data_application = changeUndefined(result.data.list[i].application);
		var data_inci_cn = changeUndefined(result.data.list[i].inci_cn);
		var data_inci_en = changeUndefined(result.data.list[i].inci_en);
		var data_packingWay = changeUndefined(result.data.list[i].packingWay);
		var data_supplier = changeUndefined(result.data.list[i].supplier);
		var data_sid = changeUndefined(result.data.list[i].sid);

		add1.innerHTML = data_id;
		add2.innerHTML = i + 1 + (currentPage - 1) * currentCount;
		add3.innerHTML = data_commodity;
		add4.innerHTML = data_chineseName;
		add5.innerHTML = data_code;
		add6.innerHTML = data_price;
		add7.innerHTML = data_unit;
		add8.innerHTML = data_origin;
		add9.innerHTML = data_MApparentState;
		add10.innerHTML = data_application;
		add11.innerHTML = data_inci_cn;
		add12.innerHTML = data_inci_en;
		add13.innerHTML = data_packingWay;
		add14.innerHTML = data_supplier + "<span style='display:none'>"+data_sid+"</span>";

		if (material_update && material_delete) {
			var add15 = row.insertCell(14);
			add15.innerHTML = "<input type='button' class='push-button' value='修改' onclick='editMaterial(this)' data-toggle='modal' data-target='#myModal'/><input type='button' value='删除' onclick='deleteRow(this)' class='push-button'>";
		} else if (!material_update && material_delete) {
			var add15 = row.insertCell(14);
			add15.innerHTML = "<input type='button' class='push-button' value='删除' onclick='deleteRow(this)'>";
		} else if (material_update && !material_delete) {
			var add15 = row.insertCell(14);
			add15.innerHTML = "<input type='button' class='push-button' value='修改' onclick='editMaterial(this)' data-toggle='modal' data-target='#myModal' />";
		}
		
	}
}


//将模态框的select框先加载好
function createAndLoadSelectOfModelBox(){
	$.post(
			"/FormulaSystem/api/material/supplier-name",
			function(result){
				
				var theSelect = $("#selectIdOfModelBox");
				optionNum = result.data.length;
				for(var i=0;i<optionNum;i++){
					theSelect.append("<option value='"+result.data[i].id+"'>"+result.data[i].companyName+"</option>");
				}
				
			},
			"json"
	);
}

function deleteRow(deleteBtn) {
	var r = confirm("是否删除该原料？");
	if (r == true) {
		// 获取该行的产品id
		var material = new Object();
		material.id = $($(deleteBtn).parent().parent().children()[0]).text();
		$.ajax({
			url : "/FormulaSystem/api/material?action=delete",
			type : "POST",
			contentType : "application/x-www-form-urlencoded",
			data : material,
			dataType : "json",
			async : true,
			success : function(result) {
				if (result.code == 200) {
					// 删除成功后，刷新页面
					totalCount -= 1; //删除一条记录，则totalCount减一
					showMaterial(true);
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

function searchByCondition() {
	id = $("#materialId").val();
	name1 = $("#name").val();
	code = $("#code").val();
	inci_cn = $("#inci_cn").val();

	currentPage = 1;
	showMaterial(true);
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

	showMaterial(false);

}







