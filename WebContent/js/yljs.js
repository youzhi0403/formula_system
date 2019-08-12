// JavaScript Document


/*       function trdadd(){  
      flag=false;  
      document.getElementById("zid").style.display="block";
      chongzhi();  
         document.getElementById("aid").disabled=false; 
       
      }  
      function baocun(){  
            if(flag==false){           
      add(flag);  
      document.getElementById("zid").style.display="none";  
            }else{         
              add(flag);  
              document.getElementById("zid").style.display="none";  
             }  
      }  
      function chongzhi(){  
       document.getElementById("formid").reset();  
      }       
      function deleteRow(input){  
          var s=input.parentNode.parentNode.rowIndex;  
          document.getElementById("tableid").deleteRow(s);  
          var num=document.getElementById("tableid").rows.length;  
          for(var i=1;i<num;i+=1){  
             table.rows[i].cells[0].innerHTML=i; 
             }  
             alert("删除成功！！");  
       }  
	    function newWin(){
	    window.open ('原料增改.html','newwindow','height=500,width=400,top=300,left=700,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
		return false;
  }*/
	    
	    
	    
	    
	    //----------------------------------------------
	    function showMaterial(){
			//要删掉之前的table，创建一个新的table：
			var tbody = document.getElementById("tbodyId");
			var index = tbody.childNodes.length;
			for(var i=0;i<index;i++){
				tbody.removeChild(tbody.childNodes[0]);
			}
	/*		alert("show执行进来了");*/
					
			$.post(
	 		"/FormulaSystem/api/material/showlist",
			function(result){
	 			var tbody = document.getElementById("tbodyId");
	/* 			alert("showSupplier方法执行到这里了");
	 			alert(result.data.list[1].id);*/

				for(var i=0;i<result.data.list.length;i++){
					var row=tbody.insertRow(-1);   
				    var add1=row.insertCell(0);  
				    var add2=row.insertCell(1);  
				    var add3=row.insertCell(2);  
				    var add4=row.insertCell(3);    
				    var add5=row.insertCell(4);
				    var add6 = row.insertCell(5);
				    var add7 = row.insertCell(6);
				    var add8 = row.insertCell(7);
				    
				    add1.innerHTML=result.data.list[i].id;
				    add2.innerHTML=result.data.list[i].name; 
				    add3.innerHTML = result.data.list[i].price;
				    add4.innerHTML=result.data.list[i].unit;
				    add5.innerHTML=result.data.list[i].origin; 
				    add6.innerHTML = result.data.list[i].texture;
				    add7.innerHTML=result.data.list[i].supplierName;
				    
				    add8.innerHTML="<input type='button' value='修改' onclick='updateRow(this)'> <input type='button' value='删除' onclick='deleteRow(this)'>";
				    /*console.log(add1.innerHTML+" "+add2.innerHTML+" "+add3.innerHTML+" "+add4.innerHTML+" "+add5.innerHTML);*/
				}
			},
			"json"
		);
		}
	    
	    //模糊查询的功能
	    function searchByCondition(){
	    	alert("进入到条件查询");
	    	var materialId = document.getElementById("materialId").value;
	    	var name = document.getElementById("name").value;
	    	var origin = document.getElementById("origin").value;
	    	var texture = document.getElementById("texture").value;
	    	var supplierId = document.getElementById("supplier").value;
	    	alert("supplierId:"+supplierId);
	    	
	    	$.post(
	    			"/FormulaSystem/api/material/showlist",
	    			{"id":materialId,"name":name,"origin":origin,"texture":texture,"sid":supplierId},
	    			function(result){
	    				//先把表格内的数据干掉
	    				var tbody = document.getElementById("tbodyId");
	    				var index = tbody.childNodes.length;
	    				for(var i=0;i<index;i++){
	    					tbody.removeChild(tbody.childNodes[0]);
	    				}
	    				
    					var tbody = document.getElementById("tbodyId");
    					
    					alert(result.data.list.length);

    					for(var i=0;i<result.data.list.length;i++){
							var row=tbody.insertRow(-1);   
						    var add1=row.insertCell(0);  
						    var add2=row.insertCell(1);  
						    var add3=row.insertCell(2);  
						    var add4=row.insertCell(3);    
						    var add5=row.insertCell(4);
						    var add6 = row.insertCell(5);
						    var add7 = row.insertCell(6);
						    var add8 = row.insertCell(7);
						    
						    add1.innerHTML=result.data.list[i].id;
						    add2.innerHTML=result.data.list[i].name; 
						    add3.innerHTML = result.data.list[i].price;
						    add4.innerHTML=result.data.list[i].unit;
						    add5.innerHTML=result.data.list[i].origin; 
						    add6.innerHTML = result.data.list[i].texture;
						    add7.innerHTML=result.data.list[i].supplierName;
						    
						    /*alert("add1.innerHTML"+add1.innerHTML);
						    alert("add2.innerHTML"+add2.innerHTML);
						    alert("add3.innerHTML"+add3.innerHTML);
						    alert("add4.innerHTML"+add4.innerHTML);
						    alert("add5.innerHTML"+add5.innerHTML);
						    alert("add6.innerHTML"+add6.innerHTML);
						    alert("add7.innerHTML"+add7.innerHTML);*/
						    
						    add8.innerHTML="<input type='button' value='修改' onclick='updateRow(this)'> <input type='button' value='删除' onclick='deleteRow(this)'>";
    					}

	    			},
	    			"json");
	    	
	    }
	    
	    //添加模块
	    function add(){
	    	window.open("materialAdd.html");
	    }
	    
	    
	    //把数据传输到add页面过去
	    function updateRow(input){
			var id = input.parentNode.parentNode.cells[0].innerHTML;
			alert(id);
			window.open("materialAdd.html?id="+id);
	    }
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    