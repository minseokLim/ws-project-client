var commonJS = {
	post_to_url : function(path, params) {
		if(typeof params === 'undefined') {
			params = new Object();
		}
	
		var token = $("meta[name='_csrf']").attr("content");
		var csrfParameter = $("meta[name='_csrf_parameter']").attr("content");
		params[csrfParameter] = token;
		
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
	
	requestAtApi : function(apiUrl, callbackFunc, method, data) {
		method = method || "GET"; // Set method to GET by default, if not specified
		
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		
		$.ajax({
			url: '/api?apiUrl=' + encodeURIComponent(apiUrl),
            type: method,
            data: JSON.stringify(data),
            beforeSend : function(xhr) {
            	/*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
				xhr.setRequestHeader(header, token);
            },
            async: false,
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