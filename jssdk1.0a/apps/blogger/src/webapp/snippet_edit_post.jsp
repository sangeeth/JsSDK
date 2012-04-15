<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %> 
<form id="frmEdit" name="frmEdit" method="post" action="${pageContext.request.contextPath}/manage?action=publish">
<p>&nbsp;&nbsp;<a name="cmdBack" href="${pageContext.request.contextPath}/manage">&lt;&lt;back</a></p>
  <div class="post">
    <dl id="comments-block">
		Title: <input type="text" name="title" size="20" value="${message.title}"/> 
		<textarea id="body" name="body" cols="50" rows="15" >${message.body}</textarea>    
		<input type="submit" name="publish" value="Publish"/>
		<input type="hidden" name="messageId" value="${message.id}"/>		
    </dl>
  </div>
</form>