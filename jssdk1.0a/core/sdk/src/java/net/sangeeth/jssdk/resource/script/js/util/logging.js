$export("js.util.logging.Logger");
$export("js.util.logging.LogManager");

/**
* Logger class definition. 
* @constructor
* @author Sangeeth Kumar .S
*/
js.util.logging.Logger = function(){
   /**
    * The name of the logger.
    * @type String
    * @private
    */	
   this.name = "";
   this.warning = Logger_warning;
   this.info = Logger_info;
   this.severe = Logger_severe;
}
/**
 * log warning message.
 * @param {String} message The warning message.
 */
function Logger_warning(message) {
   window.status = name + ": Warning; "+message;
}
/**
 * log informatory message.
 * @param {String} message The information to be logged.
 */
function Logger_info(message) {
   window.status = name + ": Info; "+message;
}
/**
 * log severe error message.
 * @param {String} message The message considered to be critical.
 */
function Logger_severe(message) {
   window.status = name + ": Severe; "+message;
}

/**
 * @class This is a Singleton LogManager class.
 * This class cannot be directly instantiated.
 * @see js.util.logging.LogManager the singleton instance of this class.
 * @constructor
 * @author Sangeeth Kumar .S
 */
function __LogManager() {
//public: properties	
   /**
    * The Logger class name.
    * @type String
    */
   this.loggerClass = "js.util.logging.Logger";
//private: properties
   /**
    * 
    */
   this.defaultLogger = null;
   /**
    * Local cache of Logger.
    * @private
    * @type Array
    */   
   this.cache = new Array();
//public: methods	
   this.getLogger = __LogManager_getLogger;
}
/**
 * Singleton Instance of LogManager.
 * @type __LogManager
 * @see __LogManager
 */
js.util.logging.LogManager = new __LogManager();
/**
 * Get a logger with a given name.
 * @param {String} name The name of the logger.
 * @return {js.util.logging.Logger} logger
 */
function __LogManager_getLogger(name)
{
   if (!name) {
   	  if (!this.defaultLogger) {
	      window.eval("this.defaultLogger = new " + this.loggerClass + "();");
          this.defaultLogger.name = System.getPath() +": "+ name;
   	  }
   	  return this.defaultLogger;
   }
   var instance = this.cache[name];
   if (instance == null) {
  	  var logger = null;
	  window.eval("logger = new " + this.loggerClass + "();");
	  logger.name = System.getPath() +": "+ name;
      this.cache[name] = logger;
      instance = logger;
   }
   return instance;
}
