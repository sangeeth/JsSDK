// Define application package
var blogger = new Package();

// Local variables
var contentArea;
var linksArea;
var recentlyArea;

// Import required assemblies
$import("js.ui.event");
$import("js.dwr.core");
$import("js.ui.toolkit");
$import("js.util.bean");

// Import required service interfaces
$importInterface("blogger.BlogService");
$importInterface("blogger.BlogLoginService");

// Import required views
$importView("blogger.homeView",
            "snippet_main.jsp",
            "blogger_main_view_eventHandler");

$importView("blogger.commentsView",
            "snippet_comments.jsp",
            "blogger_comments_view_eventHandler");

$importView("blogger.loginView",
            "snippet_login_form.jsp",
            "blogger_login_view_eventHandler");

$importView("blogger.manageView",
            "snippet_manage.jsp",
            "blogger_manage_view_eventHandler");

$importView("blogger.linksView",
            "snippet_links.jsp");

$importView("blogger.recentlyView",
            "snippet_previous_posts.jsp",
            "blogger_recently_view_eventHandler");
            
$importView("blogger.editView",
            "snippet_edit_post.jsp",
            "blogger_edit_view_eventHandler");
            
// Helper functions            
function changeTitle(newTitle) {
	document.title = "JsSDK Blog - " + newTitle;
}
function addMessage(message) {
	if (message) {
		var messages = pageContext["previousPosts"];
		messages[messages.length] = message;		
	}
}
function updateMessage(message) {
	if (message) {
		var messages = pageContext["previousPosts"];
		if (messages) {
			for(var i=0;i<messages.length;i++) {
				if (messages[i].id == message.id) {
					messages[i] = message;
					break;
				}
			}
		}
	}
}

function deleteMessage(messageId) {
	if (messageId) {
		var removed = false;
		var messages = pageContext["previousPosts"];	
		for(var i=0;i<messages.length;i++) {
			if (messages[i].id == messageId) {
				removed = true;
			}	
		    if (removed && i<messages.length-1) {
		       messages[i] = messages[i+1];
			}
		}
		--messages.length;
	}
}

function findMessage(messageId) {
	var message;
	if (messageId) {
		var messages = pageContext["previousPosts"];
		if (messages) {
			for(var i=0;i<messages.length;i++) {
				if (messages[i].id == messageId) {
					message = messages[i];
					break;
				}
			}
		}
	}
	return message;
}
// View Event Handlers
function blogger_main_view_eventHandler() {
	changeTitle("Home");	
	 new js.ui.toolkit.Command("cmdComment",function(event,srcElement){
		var messageId = srcElement.getAttribute("messageId");
		var message = findMessage(messageId);
		if (message) {
			pageContext["message"] = message;
			blogger.commentsView.show(contentArea);
		}
	});
	new js.ui.toolkit.Command("cmdAdmin",function(){
		if(pageContext["userId"]) {
			blogger.manageView.show(contentArea);
		} else {
			pageContext["error"] = null;
			blogger.loginView.show(contentArea);
		}
	});
}
function blogger_comments_view_eventHandler() {
	changeTitle("Comments");
	new js.ui.toolkit.Command("cmdBack",function(){
		blogger.homeView.show(contentArea);
	});
	new js.ui.toolkit.Command("commentForm",function(event,form){
		try {
			var commentBean = { messageId : null,
							author: "", 
		                    email: "",
		                    website: "",
		                    body: ""};
			
			js.util.BeanUtil.formToBean(form,commentBean);
	
		    blogger.BlogService.addComment(function(){
					    blogger.BlogService.getMessage(function(message){
							pageContext["message"] = message;
							updateMessage(message);
							blogger.commentsView.show(contentArea);
					    },commentBean.messageId);
				    },
		    		commentBean.messageId,
		    		commentBean.author, 
		    		commentBean.email, 
		    		commentBean.website, 
		    		commentBean.body);
		} catch(Exception) {
			alert(Exception.description);
		}
	});
}

