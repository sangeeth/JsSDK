$export("js.ui.UIManager");
$export("js.ui.FormManager");
$export("js.ui.Component");
$export("js.ui.Form");
$export("js.ui.toolkit.Command");
$export("js.ui.toolkit.View");
$import("js.ui.event");


js.ui.toolkit.View = function(renderer,eventHandler) {
	this.renderer = renderer;
	this.show = View_show;
	this.eventHandler = eventHandler;
}
function View_show(container) {
	if (this.renderer) {
		var html = this.renderer(pageContext);
		container.innerHTML = html;
		if (this.eventHandler) {
			this.eventHandler();
		}
	}
}


js.ui.toolkit.Command = function(id,onclick) {
   this.id = id;
   var elements = window.document.getElementsByName(id);
   if (elements) {
  	  for(var i=0;i<elements.length;i++) {
	 	  js.ui.EventManager.addClickEventHandler(elements[i],
												   onclick);
  	  }
   }
}
/**
 * The UI Manager class.
 * This maintains a registry of UI components and their UI template or AjaxPage
 */
function __UIManager() {
	this.registry = new Array(); 
	this.setUI = __UIManager_setUI;
	this.getUI = __UIManager_getUI;
}
/**
 * @param {String} key
 * @param {String} template
 */
function __UIManager_setUI(key,template) {
	//TODO use js.ui.toolkit.View
	this.registry[key]=null;
	
}
/**
 * @return {Processor} AjaxPage Processor
 */
function __UIManager_getUI(key) {
	return this.registry[key];
}
js.ui.UIManager = new __UIManager();

/**
 * Super class of all Components
 */
js.ui.Component = function() {
	this.cid = "component";
}
/**
 * The Base Form class
 * @author Sangeeth Kumar .S
 * @constructor
 * @param {String} gui Ajax Page template location
 */
js.ui.Form = function() {
   this.srcElement = null;	 
//   this.cid = "form";
   this.close = Form_close;
   this.show = Form_show;
   this.save = Form_save;
   this.onShow = Form_onShow;
}
js.ui.Form.prototype = new js.ui.Component();
js.ui.Form.prototype.cid = "form";
function Form_close() {
   var ui = document.getElementById(this.cid);
   if (ui) {
	   ui.outerHTML = "";
   }
}
function Form_show(srcElement) { 
   // close the form if open
   this.close();	
   this.srcElement = srcElement;
   
   this.onShow();
   var processor = js.ui.UIManager.getUI(this.cid);
   this.srcElement.insertAdjacentHTML("AfterEnd",processor(this));
}
function Form_save() {
}
function Form_onShow() {
}

/**
 * FormFactory class definition.
 * @author Sangeeth Kumar .S
 */
function __FormManager() {
	this.registry = new Array();
	this.cache = new Array();
	this.getForm = __FormManager_getForm;
	this.registerFormClass = __FormManager_registerFormClass;
	this.saveForm = __FormManager_saveForm;
	this.closeForm = __FormManager_closeForm;
}
js.ui.FormManager = new __FormManager();
/**
 * By default this method returns a shared instance 
 * of given form type.
 */
function __FormManager_getForm(type) {
  var form = this.cache[type];
  if (!form)
  {
       var formClass = this.registry[type];
       if (formClass) {
		   eval("form = new " + formClass + "();");
		   this.cache[type] = form;
       }
  }
  return form;
}
function __FormManager_registerFormClass(formClass){
	if (formClass) {
		var instance = null;
		try {
			eval("instance = new " + formClass +"();");
			this.registry[instance.cid] = formClass;
		} catch(Error) {
		}
	}
}
function __FormManager_saveForm(jsform) {
   var instance = this.getForm(jsform);
   if (instance) {
       var form = document.getElementById(jsform + "_form");
       if (form) {
	   	   instance.save(form);
       }
   	   instance.close();   	   
   }
}
function __FormManager_closeForm(jsform) {
   var instance = this.getForm(jsform);
   if (instance) {
   	   instance.close();
   }
}
js.ui.EventManager.addEventHandler(js.ui.EventManager.EVENT_ONCLICK,
			function(event) {
			   var el = event.srcElement?event.srcElement:event.target;
			   var jsform = el.getAttribute("js:form");
			   if (jsform)
			   {
			      frm = js.ui.FormManager.getForm(jsform);
			      frm.show(el);
			      js.ui.EventManager.cancel(event);
			   }
			});
