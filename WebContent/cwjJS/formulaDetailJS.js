
var groupArr = [];

function loadData(){
	var id = window.location.search;
	//如果是新增按钮跳转的页面，则不执行myFunction函数
	var updateRowId = id.substring(4);
	//先获取数据
	$.ajax({
		url : "/FormulaSystem/api/formula?action=showdetails",
		type : "POST",
		contentType : "application/json",
		dataType : "json",
		data : "{'id':"+ updateRowId + "}",
		async : true,
		success : function(result) {
			var theTable = document.getElementById("tableId");
			//先把静态的表头设值
			theTable.rows[0].cells[0].firstChild.innerHTML = result.data.f_name; //代码
			theTable.rows[1].cells[2].innerHTML = result.data.p_code; //代码
			theTable.rows[1].cells[9].innerHTML = result.data.f_number; //配方批号
			
			theTable.rows[2].cells[1].innerHTML = result.data.p_name; //产品名称
			theTable.rows[2].cells[3].innerHTML = result.data.batch_number; //生产批号
			theTable.rows[2].cells[6].innerHTML = result.data.plain_amount; //计划量
			theTable.rows[2].cells[8].innerHTML = result.data.actual_output; //实产量
			
			theTable.rows[3].cells[1].innerHTML = result.data.water_ph; //产品名称
			theTable.rows[3].cells[3].innerHTML = result.data.ele_conductivity; //水质导电率
			theTable.rows[3].cells[6].innerHTML = result.data.equipment_state; //设备状态
			theTable.rows[3].cells[8].innerHTML = result.data.product_date; //生产日期
			
			//判断哪个list.size更大
			var materialLength = result.data.fmlist.length;
			var procLenght = result.data.procList.length;
			//将类别push进数组里
			var index;
			for(var j=0;j<materialLength;j++){
				if((index=isContain(result.data.fmlist[j].group))!=-1){
					groupArr[index].groupVal++;
				}
				else{
					var obj = {
							"groupName":result.data.fmlist[j].group,
							"groupVal":1
					};
					groupArr.push(obj);
				}
			}
			
			var isInsertFirstRow = false;
			var jqOfTable = $("#tableId");
			var IDIndex = 1;
			if(materialLength>procLenght){
				//原料列表较长的情况
				for(var x=0;x<groupArr.length;x++){
					for(var y=0;y<groupArr[x].groupVal;y++){
						if(!isInsertFirstRow){
							jqOfTable.append("<tr id='"+(IDIndex++)+"'><td rowspan='"+groupArr[x].groupVal+"><div align='center'>"+groupArr[x].groupName+"</div></td><td contentEditable='true'><div align='center'>"+result.data.fmlist[(IDIndex-2)].m_name+"</div></td><td contentEditable='true'><div align='center'>"+result.data.fmlist[(IDIndex-2)].cn_name+"</div></td><td contentEditable='true'><div align='center'>"+result.data.fmlist[(IDIndex-2)].plan_amount+"</div></td><td contentEditable='true'><div align='center'>"+result.data.fmlist[(IDIndex-2)].actual_amount+"</div></td><td contentEditable='true'><div align='center'>"+result.data.fmlist[(IDIndex-2)].m_batch_num+"</div></td><td contentEditable='true'><div align='center'>"+result.data.fmlist[(IDIndex-2)].checked_weight+"</div></td><td contentEditable='true'></td><td colspan='5' contentEditable='true'></td></tr>");
							isInsertFirstRow = true;
						}else{
							jqOfTable.append("<tr id='"+(IDIndex++)+"'><td contentEditable='true'><div align='center'>"+result.data.fmlist[(IDIndex-2)].m_name+"</div></td><td contentEditable='true'><div align='center'>"+result.data.fmlist[(IDIndex-2)].cn_name+"</div></td><td contentEditable='true'><div align='center'>"+result.data.fmlist[(IDIndex-2)].plan_amount+"</div></td><td contentEditable='true'><div align='center'>"+result.data.fmlist[(IDIndex-2)].actual_amount+"</div></td><td contentEditable='true'><div align='center'>"+result.data.fmlist[(IDIndex-2)].m_batch_num+"</div></td><td contentEditable='true'><div align='center'>"+result.data.fmlist[(IDIndex-2)].checked_weight+"</div></td><td contentEditable='true'></td><td colspan='5' contentEditable='true'> </td></tr>");
						}
					}
					isInsertFirstRow = false;
				}
				for(var x=1;x<=procLenght;x++){
					document.getElementById(x).lastChild.innerHTML = result.data.procList[(x-1)];
				}
				
				
			}else{
				//乳化工艺列表较长的情况
				var number = procLenght-materialLength;
				for(var x=0;x<groupArr.length;x++){
					for(var y=0;y<groupArr[x].groupVal;y++){
						if(!isInsertFirstRow){
							jqOfTable.append("<tr id='"+(IDIndex++)+"'><td rowspan='"+groupArr[x].groupVal+"'><div align='center'>"+groupArr[x].groupName+"</div></td><td contentEditable='true'><div align='center'>"+result.data.fmlist[(IDIndex-2)].m_name+"</div></td><td contentEditable='true'><div align='center'>"+result.data.fmlist[(IDIndex-2)].cn_name+"</div></td><td contentEditable='true'><div align='center'>"+result.data.fmlist[(IDIndex-2)].plan_amount+"</div></td><td contentEditable='true'><div align='center'>"+result.data.fmlist[(IDIndex-2)].actual_amount+"</div></td><td contentEditable='true'><div align='center'>"+result.data.fmlist[(IDIndex-2)].m_batch_num+"</div></td><td contentEditable='true'><div align='center'>"+result.data.fmlist[(IDIndex-2)].checked_weight+"</div></td><td contentEditable='true'></td><td colspan='5' contentEditable='true'></td></tr>");
							isInsertFirstRow = true;
						}else{
							jqOfTable.append("<tr id='"+(IDIndex++)+"'><td contentEditable='true'><div align='center'>"+result.data.fmlist[(IDIndex-2)].m_name+"</div></td><td contentEditable='true'><div align='center'>"+result.data.fmlist[(IDIndex-2)].cn_name+"</div></td><td contentEditable='true'><div align='center'>"+result.data.fmlist[(IDIndex-2)].plan_amount+"</div></td><td contentEditable='true'><div align='center'>"+result.data.fmlist[(IDIndex-2)].actual_amount+"</div></td><td contentEditable='true'><div align='center'>"+result.data.fmlist[(IDIndex-2)].m_batch_num+"</div></td><td contentEditable='true'><div align='center'>"+result.data.fmlist[(IDIndex-2)].checked_weight+"</div></td><td contentEditable='true'></td><td colspan='5' contentEditable='true'> </td></tr>");
						}
					}
					isInsertFirstRow = false;
				}
				
				for(var k=0;k<number;k++){
					jqOfTable.append("<tr id='"+(IDIndex++)+"'><td colspan='8'>&nbsp;</td><td colspan='6'></td></tr>");
				}
				
				for(var x=1;x<=procLenght;x++){
					document.getElementById(x).lastChild.innerHTML = result.data.procList[(x-1)];
				}
			}
			
			var attentionLength = result.data.attentionList.length;
			isInsertFirstRow = false;
			//先插入一行注意事项           乳化记录
			jqOfTable.append("<tr><td colspan='8'>注意事项：</td><td colspan='5'>乳化异常记录:</td></tr>");
			for(var x=0;x<attentionLength;x++){
				if(!isInsertFirstRow){
					jqOfTable.append("<tr><td colspan='8'>"+result.data.attentionList[x]+"</td><td rowspan='"+(attentionLength+1)+"' colspan='5' contentEditable='true'></td></tr>");
					isInsertFirstRow = true;
				}else{
					jqOfTable.append("<tr><td colspan='8'>"+result.data.attentionList[x]+"</td></tr>");
				}
				
			}
			//添加理化指标
			jqOfTable.append("<tr><td colspan='8'>理化指标:"+result.data.physicochemical_target+"</td></tr>");
			jqOfTable.append("<tr><td>&nbsp;</td><td>&nbsp;</td><td><div align='right'>工程师：</div></td><td contentEditable='true'>"+result.data.engineer+"</td><td><div align='right'>称料：</div></td><td contentEditable='true'>"+result.data.material_weigher+"</td><td><div align='right'>核称：</div></td><td contentEditable='true'>"+result.data.material_checker+"</td><td><div align='right'>配料员：</div></td><td contentEditable='true'>"+result.data.material_distributor+"</td><td><div align='right'>乳化主管：</d6iv></td><td contentEditable='true'>"+result.data.supervisor+"</td><td>&nbsp;</td></tr>");
		}
	});
	
	function isContain(gName){
		for(var j=0;j<groupArr.length;j++){
			if(groupArr[j].groupName==gName){
				return j;
			}
		}
		return -1;
	}
	
}

