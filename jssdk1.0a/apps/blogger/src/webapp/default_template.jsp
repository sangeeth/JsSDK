<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
  <title>JsSDK Blog - ${param.title}</title>
  <link href="styles/style.css" type="text/css" rel="stylesheet"/>
  <c:if test="${jssdkver != null}">
	<script type="text/javascript" src="${pageContext.request.contextPath}/jssdk"></script>
	<script type="text/javascript" src="scripts/blogger.js"></script>
  </c:if>
</head>
<body>
     <div id="content">
	     <jsp:include page="header.jsp"/>
		 <div id="main">
		   <div id="main2">
		      <div id="main3">	     
	     		 <jsp:include page="${param.content}"/>
	     	  </div>
	       </div>
	     </div>
	     <div id="sidebar">
	        <jsp:include page="profile.jsp"/>
			<div class="box">
			   <div class="box2">
			      <div class="box3">
			        <h2 class="sidebar-title">Links</h2>
			        <div id="links">
				        <jsp:include page="snippet_links.jsp"/>
					</div>
				    <h2 class="sidebar-title">Previous Posts</h2>
				      <div id="recently">
				        <jsp:include page="snippet_previous_posts.jsp"/>				      
				      </div>
				    <h2 class="sidebar-title">Meta</h2>
			  	    <a name="cmdAdmin" href="${pageContext.request.contextPath}/admin">Admin</a>
			     </div>
			   </div>
			</div>
	     </div>
 	     <jsp:include page="footer.jsp"/>
	  </div>
</body>
</html>