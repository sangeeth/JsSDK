<%@ page contentType="text/html; charset=ISO-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<ul>
<c:forEach items="${previousPosts}" var="post">
   <li>
     <a name="cmdComment" messageId="${post.id}" href="${pageContext.request.contextPath}/comment?messageId=${post.id}">${post.title}</a>
   </li>
</c:forEach>
</ul>