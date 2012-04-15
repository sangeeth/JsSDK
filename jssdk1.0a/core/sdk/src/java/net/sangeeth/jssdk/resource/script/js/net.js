$export("js.net.HttpClient");

js.net.HttpClient = function() {
	this.transport = null;
	this.mozilla = false;
    if (window.XMLHttpRequest) {
        this.transport = new XMLHttpRequest();
       	this.mozilla = true;
    // branch for IE/Windows ActiveX version
    } else if (window.ActiveXObject) {
        this.transport = new ActiveXObject("Microsoft.XMLHTTP");
    }		
	this.doGet = HttpClient_doGet;	
	this.destroy = HttpClient_destroy;
}
function HttpClient_doGet(url,callback) {
	var async = false;
	if (callback) {
	    this.transport.onreadystatechange = callback;
	    async = true;
	}
    this.transport.open("GET", url, async);
    if (this.mozilla) {
	    this.transport.send(null);
    } else {
    	this.transport.send();
    }
}
function HttpClient_destroy() {
	this.transport = null;
}

