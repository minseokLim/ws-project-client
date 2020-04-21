var commonJS = {
	post_to_url : function(path, params) {
        var form = document.createElement("form");
        form.setAttribute("method", "post");
        form.setAttribute("action", path);
     
        //히든으로 값을 주입시킨다.
        for(var key in params) {
            var hiddenField = document.createElement("input");
            hiddenField.setAttribute("type", "hidden");
            hiddenField.setAttribute("name", key);
            hiddenField.setAttribute("value", params[key]);
     
            form.appendChild(hiddenField);
        }
     
        document.body.appendChild(form);
        form.submit();
	},
	
	getRequestAtApi : function(apiUrl, callbackFunc) {
		$.ajax({
			url: '/api',
            type: 'GET',
            data: {
                apiUrl: apiUrl
            },
            success: function(result) {
            	callbackFunc(result);
            },
            error:function(request, status, error){
            	alert("code : " + request.status + "\n" + "message : " + request.responseText + "\n" + "error : " + error);
            }
		});
	},
	
	postRequestAtApi : function(apiUrl, callbackFunc, data) {
		
		data.apiUrl = apiUrl;
		
		$.ajax({
            url: '/api',
            type: 'POST',
            data: JSON.stringify(data),
            contentType : "application/json; charset=UTF-8",
            success: function(result) {
            	callbackFunc(result);
            },
            error:function(request, status, error){
            	alert("code : " + request.status + "\n" + "message : " + request.responseText + "\n" + "error : " + error);
            }
        });
	},
	
	putRequestAtApi : function(apiUrl, callbackFunc, data) {
		data.apiUrl = apiUrl;
		
		$.ajax({
            url: '/api',
            type: 'PUT',
            data: JSON.stringify(data),
            contentType : "application/json; charset=UTF-8",
            success: function(result) {
            	callbackFunc(result);
            },
            error:function(request, status, error){
            	alert("code : " + request.status + "\n" + "message : " + request.responseText + "\n" + "error : " + error);
            }
        });
	}
}