<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<p>&nbsp;&nbsp;<a name="cmdBack" href="${pageContext.request.contextPath}/home">&lt;&lt;back</a></p>
<div class="post">
<form name="frmManage" action="${pageContext.request.contextPath}/manage?action=new" method="post">
   <input name="cmdPost" type="submit" value="New Post"/>
</form>   
<table style="color:#333; font-size:97%;" border="0">
	<c:forEach items="${previousPosts}" var="post">
		<tr>
			<td>
				<div class="item-control">
				   <a name="cmdEdit" class="item-control" messageId="${post.id}" href="${pageContext.request.contextPath}/manage?action=edit&messageId=${post.id}">
					<span class="quick-edit-icon">&nbsp;</span>
				   </a> 
				   <a name="cmdDelete" class="item-control" messageId="${post.id}" href="${pageContext.request.contextPath}/manage?action=delete&messageId=${post.id}"> 
					  <span class="delete-post-icon">&nbsp;</span> 
				   </a></div>
				</td>
				<td>${post.title}</td>
		</tr>
	</c:forEach>
</table>
</div>
