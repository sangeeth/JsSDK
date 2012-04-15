<%-- Core with EL --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<c:forEach items="${userList}" var="user">
  <input id="cmdUser" type="checkbox" value="${user.name}"/>${user.name}<br/>
</c:forEach>