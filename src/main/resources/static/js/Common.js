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
            	if(result._embedded) {
            		var wsPslList = result._embedded.wsPslDtoList;
                    var rows = '';
                    
                    for(var i = 0; i < wsPslList.length; i++) {
                    	var content = wsPslList[i].content;
                    	var author = wsPslList[i].author;
                    	
                    	if(content.length > 11) content = content.substring(0, 10) + '...';
                    	if(author.length > 7) author = author.substring(0, 6) + '...';
                    	
                        rows += '<tr data-href="' + wsPslList[i]._links.self.href + '" onclick="viewWsPsl(this);">' +
                                    '<td>' + content + '</td>' +
                                    '<td>' + author + '</td>' +
                                '</tr>';
                    }
                    
                    $('#tableBody').append(rows);
                    
                    if(result._links.next) {
                        nextUri = result._links.next.href;
                    } else {
                        nextUri = null;
                    }
            	}
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
            	alert('저장되었습니다.');
                window.location.href = '/ws-service/wsPslList';
            }
        });
	}
}