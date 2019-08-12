// JavaScript Document

		
 function trdadd(){  
            flag=false;  
      document.getElementById("fid").style.display="block";
      chongzhi();  
         document.getElementById("aid").disabled=false; 
       
      }  
      function baocun(){  
            if(flag==false){           
      add(flag);  
      document.getElementById("fid").style.display="none";  
            }else{         
              add(flag);  
              document.getElementById("fid").style.display="none";  
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
	    window.open ('产品增改.html','newwindow','height=500,width=400,top=300,left=700,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
		return false;
  }