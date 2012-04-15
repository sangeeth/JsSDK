$import("js.util.logging");
$import("js.util.bean");
$export("js.dwr.logging.DWRLogger");

js.util.logging.LogManager.loggerClass = "js.dwr.logging.DWRLogger";

/**
 * DWR based Logger class definition. 
 * @constructor
 * @extends js.util.logging.Logger
 * @author Sangeeth Kumar .S
 */
js.dwr.logging.DWRLogger = function() {
   this.warning = DWRLogger_warning;
   this.info = DWRLogger_info;
   this.severe = DWRLogger_severe;
}
function DWRLogger_warning(message) {
   if (g_context.loggingEnabled && g_context.loggingLevel>=2) {
	   LogService.warning(function(){},g_context,this.name,message);
   }
}
function DWRLogger_info(message) {
   if (g_context.loggingEnabled && g_context.loggingLevel>=1) {
	   LogService.info(function(){},g_context,this.name,message);
   }
}
function DWRLogger_severe(message) {
   if (g_context.loggingEnabled && g_context.loggingLevel>=3) {
	   LogService.severe(function(){},g_context,this.name,message);
   }
}