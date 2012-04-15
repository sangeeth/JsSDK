$export("js.util.BeanUtil");
$import("js.reflect");

/**
 * Bean creation Utility 
 * @author Sangeeth Kumar .S
 */
function __BeanUtil() {
//private:	
	this.count = 0; 
//public:	
	this.genID = __BeanUtil_genID;
	this.formToBean = __BeanUtil_formToBean;
	this.elementToBean = __BeanUtil_elementToBean;
	this.setProperty = __BeanUtil_setProperty;
	this.toText = __BeanUtil_toText;
}
js.util.BeanUtil = new __BeanUtil();

function __BeanUtil_genID() {
	this.count++;
	this.count%=100;
	return new Date().getTime() + "" + this.count;
}
/**
 * read the values from input/select tags and fill
 * the given bean.
 */
function __BeanUtil_formToBean(form,bean) {
   // populate each property of the bean
   for(prop in bean)  {
      try  {
      	 var el = form.elements[prop.toString()];
      	 if (el!=null)
      	 {
      	   var newValue = null;	 
      	   if (el.tagName == "INPUT") {
      	      if(el.type=="text" 
      	      	 || el.type=="hidden" 
      	      	 || el.type=="password") {
      	      	 newValue = el.value;
		      }
      	      else if (el.type=="checkbox" 
	      	      	   || el.type=="radio") {
		    	 newValue = el.checked;
		      }
		   }
           else if (el.tagName=="SELECT" || el.tagName == "TEXTAREA"){
           	   newValue = el.value;
		   }
	   	   this.setProperty(bean,prop,newValue);
	     }
      } catch(Ex) {    
      	 // need not handle
      }
   }
   return bean;
}
function __BeanUtil_elementToBean(element,bean,namespace) {
   // populate each property of the bean
   // namespace of each custom attribute
   var ns = namespace?namespace+":":"";
   for(prop in bean) {
      try {
      	 var attr = element.getAttribute(ns+prop.toString());
      	 this.setProperty(bean,prop,attr);
      }
      catch(Ex) {
         // need not handle
      }
   }
   return bean;
}
function __BeanUtil_setProperty(bean,property,newValue) {
  	 if (newValue!=null) {
  	    var value = bean[property];
  	    var type = js.reflect.Type.getType(value);
  	    switch(type) {
  	    case js.reflect.Type.NUMBER:
  			value = new Number(newValue);
  			break;
  	    case js.reflect.Type.BOOLEAN:
  			value = new Boolean(newValue);
  			break;
  	    default:
  			value = newValue;
  			break;      			
  	    }
  		bean[property] = value;
     }
}
function __BeanUtil_toText(bean) {
	var text="[";
	for(prop in bean) {
	    text += prop + "=" + bean[prop] + "; ";
	}
	text=text+"]";
	return text;
}