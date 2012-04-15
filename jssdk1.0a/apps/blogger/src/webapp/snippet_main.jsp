<%-- Core with EL --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<c:forEach items="${previousPosts}" var="post">
   <div class="post">
		<a name="postIndex"></a>
   		<h3 class="post-title">${post.title}</h3>
        <div class="post-body">
	      <p>${post.body}</p>
	   </div>
	   <p class="post-footer">
	      <em>posted by ${post.author} @ ${post.time}
	      </em> &nbsp;
	      <a name="cmdComment" messageId="${post.id}" class="comment-link" 
	         href="${pageContext.request.contextPath}/comment?messageId=${post.id}">
			comments
	      </a>
	   </p>
   </div>
</c:forEach>
