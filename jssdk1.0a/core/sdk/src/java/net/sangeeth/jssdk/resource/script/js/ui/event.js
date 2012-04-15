$export("js.ui.EventManager");

function __EventManager() {
	this.EVENT_ONLOAD = "onload";
	this.EVENT_ONCLICK = "onclick";
	this.addEventHandler = __EventManager_addEventHandler;
	this.addClickEventHandler = __EventManager_addClickEventHandler;
	this.cancel = __EventManager_cancel;
}
js.ui.EventManager = new __EventManager();
/**
 * @param handler function pointer which
 *                has to invoked when document.onload/window.onload 
 * 	              event gets triggered
 */
function __EventManager_addEventHandler(eventType,handler) {
	if (window.name == '___swapspace___')  {
		return;
	}
	
	if (eventType==this.EVENT_ONLOAD) {
			if (window.addEventListener) {
			  window.addEventListener("load", handler, false);
			} else if (window.attachEvent) {
			  window.attachEvent("onload", handler);
			} else {
			  window.onload = handler;
			}		
	} else if (eventType==this.EVENT_ONCLICK) {
			if (document.addEventListener) {
			  document.addEventListener("click", handler, false);
			} else if (window.attachEvent) {
			  document.attachEvent("onclick", handler);  
			} else {
			  document.onclick = handler;  
			}		
	}
}
function __EventManager_addClickEventHandler(element,pHandler) {
    var handler = function(event) {
          var observer = event.srcElement?event.srcElement:event.target;
          try {
	    	  pHandler(event,element,observer);
          } catch (Exception) {
          	 alert(Exception.description);
          }
	   	  js.ui.EventManager.cancel(event);
	      return false; 
    };
    if (element.nodeName == 'FORM') {
		if (element.addEventListener) {
		  element.addEventListener("submit", handler, false);
		} else if (element.attachEvent) {
		  element.attachEvent("onsubmit", handler);  
		} else {
		  element.onsubmit = handler;  
		}		
    } else {
		if (element.addEventListener) {
		  element.addEventListener("click", handler, false);
		} else if (element.attachEvent) {
		  element.attachEvent("onclick", handler);  
		} else {
		  element.onclick = handler;  
		}		
    }
}
function __EventManager_cancel(event) {
	if (typeof event.preventDefault != 'undefined') {
		event.preventDefault();
	} else { event.returnValue = false; }
}