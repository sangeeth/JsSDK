<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %> 
<p>&nbsp;&nbsp;<a name="cmdBack" href="${pageContext.request.contextPath}/home">&lt;&lt;back</a></p>
  <div class="post">
     <h3 class="post-title">${message.title}</h3>

    <div class="post-body">
      <p>${message.body}</p>
    </div>

    <p class="post-footer">
      <em>posted by ${message.author} @ ${message.time}</em> &nbsp;
    </p>
    
    <dl id="comments-block">
       <c:forEach items="${message.comments}" var="comment">
	      <dt class="comment-data">
	        At ${comment.time}, ${comment.author} said...
	      </dt>
	      <dd class="comment-body">
	        <p>${comment.body}</p>
	      </dd>
       </c:forEach>
	  <div id="messages"></div>
	  <div class="comment-poster">
		Leave a comment:<br/>
      </div>
	  <form id="commentForm" name="commentForm" action="${pageContext.request.contextPath}/comment" method="post">
	    <input id="messageId" type="hidden" name="messageId" value="${message.id}"/>
		<input id="author" type="text" name="author" size="20" max="60" value=""/> <small>Name (required)<br/></small>
		<input id="email" type="text" name="email" size="20" value=""/> <small>e-mail <br/></small>
		<input id="website" type="text" name="website" size="20" value=""/> <small>Website <br/></small>
		<small><p><b>XHTML:</b> You can use these tags: &lt;a href="" title=""&gt; &lt;abbr title=""&gt; &lt;acronym title=""&gt; &lt;b&gt; &lt;blockquote cite=""&gt; &lt;code&gt; &lt;em&gt; &lt;i&gt; &lt;strike&gt; &lt;strong&gt;</p></small>
		<textarea id="body" name="body" cols="50" rows="8" ></textarea>
		<input id="submitComment" type="submit" value="Submit comment"/>
	   </form>
    </dl>
  </div>