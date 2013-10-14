/*这里用jquery会出现问题所以只能用js来实现组选了*/
	function group_click(group_id){
		var test = document.getElementById("group_" + group_id).checked;
		selElements = document.getElementsByTagName("input"); 
		if(test == true){
			for (var i = 0; i < selElements.length; i++) {
		        if (selElements[i].className == "div_friend_" + group_id) {
		        	selElements[i].checked = true;
		        }  
		    }  
		}else{
			for (var i = 0; i < selElements.length; i++) {  
		        if (selElements[i].className == "div_firend_" + group_id) {
		        	selElements[i].checked = false;
		        }  
		    }  
		}
	}
	function friend_state(group_id){
		 $("#div_group_" + group_id).toggle();
	}
	
	
	
	
	
	
	
	