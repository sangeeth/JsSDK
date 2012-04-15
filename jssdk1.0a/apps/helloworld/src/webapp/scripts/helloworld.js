// Define application package
var helloworld = new Package();

// Local variables
var contentArea;

// Import required assemblies
$import("js.ui.event");
$import("js.dwr.core");
$import("js.ui.toolkit");
$import("js.util.bean");

// Import required service interfaces
$importInterface("helloworld.UserManagementService");

// Import required views
$importView("helloworld.homeView",
            "snippet_home.jsp",
            "helloworld_home_view_eventHandler");

$importView("helloworld.mainView",
            "snippet_main.jsp",
            "helloworld_main_view_eventHandler");
            
// Helper functions            
function changeTitle(newTitle) {
	document.title = "Hello World - " + newTitle;
}

// View Event Handlers
function helloworld_home_view_eventHandler() {
	changeTitle("Home");	
	new js.ui.toolkit.Command("cmdShowUsers",function(){
	    window.alert("Hello");
	    helloworld.UserManagementService.getUsers(function(data){
				pageContext["userList"] = data;
				helloworld.mainView.show(contentArea);
	    });
	});
}

// View Event Handlers
function helloworld_main_view_eventHandler() {
	changeTitle("Select Users");
}

js.ui.EventManager.addEventHandler(js.ui.EventManager.EVENT_ONLOAD,
     function(event) {
     	showLogo();
     	js.dwr.core.DWRUtil.useLoadingMessage("Processing...");
		contentArea = document.getElementById("contentArea");
     }
);