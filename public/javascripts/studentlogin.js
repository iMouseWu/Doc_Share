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

/*学生登录异步提交，并且显示该学生所在学院的热门资源*/
	$(document).ready(function() {
		$('#loginForms').ajaxForm({
			success : showResponse,
			datatype : 'json'
		});
	});
	
	function showResponse(responseJson) {
		var html = "";
		if (responseJson == null) {
			alert("用户名或者密码错误");
		} else {
			$.each(responseJson[0], function(commentIndex, comment) {
			$("#top_information").empty();
			$("#top_information").append("<li class='navTag'><a href='#' onclick='uploadframe();'>我要上传</a></li>"
					+ "<li class='navTag'><a href='Personal/view_personalinfo?iframe_info=./view_message'>你有"+responseJson[1]+"条消息</a></li>"
					+ "<li class='navTag'><a href='Personal/view_personalinfo?iframe_info=./view_myresources'>个人中心</a></li>");
			$("#suggestorlogin").empty();
			if(commentIndex%2 == 0) 
				html+= '<ul class="suggestColumn suggestCommon">';
			html+='<li class="suggestItem suggestItemCommon"><div class="wantItemHeader"><h3 class="left">'+comment.realName+'</h3></div><br><br><div class="suggestItemBody">简介:'+comment.intro+'</div>'
			html+='<div class="right operation"><span class="tips">'+comment.uploaddate+'</span><span class="tips">'+comment.uploadname+'</span></div></li>';
			if(commentIndex%2 != 0)
				html+='</ul>';
			});
			$("#suggestorlogin").html(html);
			/*联系人的分组显示*/
			$.each(responseJson[2],function(commentIndex,map){
				$('#modal-body').append("<label class='checkbox'>"+
						  "<input type='checkbox' id='group_" + map.group_id + "' onclick='group_click("+ map.group_id +");' name='group_name' value='"+ map.group +"'><a href='#' onclick='friend_state("+ map.group_id +");'>"+ map.group + "</a>" +
				  "</label>");
				$('#modal-body').append("<div id='div_group_" + map.group_id + "'></div>");
				$.each(map.linkman,function(Index,LinkMane){
					$("#div_group_" + map.group_id).append("<label class='checkbox'>"+
							  "<input type='checkbox' name='linkname' class='div_firend_"+ map.group_id + "' value='"+LinkMane.friend_name+"'>"+LinkMane.friend_name+
							  "</label>"); 
				});
				});
		}
	}
	/*如果session里面有值的话就自动登录,即存放session的隐藏域不为空值*/
//	$(function(){
//		var username = $('#session_username').val();
//		var password = $('#session_password').val();
//		if(username != ""){
//			$('#login').hide();
//			$.getJSON('ViewResource/viewInsMostDown',function(data){
//				showResponse(data);
//			});
//		}
//	});
	function uploadframe(){
		$('#share_button').hide();
		jQuery.noConflict();
		$('#uploadssurface').modal();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	