function blogger_login_view_eventHandler() {
	changeTitle("Admin Login");
	new js.ui.toolkit.Command("frmLogin",function(event,form) {
		var bean = { user : null,
					 password: null};
		js.util.BeanUtil.formToBean(form,bean);
		try {
		    blogger.BlogLoginService.login(
		    		bean.user,
		    		bean.password,{
		    			callback: function(success) { 
		    				      pageContext["userId"] = bean.user;
				     	          blogger.manageView.show(contentArea);
		   		        },
		   		        errorHandler: function(errorString,exception) {
							pageContext["error"] = errorString;
							blogger.loginView.show(contentArea);
		   		        }
		    });
		} catch (Exception) {
			alert(Exception.description);
		}
	});		
	new js.ui.toolkit.Command("cmdBack",function(){
		blogger.homeView.show(contentArea);
	});	
}

function blogger_manage_view_eventHandler() {
	changeTitle("Admin Manage");
	new	js.ui.toolkit.Command("frmManage",function(){
		pageContext["message"] = {id: null, title: "", body: ""};
		blogger.editView.show(contentArea);
	});
    new	js.ui.toolkit.Command("cmdEdit",function(event,element){
    	var messageId = element.getAttribute("messageId");
   		var message = findMessage(messageId);
   		if (message) {
			pageContext["message"] = message;
			blogger.editView.show(contentArea);
    	}
    });
    new	js.ui.toolkit.Command("cmdDelete",function(event,element){
    	var messageId = element.getAttribute("messageId");
   		var message = findMessage(messageId);
   		if (message) {
		    blogger.BlogService.removeMessage(function(){
		    	    deleteMessage(messageId);
					blogger.manageView.show(contentArea);
					blogger.recentlyView.show(recentlyArea);
		    },message.id);		
    	}
    });
	new js.ui.toolkit.Command("cmdBack",function(){
		blogger.homeView.show(contentArea);
	});    
}

function blogger_edit_view_eventHandler() {
	changeTitle("Admin Add/Edit Post");	
	new js.ui.toolkit.Command("frmEdit", function(event,form) {
		var userId = pageContext["userId"];
		var bean = {messageId : null, title: null, body: null};
		js.util.BeanUtil.formToBean(form,bean);
		
		var message = findMessage(bean.messageId);
		if (message) {
			message.title = bean.title;
			message.body = bean.body;
		    blogger.BlogService.updateMessage(function(message){
				blogger.manageView.show(contentArea);
				blogger.recentlyView.show(recentlyArea);
		    },userId,message.id,bean.title,bean.body);		
		} else {
		    blogger.BlogService.publish(function(message){
		    	addMessage(message);
				blogger.manageView.show(contentArea);
				blogger.recentlyView.show(recentlyArea);
		    },userId,bean.title,bean.body);		
		}
		return false;		
	});
	new js.ui.toolkit.Command("cmdBack",function(){
		blogger.manageView.show(contentArea);
	});	
}

function blogger_recently_view_eventHandler() {
	 new js.ui.toolkit.Command("cmdComment",function(event,srcElement){
		var messageId = srcElement.getAttribute("messageId");
		var message = findMessage(messageId);
		if (message) {
			pageContext["message"] = message;
			blogger.commentsView.show(contentArea);
		}
	});
}

js.ui.EventManager.addEventHandler(js.ui.EventManager.EVENT_ONLOAD,
     function(event) {
     	showLogo();
     	js.dwr.core.DWRUtil.useLoadingMessage("Processing...");
		contentArea = document.getElementById("main3");
		recentlyArea = document.getElementById("recently");
		linksArea = document.getElementById("links");		

	    blogger.BlogService.getMessages(function(data){
				pageContext["previousPosts"] = data;
				
				blogger.homeView.show(contentArea);
				blogger.recentlyView.show(recentlyArea);
	    });
     }
);