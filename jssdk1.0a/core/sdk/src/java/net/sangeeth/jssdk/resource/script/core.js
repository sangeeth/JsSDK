/**
* Package class definition
* @author Sangeeth Kumar .S
*/
function Package()
{
   this.$import = Package_import;
   this.load = Package_load;
   this.$export = Package_export;
   this.$importView = Package_importView;
   this.cache = new Array();
}
function Package_export(clazz) {
   var tokens = clazz.split(".");
   var pkgName = "";
   var pkg = this;
   for(var i=0;i<tokens.length;i++)
   {
      pkgName = tokens[i];
      if (i==tokens.length-1) {
		  pkg[pkgName] = function(){};
	  }
	  else {
	    if (pkg[pkgName] == null) {
	       pkg[pkgName] = new Package();
	    } else {
	       pkg = pkg[pkgName];
	    }
	  }
   }
}
function Package_importView(viewId,viewPath,eventHandler) {
	var url = pageContext.request.contextPath 
	          + "/jssdk/view?viewPath=" + viewPath 
	          + "&viewId=" + viewId;
	if (eventHandler) {   	          
	    url += "&eventHandler=" + eventHandler;
	}
	if(this.cache[url] == null) {
	   var scriptCode = this.load(url);
	   if (scriptCode) {
	   	  window.eval(scriptCode);
	   }
	   this.cache[url] = true;
	}
}
function Package_import(assembly,basePath,preserveName) {
   assembly = assembly.replace(/\*/g,"__wildcard"); 
   if (!basePath || typeof basePath == 'undefined') {
   	  basePath = "/jssdk/script";
   }
   var newName = preserveName?assembly:assembly.replace(/\./g,"/");
   var file = pageContext.request.contextPath + basePath + "/" + newName + ".js";
   
   if(this.cache[file] == null) {
       var script = this.load(file);
       if (script) {  window.eval(script); }
       else { throw new Error(200,assembly + " not found."); }
       this.cache[file] = true;
   }
}
function Package_load( url ) {
        var req;
        // branch for native XMLHttpRequest object
        if (window.XMLHttpRequest) {
            req = new XMLHttpRequest();
            req.open("GET", url, false);
            req.send(null);
        // branch for IE/Windows ActiveX version
        } else if (window.ActiveXObject) {
            req = new ActiveXObject("Msxml2.XMLHTTP");
            if (req) {
                req.open("GET", url, false);
                req.send();
            }
        }
        
        var content;
        content = (req.status != "200")?null:req.responseText;
        req = null;
                
        return content;
}
function $import(assembly){   
  var index = assembly.indexOf(".");
  var basePkg = assembly.substring(0,index);
  window.eval(basePkg+".$import('"+assembly+"');");
}
function $export(assembly){ 
  var index = assembly.indexOf(".");
  var clazz = assembly.substr(index+1);
  var basePkg = assembly.substring(0,index);
  window.eval(basePkg+".$export('"+clazz+"');");
}
function $importView(viewId,viewPath,eventHandler) {
try {
	var index = viewId.indexOf(".");
  var basePkg = viewId.substring(0,index);
  
   if (eventHandler) {
      window.eval(basePkg+".$importView('"+viewId +"','"
  								   +viewPath +"','"
  								   +eventHandler
  								   +"');");
   } else {
      window.eval(basePkg+".$importView('"+viewId +"','"
  								   +viewPath +"');");
   }
} catch(E){
	alert("Error " + E.description);
}
}

function $importInterface(serviceInterface) {
  var index = serviceInterface.indexOf(".");
  var basePkg = serviceInterface.substring(0,index);
  window.eval(basePkg+".$import('"+serviceInterface+"','/dwr/interface',true);");
}

function __PageContext() {
   this.setProperty = __PageContext_setProperty;
   this.getProperty = __PageContext_getProperty;   
   this.getPath = __PageContext_getPath;
   this.request = {contextPath: ""};
   this.path = null;
}
function __PageContext_setProperty(key,value) {
   this[key] = value;
}
function __PageContext_getProperty(key) {
   return this[key];
}
function __PageContext_getPath(){
   if (!this.path) {
   	   var url = window.document.location.toString();
	   var index = url.indexOf("://");
	   index = url.indexOf("/",index+3);
	   var endIndex = url.indexOf("?",index+1);
	   this.path = url.substring(index,endIndex);   	
   }
   return this.path;
}
var pageContext = new __PageContext();

var js = new Package();

function showLogo() {
  var logo = document.createElement('div');
  logo.style.position = "absolute";
  logo.style.top = "0px";
  logo.style.left = "0px";
  logo.style.background = "#EEEECC";
  logo.style.color = "#123";
  logo.style.fontFamily = "Arial,Helvetica,sans-serif";
  logo.style.padding = "4px";
  document.body.appendChild(logo);
  var text = document.createTextNode("Powered by JsSDK");
  logo.appendChild(text);
}

document.cookie="jssdkver=1.0.0;path=/";


/**
 * Find the element in the current HTML document with the given id or ids
 * @see http://getahead.ltd.uk/dwr/browser/util/$
 */
var $;
if (!$ && document.getElementById) {
  $ = function() {
    var elements = new Array();
    for (var i = 0; i < arguments.length; i++) {
      var element = arguments[i];
      if (typeof element == 'string') {
        element = document.getElementById(element);
      }
      if (arguments.length == 1) {
        return element;
      }
      elements.push(element);
    }
    return elements;
  }
}
else if (!$ && document.all) {
  $ = function() {
    var elements = new Array();
    for (var i = 0; i < arguments.length; i++) {
      var element = arguments[i];
      if (typeof element == 'string') {
        element = document.all[element];
      }
      if (arguments.length == 1) {
        return element;
      }
      elements.push(element);
    }
    return elements;
  }
}