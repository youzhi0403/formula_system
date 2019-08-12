//存储权限的变量

var product_showlist;

var formula_showlist;

var material_showlist;

var supplier_showlist;

var system_all;

var user_showlist;

var role_showlist;

var productManage_list;

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

function setAuthorityAndName() {
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
	
	console.log($.cookie("userPrivilege"));
	
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
	if (!system_all || (!user_showlist && !role_showlist && !productManage_showlist)) {
		document.getElementById("systemPage").style.display = "none";
	}
	/*
	 * else{ if(!user_showlist){
	 * document.getElementById("userPage").style.display = "none"; }
	 * if(!role_showlist){ document.getElementById("rolePage").style.display =
	 * "none"; } if(!productManage_showlist){
	 * document.getElementById("productManagePage").style.display = "none"; } }
	 */
}

$(document).ready(function() {
	$(".xitong").click(function() {
		$("p").toggle("slow");
	});
});

// 系统管理的显示与隐藏



