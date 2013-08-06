/*上传列表框二级联动*/
	$(document).ready(function(){
		$.get("@{ViewResource.viewInstitute}",function(data){
			$('#institute_sel').empty();
			$('#institute_sel').append("<option>---请选择---</option>");
			$.each(data,function(commentIndex, comment){
				$('#institute_sel').append("<option>"+data[commentIndex]+"</option>");
			});
		},"json");
	});
	
     $('#institute_sel').change(function(){
    	 $.get("@{ViewResource.viewSubject}",{
    		 institute: $('#institute_sel').val()
    	 },function(data){
    		 $('#subject_sel').empty();
    		 if(data == ""){
    			 $('#subject_sel').append("<option>---请选择---</option>");
    		 }else{
 			$.each(data,function(commentIndex, comment){
 				$('#subject_sel').append("<option>"+data[commentIndex].subject+"</option>");
 			});
 			}
 		},"json");
     });