/*
 * jQuery File Upload Plugin JS Example 6.7
 * https://github.com/blueimp/jQuery-File-Upload
 *
 * Copyright 2010, Sebastian Tschan
 * https://blueimp.net
 *
 * Licensed under the MIT license:
 * http://www.opensource.org/licenses/MIT
 */

/*jslint nomen: true, unparam: true, regexp: true */
/*global $, window, document */

$(function () {
    'use strict';
    // Initialize the jQuery File Upload widget:
    $('#fileupload')
    .fileupload({
    	dataType : 'json',
    	autoUpload: false,
    	acceptFileTypes: /(\.|\/)(jpg)$/i,
//        // The following option limits the number of files that are
//        // allowed to be uploaded using this widget:
//        maxNumberOfFiles: undefined,
//        // The maximum allowed file size:
//        maxFileSize: undefined,
//        // The minimum allowed file size:
//        minFileSize: undefined,
//        // The regular expression for allowed file types, matches
//        // against either file type or file name:
//        acceptFileTypes:  /.+$/i,
//        // The regular expression to define for which files a preview
//        // image is shown, matched against the file type:
//        previewSourceFileTypes: /^image\/(gif|jpeg|png)$/,
//        // The maximum file size of images that are to be displayed as preview:
//        previewSourceMaxFileSize: 5000000, // 5MB
//        // The maximum width of the preview images:
//        previewMaxWidth: 80,
//        // The maximum height of the preview images:
//        previewMaxHeight: 80,
//        // By default, preview images are displayed as canvas elements
//        // if supported by the browser. Set the following option to false
//        // to always display preview images as img elements:
//        previewAsCanvas: true,
//        // The ID of the upload template:
//        uploadTemplateId: 'template-upload',
//        // The ID of the download template:
//        downloadTemplateId: 'template-download',
//        // The container for the list of files. If undefined, it is set to
//        // an element with class "files" inside of the widget element:
//        filesContainer: undefined,
//        // By default, files are appended to the files container.
//        // Set the following option to true, to prepend files instead:
//        prependFiles: false,
        add: function (e, data) {
        	var uploadFile = data.files[0];
        	if (!(/(zip)|(rar)$/i).test(uploadFile.name)) {
        		alert("文件类型错误");
        		$('#upfile').val("");
            }else{
        	$('#mes').html("简介(30字以内):<br><textarea rows='2'name='intro_sel' style='resize:none' maxlength='30'></textarea>");
        	$('#add_success').html("文件添加成功");
        	$('#progress').html('<div class="progress progress-striped" id="proessview">');
            $('#proessview').html("<div class='bar' id='bar'></div>");
            $('#submit').html("<button class='btn btn-primary' id='submitbutton'>上传</button>");
            $('#submitbutton').bind("click",function(){
            	data.context = $('#submit').html("上传中");/*这个蛋疼的东西是什么，谁能告诉我，英语差就是看不懂啊*/
                data.submit();
            });
            $('#mes').fadeIn();
            }
        },
        progress: function (e, data) {
            if (data.context) {
                var progress = parseInt(data.loaded / data.total * 100, 10);
                    $('#bar').css(
                        'width',
                        progress + '%'
                    );
            }
        },
        done:function(e,data){
        	data.context.html('上传成功');
        	 $('#mes').html("<div class='alert alert-success'> <h4>Well Done</h4>Alert Your File Name?" +
       			  "<button type='button' class='close' data-dismiss='alert'>&times;</button>"+
       			  "<form action='Uploads/alterFilename' id='alertfileform'><input type='text' name='filename' value='"+ data.result[0].realName +"'>"
       			  +"<input type='hidden' value='"+data.result[0].id+"' name='id'><br><input type='submit' value='修改' class='btn btn-danger'></form>"+
       			  "</div>");
       			 $('#alertfileform').ajaxForm(function(){
       				 $('#mes').html("<div class='alert alert-success'>恭喜你！修改成功！<br></div>")
       			 });
       			$('#upfile').val("");
       			$('#add_success').html("请添加文件");
       			$('#progress').html("");
        },
        send:function(e, data){
        	if ($("#institute_sel").val() == "---请选择---") {
        		alert("请选择学院");
        		$('#progress').html("");
        		$('#proessview').html("");
        		$('#submit').html("");
        		$('#add_success').html("请添加文件");
        		$('#mes').fadeOut(1);
        		return false;
            }
        },
    });
    

});
