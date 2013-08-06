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
			$.each(responseJson, function(commentIndex, comment) {
				html += "<div class='doc_resourcedetails'>"+
				"<a href=@{ViewResource.viewDownloadsDetails}?hashName="+responseJson[commentIndex].hashName + ">" 
						+ responseJson[commentIndex].realName 
						+"</a>" 
						+"</div><br>";
			});
			$('#wronglogin').text("");
			$('#suggest').html(html);
			var html1 = "<div id='uploadsbutton'><span class='label label-info'>欢迎你</span><br><br>"
			html1 += "<button class='btn btn-info btn-large btn-block' type='button' data-toggle='modal' data-target='#uploadssurface';>我要上传</button></div>";
			$('#login').html(html1);
		}
	}