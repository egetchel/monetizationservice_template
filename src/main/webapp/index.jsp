<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.ezpaymentprocessing.utils.ConfigManager" %> 

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="//code.jquery.com/jquery-2.1.3.min.js"></script>
<title>Welcome to <%=ConfigManager.getGearName() %></title>
<style>
body
{
	font-family: Arial;
	font-size: 1em;
}
input, textarea, select
{
	font-family: Arial;
	font-size: 1em;

}
.black-border
{
	 border: 1px solid black;
	 padding: 5px 5px 5px 5px;
	 
}
.center
{
	align:center;
}
.text-center
{
	text-align:center;
}
.wrapper
{
	width:600px;
}
.upperCornersRound, .allCornersRound {
	border-top-left-radius: 0.5em;
	border-top-right-radius: 0.5em;
	-moz-border-radius-topleft: 0.5em;
	-moz-border-radius-topright: 0.5em;
	-webkit-border-top-left-radius: 0.5em;
	-webkit-border-top-right-radius: 0.5em;
}
.lowerCornersRound, .allCornersRound {
	border-bottom-left-radius: 0.5em;
	border-bottom-right-radius: 0.5em;
	-moz-border-radius-bottomleft: 0.5em;
	-moz-border-radius-bottomright: 0.5em;
	-webkit-border-bottom-left-radius: 0.5em;
	-webkit-border-bottom-right-radius: 0.5em;
}
</style>
</head>
<body>
<div class="">
<div class="black-border text-center upperCornersRound">
Welcome to the <%=ConfigManager.getGearName()%> mobile app
</div>
<div class="black-border lowerCornersRound">
<img src="images/Red_Hat_RGB_150px.jpg" align="right"/>
<form id="purchaseForm" method="post" action="PurchaseServlet"> 
<input type="hidden" name="merchantId" id="merchantId" value="<%=ConfigManager.getGearName()%>">
<br/>
<table>
	<tr>
		<td>Amount:</td>
		<td>	
			<select name="amount" id="amount">
	  		<option value="10">Earphones - $10</option>
	  		<option value="50">Bluetooth Receiver - $50</option>
  			<option value="2500">Anthem Preamplifier - $2,500</option>
			</select>
		</td>
	 </tr>
	<tr>
		<td>Mobile Number:</td>
		<td><input type="text" name="mobileNumber" id="mobileNumber" value="6173124838"></td>
	 <tr>
	 	<td>&nbsp;</td>
	 	<td>
			<button type="button" id="submitButtonRest" onclick="javascript:submitRestPost('PurchaseServlet');">Purchase</button>
		</td>
	</tr>
</table>		
</form>
</div>
<div id="result">&nbsp;</div>
</div>
<script type="text/javascript">
function submitRestPost(restEndpoint) 
{
	//alert ("invoking " + restEndpoint);
	var frm = $("#purchaseForm");
	var data = frm.serialize();
	$( "#result" ).html("");
	$("#endpoint").html(restEndpoint);
	$("#requestData").html( data );
	$.ajax({
		  url:restEndpoint,
		  type:"POST",
		  data:data,
		  success: function(response)
		  {
			  // Invoked if we have a valid payload back
			  var approved = response.approved;
			  var message  = response.message;
			  var consoleMessage;
			  if (approved) {
				consoleMessage = "Your purchase was approved";
			  }
			  else
			  {
				  consoleMessage ="Purchase denied. "+ message;
			  }
		      //var consoleMessage = "Purchase Approved " + response.approved + " Reason: " + response.message; 
			  $( "#result" ).html( consoleMessage );
			  
		  }
		});

}

$.fn.serializeObject = function()
{
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

</script>

</body>
</html>