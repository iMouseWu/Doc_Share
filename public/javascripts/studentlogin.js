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
			$('#wronglogin').text("用户名密码错误");
		} else {
			$.each(responseJson[0], function(commentIndex, comment) {
				html += "<div class='doc_resourcedetails'>"+
				"<a href=@{ViewResource.viewDownloadsDetails}?hashName="+comment.hashName + ">" 
						+ comment.realName 
						+"</a>" 
						+"</div><br>";
			});
			$('#wronglogin').text("");
			$('#suggest').html(html);
			var html1 = "<div id='uploadsbutton'><span class='label label-info'>欢迎你</span><br><br>"
			html1 += "<button class='btn btn-info btn-large btn-block' type='button' data-toggle='modal' data-target='#uploadssurface';>我要上传</button></div>";
			html1 += "<a href='SeekHelp/seek_home?page=1'>找不到资源？去资源区！</a>";
			html1 += "<a href='Personal/view_personalinfo?iframe_info=./view_message'>你有"+responseJson[1]+"条消息</a>";
			html1 += "<a href='Personal/view_personalinfo?iframe_info=./view_myresources'>个人中心</a>"
			$('#login_uploads').html(html1);
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
	$(function(){
		var username = $('#session_username').val();
		var password = $('#session_password').val();
		if(username != ""){
			$('#login').hide();
			$.getJSON('ViewResource/viewInsMostDown',function(data){
				showResponse(data);
			});
		}
	});
	
	
	
	
	
	
	
	
	
	
	
	
	