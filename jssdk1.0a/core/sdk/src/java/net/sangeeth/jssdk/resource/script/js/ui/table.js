$import("js.util.collections");
$import("js.ui.toolkit");
$import("js.ui.event");

$export("js.ui.table.PagedTable");
$export("js.ui.table.SortableColumn");

js.ui.table.PagedTable = function(cid) {
    this.cid = cid;	
	this.containerId = "contents";
	this.defaultContent = "No results found."
	this.pageSize = 5;
	this.pageCount = 0;
	this.currentPageId = 0;
	this.page = null;
	this.pageRowsProperty = null;
	this.pageNavigator = null;
	this.viewProcessor = js.ui.UIManager.getUI(this.cid);
	this.beanComparator = new js.util.BeanComparator();
	this.cache = new Array();
//public:
	this.show = PagedTable_show;
	this.nextPage = PagedTable_nextPage;
	this.prevPage = PagedTable_prevPage;
	this.showPage = PagedTable_showPage;
	this.showPageById = PagedTable_showPageById;
	this.sortPage = PagedTable_sortPage;
	this.setView = PagedTable_setView;
//Hook Event Handlers
	instance = this;
	js.ui.EventManager.addEventHandler(
				js.ui.EventManager.EVENT_ONCLICK,
				function(event) {
  			       var el = event.srcElement?event.srcElement:event.target;
  			       var component = el.getAttribute("js:component");
  			       if (component == "SortableColumn") {
				   	  var beanPath = el.getAttribute("js:beanPath");
				   	  if (beanPath) {
					   	  instance.sortPage(beanPath);
					   	  js.ui.EventManager.cancel(event);
					   	  return false;
				   	  }
					}
				});
}
js.ui.table.PagedTable.prototype = new js.ui.Component();
function PagedTable_setView(view) {
	//TODO use js.ui.toolkit.View
}
function PagedTable_show(page) {
   this.page = page;
   if (page)
   {
	   this.pageCount = page.pageCount;
	   this.currentPageId = page.currentPageId;
	   this.cache = new Array();
   	   this.cache[this.currentPageId] = page;
   }
   this.showPage();
}
function PagedTable_prevPage() {
	--this.currentPageId;
	if (this.currentPageId<0)
	   this.currentPageId=0;

	this.showPageById(this.currentPageId);
}
function PagedTable_nextPage() {
	this.currentPageId++;
	if (this.currentPageId>this.pageCount)
	   this.currentPageId=this.pageCount;
	
	this.showPageById(this.currentPageId);
}
function PagedTable_showPageById(pageId){
	if (pageId>0 && pageId <= this.pageCount) {
		var instance = this;
		var page = this.cache[pageId];
		if (page == null)
		{
			if (this.pageNavigator) {
				var fn = this.pageNavigator;
				fn(pageId);
			}
		}
		else {
			this.showPage(page);
		}
		this.currentPageId = pageId;
	}
}

function PagedTable_showPage() {
	if (arguments && arguments.length==1)
	{
		this.page = arguments[0];
		this.cache[this.currentPageId] = this.page;
	}
   	var srcElement = document.getElementById(this.containerId);
	var html = this.defaultContent;
	
	if (this.page && this.viewProcessor)
	{
		html = this.viewProcessor(this.page);
	}
	
	srcElement.innerHTML = html;
}
function PagedTable_sortPage(property) {
	if (this.pageRowsProperty) {
		var rows = this.page[this.pageRowsProperty];
		if (this.beanComparator.property == property)	{
		   	this.beanComparator.order*=-1; //Toggle		
		} else {
			this.beanComparator.order = js.util.Collections.ASCENDING_ORDER;
			this.beanComparator.property = property;
		}
	    js.util.Collections.sort(rows,this.beanComparator);
	    this.showPage();
	    var table = document.getElementById(this.cid);
	    js.ui.table.SortableColumn.highlight(table,property,this.beanComparator.order);
	}
}

function __SortableColumn() {
	this.defaultClass = "";
	this.ascOrderClass = "";
	this.desOrderClass = "";		
	this.highlight = SortableColumn_highlight;
}
js.ui.table.SortableColumn = new __SortableColumn();
function SortableColumn_highlight(table,beanPath,order) {
	if (table) {
	    var theads = table.getElementsByTagName("THEAD");
	    if(theads && theads.length>0){
	       var thead = theads[0];
	       var trs = table.getElementsByTagName("TR");
	       if (trs && trs.length>0) {
			  var tr = trs[0];
			  var ths = tr.childNodes;			  
			  for(var i=0;i<ths.length;i++) {
			     if (ths[i].nodeType==1)  { //if Element Node
			        var attrClass = ths[i].getAttributeNode("class");
			        var attrBeanPath = ths[i].getAttribute("js:beanPath");
			        if (attrBeanPath==beanPath) {
			        	if (order==js.util.Collections.ASCENDING_ORDER) {
			        		attrClass.value = this.ascOrderClass;
			        	} else {
							attrClass.value = this.desOrderClass;
						}
			        } else {
			        	attrClass.value = this.defaultClass;
			        }
			     }
		      }
	       }
	    }
	}	
}