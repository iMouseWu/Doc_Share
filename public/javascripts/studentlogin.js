/*这里用jquery会出现问题所以只能用js来实现组选了*/
	function group_click(group_id){
		var test = document.getElementById("group_" + group_id).checked;
		selElements = document.getElementsByTagName("input"); 
		if(test == true){
			for (var i = 0; i < selElements.length; i++) {  
		        if (selElements[i].className == "div_firend_" + group_id) {
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


	/*如果session里面有值的话就自动登录,即存放session的隐藏域不为空值*/
	$(function(){
		var username = $('#session_username').val();
		var password = $('#session_password').val();
		var tipinfo = $("#tipinfo").val();
		if(username != ""){
			$('#login').hide();
			$("#header").animate({
				height: '420px'
			});
			$.getJSON('ViewResource/viewInsMostDown',function(data){
				showResponse(data);
			});
		}else if(tipinfo == "请登录"){
			$("#header").animate({
				height: '420px'
			});
		}else{
		}
	});
	function uploadframe(){
		$('#share_button').hide();
		jQuery.noConflict();
		$('#uploadssurface').modal();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	