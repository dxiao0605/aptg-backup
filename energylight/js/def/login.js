

/* $(document).ready(function () {
$("#login_btn").click(function () {
var username = $("#userName").val().trim();
var password = $("#userPassword").val().trim();

if (username == "admin" && password == "admin") {

window.location = "P2.html";

} else {

msg = "Invalid username and password!";
$("#message").html(msg);

}
});
}); */





$(document).ready(function () {
	
	$('#login_btn').click(function () {

		
		//alert('login_btn');
		
		var username = $("#userName").val().trim();
		var password = $("#userPassword").val().trim();

        //var urlapi = url_login +username+'&password='+password

		$.ajax({
			url: url_login,
			dataType: 'json',
			data: {
				'account' :username,
				'password' :password
			},
			success: function (data) {
				var items = [];

				

				if (data.code == '00')
{
					
					localStorage.setItem('currentid', 'N');
					
					
					//Token =data.msg.token
					localStorage.setItem('Token', data.msg.token);
					
					//UserName=data.msg.username
					localStorage.setItem('username', data.msg.username);
					
					localStorage.setItem('role', data.msg.role);
					
					localStorage.setItem('session', '111');
					
					localStorage.setItem('selcompany', '-1');
					
					
					
				
					
					if(localStorage.getItem('role')=='SuperAdmin')
					window.location = "mainEmeter.html";
					
				
					
}
				else {
					//msg = "Invalid username and password!" + "," + username + "," + password + "," + serveruser + "," + serverpwd;
					msg = "帳號密碼錯誤";
					$("#message").html(msg).css('color', 'red');
				}
			},
			statusCode: {
				404: function () {
					alert('There was a problem with the server.  Try again soon!');
				}
			}
		});
	});
	
	
	
	$("#userPassword").keyup(function(event) {
	    if (event.keyCode === 13) {
	        $("#login_btn").click();
	    }
	});
	
	
	
	
	$('#userName').on('change', function(){
		   var selected = $('#userName option:selected').val();
		   //alert(selected);
		   
		   
		   if(selected =='SuperUser')
			   $('#userPassword').val('SuperUser');
		   
		   if(selected =='aptgadmin')
			   $('#userPassword').val('aptgadmin');
		   
		   if(selected =='adminA')
			   $('#userPassword').val('adminA');
		   
		   
		   if(selected =='userA1')
			   $('#userPassword').val('userA1');
		   
		   if(selected =='userA2')
			   $('#userPassword').val('userA2');
	
	
	});
	
});
