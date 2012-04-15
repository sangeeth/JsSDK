<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<p>&nbsp;&nbsp;<a name="cmdBack" href="${pageContext.request.contextPath}/home">&lt;&lt;back</a></p>
<div class="post">
<c:if test="${error != null}">
	<div id="messages">
    	<p style='color:red'>${error}</p>
	</div>
</c:if>
<center>
<form name="frmLogin" method="post" action="${pageContext.request.contextPath}/login">
<table style="color:#333; font-size:97%;" border="0">
	<tr>
		<td>user:</td>
		<td><input id="user" type="text" name="user" size="20" max="60" value="" /></td>
	</tr>
	<tr>
		<td>password:</td>
		<td><input id="password" type="password" name="password" size="20" value="" /></td>
	</tr>
</table>
<input id="cmdLogin" type="submit" value="Login"/>
</form>
</center>
</div>
