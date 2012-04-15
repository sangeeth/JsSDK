/*
 * Copyright 2005 Joe Walker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Declare a constructor function to which we can add real functions.
 */
$export("js.dwr.core.DWREngine");
$export("js.dwr.core.DWRUtil");

function __DWREngine(){
	this.setErrorHandler = 	__DWREngine_setErrorHandler;
	this.setWarningHandler = __DWREngine_setWarningHandler;
	this.setTimeout = __DWREngine_setTimeout;
	this.setPreHook = __DWREngine_setPreHook;
	this.setPostHook = __DWREngine_setPostHook;
	this.setMethod = __DWREngine_setMethod;
	this.setVerb = __DWREngine_setVerb;
	this.setOrdered = __DWREngine_setOrdered;
	this.setAsync = __DWREngine_setAsync;
	this.setTextHtmlHandler = __DWREngine_setTextHtmlHandler;
	this.defaultMessageHandler = __DWREngine_defaultMessageHandler;
	this.beginBatch = __DWREngine_beginBatch;
	this.endBatch = __DWREngine_endBatch;
	this._execute = __DWREngine__execute;
	this._sendData = __DWREngine__sendData;
	this._stateChange = __DWREngine__stateChange;
	this._handleResponse = __DWREngine__handleResponse;
	this._handleServerError = __DWREngine__handleServerError;
	this._eval = __DWREngine__eval;
	this._abortRequest = __DWREngine__abortRequest;
	this._clearUp = __DWREngine__clearUp;
	this._handleError = __DWREngine__handleError;
	this._handleWarning = __DWREngine__handleWarning;
	this._handleMetaDataError = __DWREngine__handleMetaDataError;
	this._handleMetaDataWarning = __DWREngine__handleMetaDataWarning;
	this._serializeAll = __DWREngine__serializeAll;
	this._lookup = __DWREngine__lookup;
	this._serializeObject = __DWREngine__serializeObject;
	this._serializeXml = __DWREngine__serializeXml;
	this._serializeArray = __DWREngine__serializeArray;
	this._unserializeDocument = __DWREngine__unserializeDocument;
	this._newActiveXObject = __DWREngine__newActiveXObject;
	
	this._warningHandler = null;
    this._timeout = null;
	/** XHR remoting method constant. See function __DWREngine_setMethod() */    
    this.XMLHttpRequest = 1;
    /** XHR remoting method constant. See function __DWREngine_setMethod() */
    this.IFrame = 2;
	// private:
	/** A function to call if something fails. */
	this._errorHandler = this.defaultMessageHandler;
	/** For debugging when something unexplained happens. */
	this._warningHandler = null;
	/** A function to be called before requests are marshalled. Can be null. */
	this._preHook = null;
	/** A function to be called after replies are received. Can be null. */
	this._postHook = null;
	/** An array of the batches that we have sent and are awaiting a reply on. */
	this._batches = [];
	/** In ordered mode, the array of batches waiting to be sent */
	this._batchQueue = [];
	/** A map of known ids to their handler objects */
	this._handlersMap = {};
	/** What is the default remoting method */
	this._method = this.XMLHttpRequest;
	/** What is the default remoting verb (ie GET or POST) */
	this._verb = "POST";
	/** Do we attempt to ensure that calls happen in the order in which they were sent? */
	this._ordered = false;
	/** Do we make the calls async? */
	this._async = true;
	/** The current batch (if we are in batch mode) */
	this._batch = null;
	/** The global timeout */
	this._timeout = 0;
	/** ActiveX objects to use when we want to convert an xml string into a DOM object. */
	this._DOMDocument = ["Msxml2.DOMDocument.6.0", "Msxml2.DOMDocument.5.0", "Msxml2.DOMDocument.4.0", "Msxml2.DOMDocument.3.0", "MSXML2.DOMDocument", "MSXML.DOMDocument", "Microsoft.XMLDOM"];
	/** The ActiveX objects to use when we want to do an XMLHttpRequest call. */
	this._XMLHTTP = ["Msxml2.XMLHTTP.6.0", "Msxml2.XMLHTTP.5.0", "Msxml2.XMLHTTP.4.0", "MSXML2.XMLHTTP.3.0", "MSXML2.XMLHTTP", "Microsoft.XMLHTTP"];
}

js.dwr.core.DWREngine = new __DWREngine();

/**
 * Set an alternative error handler from the default alert box.
 * @see http://getahead.ltd.uk/dwr/browser/engine/errors
 */
function __DWREngine_setErrorHandler(handler) {
  this._errorHandler = handler;
}

/**
 * Set an alternative warning handler from the default alert box.
 * @see http://getahead.ltd.uk/dwr/browser/engine/errors
 */
function __DWREngine_setWarningHandler(handler) {
  this._warningHandler = handler;
}

/**
 * Set a default timeout value for all calls. 0 (the default) turns timeouts off.
 * @see http://getahead.ltd.uk/dwr/browser/engine/errors
 */
function __DWREngine_setTimeout(timeout) {
  this._timeout = timeout;
}

/**
 * The Pre-Hook is called before any DWR remoting is done.
 * @see http://getahead.ltd.uk/dwr/browser/engine/hooks
 */
function __DWREngine_setPreHook(handler) {
  this._preHook = handler;
}

/**
 * The Post-Hook is called after any DWR remoting is done.
 * @see http://getahead.ltd.uk/dwr/browser/engine/hooks
 */
function __DWREngine_setPostHook(handler) {
  this._postHook = handler;
}

/**
 * Set the preferred remoting method.
 * @param newMethod One of js.dwr.engine.DWREngine.XMLHttpRequest or js.dwr.engine.DWREngine.IFrame
 * @see http://getahead.ltd.uk/dwr/browser/engine/options
 */
function __DWREngine_setMethod(newMethod) {
  if (newMethod != this.XMLHttpRequest && newMethod != this.IFrame) {
    this._handleError("Remoting method must be one of js.dwr.engine.DWREngine.XMLHttpRequest or js.dwr.engine.DWREngine.IFrame");
    return;
  }
  this._method = newMethod;
};

/**
 * Which HTTP verb do we use to send results? Must be one of "GET" or "POST".
 * @see http://getahead.ltd.uk/dwr/browser/engine/options
 */
function __DWREngine_setVerb(verb) {
  if (verb != "GET" && verb != "POST") {
    this._handleError("Remoting verb must be one of GET or POST");
    return;
  }
  this._verb = verb;
}

/**
 * Ensure that remote calls happen in the order in which they were sent? (Default: false)
 * @see http://getahead.ltd.uk/dwr/browser/engine/ordering
 */
function __DWREngine_setOrdered(ordered) {
  this._ordered = ordered;
};

/**
 * Do we ask the XHR object to be asynchronous? (Default: true)
 * @see http://getahead.ltd.uk/dwr/browser/engine/options
 */
function __DWREngine_setAsync(async) {
  this._async = async;
};

/**
 * Setter for the text/html handler - what happens if a DWR request gets an HTML
 * reply rather than the expected Javascript. Often due to login timeout
 */
function __DWREngine_setTextHtmlHandler(handler) {
  this._textHtmlHandler = handler;
}

/**
 * The default message handler.
 * @see http://getahead.ltd.uk/dwr/browser/engine/errors
 */
function __DWREngine_defaultMessageHandler(message) {
  if (typeof message == "object" && message.name == "Error" && message.description) {
    alert("Error: " + message.description);
  }
  else {
    // Ignore NS_ERROR_NOT_AVAILABLE
    if (message.toString().indexOf("0x80040111") == -1) {
      alert(message);
    }
  }
};

/**
 * For reduced latency you can group several remote calls together using a batch.
 * @see http://getahead.ltd.uk/dwr/browser/engine/batch
 */
function __DWREngine_beginBatch() {
  if (this._batch) {
    this._handleError("Batch already started.");
    return;
  }
  // Setup a batch
  this._batch = {
    map:{ callCount:0 },
    paramCount:0,
    ids:[],
    preHooks:[],
    postHooks:[]
  };
};

/**
 * Finished grouping a set of remote calls together. Go and execute them all.
 * @see http://getahead.ltd.uk/dwr/browser/engine/batch
 */
function __DWREngine_endBatch(options) {
  var batch = this._batch;
  if (batch == null) {
    this._handleError("No batch in progress.");
    return;
  }
  // Merge the global batch level properties into the batch meta data
  if (options && options.preHook) batch.preHooks.unshift(options.preHook);
  if (options && options.postHook) batch.postHooks.push(options.postHook);
  if (this._preHook) batch.preHooks.unshift(this._preHook);
  if (this._postHook) batch.postHooks.push(this._postHook);

  if (batch.method == null) batch.method = this._method;
  if (batch.verb == null) batch.verb = this._verb;
  if (batch.async == null) batch.async = this._async;
  if (batch.timeout == null) batch.timeout = this._timeout;

  batch.completed = false;

  // We are about to send so this batch should not be globally visible
  this._batch = null;

  // If we are in ordered mode, then we don't send unless the list of sent
  // items is empty
  if (!this._ordered) {
    this._sendData(batch);
    this._batches[this._batches.length] = batch;
  }
  else {
    if (this._batches.length == 0) {
      // We aren't waiting for anything, go now.
      this._sendData(batch);
      this._batches[this._batches.length] = batch;
    }
    else {
      // Push the batch onto the waiting queue
      this._batchQueue[this._batchQueue.length] = batch;
    }
  }
};

/**
 * @private Send a request. Called by the Javascript interface stub
 * @param path part of URL after the host and before the exec bit without leading or trailing /s
 * @param scriptName The class to execute
 * @param methodName The method on said class to execute
 * @param func The callback function to which any returned data should be passed
 *       if this is null, any returned data will be ignored
 * @param vararg_params The parameters to pass to the above class
 */
function __DWREngine__execute(path, scriptName, methodName, vararg_params) {
  var singleShot = false;
  if (this._batch == null) {
    this.beginBatch();
    singleShot = true;
  }
  // To make them easy to manipulate we copy the arguments into an args array
  var args = [];
  for (var i = 0; i < arguments.length - 3; i++) {
    args[i] = arguments[i + 3];
  }
  // All the paths MUST be to the same servlet
  if (this._batch.path == null) {
    this._batch.path = path;
  }
  else {
    if (this._batch.path != path) {
      this._handleError("Can't batch requests to multiple DWR Servlets.");
      return;
    }
  }
  // From the other params, work out which is the function (or object with
  // call meta-data) and which is the call parameters
  var params;
  var callData;
  var firstArg = args[0];
  var lastArg = args[args.length - 1];

  if (typeof firstArg == "function") {
    callData = { callback:args.shift() };
    params = args;
  }
  else if (typeof lastArg == "function") {
    callData = { callback:args.pop() };
    params = args;
  }
  else if (lastArg != null && typeof lastArg == "object" && lastArg.callback != null && typeof lastArg.callback == "function") {
    callData = args.pop();
    params = args;
  }
  else if (firstArg == null) {
    // This could be a null callback function, but if the last arg is also
    // null then we can't tell which is the function unless there are only
    // 2 args, in which case we don't care!
    if (lastArg == null && args.length > 2) {
        this._handleError("Ambiguous nulls at start and end of parameter list. Which is the callback function?");
    }
    callData = { callback:args.shift() };
    params = args;
  }
  else if (lastArg == null) {
    callData = { callback:args.pop() };
    params = args;
  }
  else {
    this._handleError("Missing callback function or metadata object.");
    return;
  }

  // Get a unique ID for this call
  var random = Math.floor(Math.random() * 10001);
  var id = (random + "_" + new Date().getTime()).toString();
  var prefix = "c" + this._batch.map.callCount + "-";
  this._batch.ids.push(id);

  // batchMetaData stuff the we allow in callMetaData for convenience
  if (callData.method != null) {
    this._batch.method = callData.method;
    delete callData.method;
  }
  if (callData.verb != null) {
    this._batch.verb = callData.verb;
    delete callData.verb;
  }
  if (callData.async != null) {
    this._batch.async = callData.async;
    delete callData.async;
  }
  if (callData.timeout != null) {
    this._batch.timeout = callData.timeout;
    delete callData.timeout;
  }

  // callMetaData stuff that we handle with the rest of the batchMetaData
  if (callData.preHook != null) {
    this._batch.preHooks.unshift(callData.preHook);
    delete callData.preHook;
  }
  if (callData.postHook != null) {
    this._batch.postHooks.push(callData.postHook);
    delete callData.postHook;
  }

  // Default the error and warning handlers
  if (callData.errorHandler == null) callData.errorHandler = this._errorHandler;
  if (callData.warningHandler == null) callData.warningHandler = this._warningHandler;

  // Save the callMetaData
  this._handlersMap[id] = callData;

  this._batch.map[prefix + "scriptName"] = scriptName;
  this._batch.map[prefix + "methodName"] = methodName;
  this._batch.map[prefix + "id"] = id;

  // Serialize the parameters into batch.map
  for (i = 0; i < params.length; i++) {
    this._serializeAll(this._batch, [], params[i], prefix + "param" + i);
  }

  // Now we have finished remembering the call, we incr the call count
  this._batch.map.callCount++;
  if (singleShot) {
    this.endBatch();
  }
};

/** @private Actually send the block of data in the batch object. */
function __DWREngine__sendData(batch) {
  // If the batch is empty, don't send anything
  if (batch.map.callCount == 0) return;
  // Call any pre-hooks
  for (var i = 0; i < batch.preHooks.length; i++) {
    batch.preHooks[i]();
  }
  batch.preHooks = null;
  // Set a timeout
  if (batch.timeout && batch.timeout != 0) {
    batch.interval = setInterval(function() { js.dwr.core.DWREngine._abortRequest(batch); }, batch.timeout);
  }
  // A quick string to help people that use web log analysers
  var urlPostfix;
  if (batch.map.callCount == 1) {
    urlPostfix = batch.map["c0-scriptName"] + "." + batch.map["c0-methodName"] + ".dwr";
  }
  else {
    urlPostfix = "Multiple." + batch.map.callCount + ".dwr";
  }

  // Get setup for XMLHttpRequest if possible
  if (batch.method == this.XMLHttpRequest) {
    if (window.XMLHttpRequest) {
      batch.req = new XMLHttpRequest();
    }
    // IE5 for the mac claims to support window.ActiveXObject, but throws an error when it's used
    else if (window.ActiveXObject && !(navigator.userAgent.indexOf("Mac") >= 0 && navigator.userAgent.indexOf("MSIE") >= 0)) {
      batch.req = this._newActiveXObject(this._XMLHTTP);
    }
  }

  var query = "";
  var prop;

  // This equates to (batch.method == XHR && browser supports XHR)
  if (batch.req) {
    batch.map.xml = "true";
    // Proceed using XMLHttpRequest
    if (batch.async) {
      batch.req.onreadystatechange = function() { js.dwr.core.DWREngine._stateChange(batch); };
    }
    // Workaround for Safari 1.x POST bug
    var indexSafari = navigator.userAgent.indexOf("Safari/");
    if (indexSafari >= 0) {
      var version = navigator.userAgent.substring(indexSafari + 7);
      if (parseInt(version, 10) < 400) batch.verb == "GET";
    }
    if (batch.verb == "GET") {
      // Some browsers (Opera/Safari2) seem to fail to convert the value
      // of batch.map.callCount to a string in the loop below so we do it
      // manually here.
      batch.map.callCount = "" + batch.map.callCount;

      for (prop in batch.map) {
        var qkey = encodeURIComponent(prop);
        var qval = encodeURIComponent(batch.map[prop]);
        if (qval == "") this._handleError("Found empty qval for qkey=" + qkey);
        query += qkey + "=" + qval + "&";
      }

      try {
        batch.req.open("GET", batch.path + "/exec/" + urlPostfix + "?" + query, batch.async);
        batch.req.send(null);
        if (!batch.async) this._stateChange(batch);
      }
      catch (ex) {
        this._handleMetaDataError(null, ex);
      }
    }
    else {
      for (prop in batch.map) {
        if (typeof batch.map[prop] != "function") {
          query += prop + "=" + batch.map[prop] + "\n";
        }
      }

      try {
        batch.req.open("POST", batch.path + "/exec/" + urlPostfix, batch.async);
        batch.req.setRequestHeader('Content-Type', 'text/plain');
        batch.req.send(query);
        if (!batch.async) this._stateChange(batch);
      }
      catch (ex) {
        this._handleMetaDataError(null, ex);
      }
    }
  }
  else {
    batch.map.xml = "false";
    var idname = "dwr-if-" + batch.map["c0-id"];
    // Proceed using iframe
    batch.div = document.createElement("div");
    batch.div.innerHTML = "<iframe src='javascript:void(0)' frameborder='0' width='0' height='0' id='" + idname + "' name='" + idname + "'></iframe>";
    document.body.appendChild(batch.div);
    batch.iframe = document.getElementById(idname);
    batch.iframe.setAttribute("style", "width:0px; height:0px; border:0px;");

    if (batch.verb == "GET") {
      for (prop in batch.map) {
        if (typeof batch.map[prop] != "function") {
          query += encodeURIComponent(prop) + "=" + encodeURIComponent(batch.map[prop]) + "&";
        }
      }
      query = query.substring(0, query.length - 1);

      batch.iframe.setAttribute("src", batch.path + "/exec/" + urlPostfix + "?" + query);
      document.body.appendChild(batch.iframe);
    }
    else {
      batch.form = document.createElement("form");
      batch.form.setAttribute("id", "dwr-form");
      batch.form.setAttribute("action", batch.path + "/exec" + urlPostfix);
      batch.form.setAttribute("target", idname);
      batch.form.target = idname;
      batch.form.setAttribute("method", "POST");
      for (prop in batch.map) {
        var formInput = document.createElement("input");
        formInput.setAttribute("type", "hidden");
        formInput.setAttribute("name", prop);
        formInput.setAttribute("value", batch.map[prop]);
        batch.form.appendChild(formInput);
      }
      document.body.appendChild(batch.form);
      batch.form.submit();
    }
  }
};

/** @private Called by XMLHttpRequest to indicate that something has happened */
function __DWREngine__stateChange(batch) {
  if (!batch.completed && batch.req.readyState == 4) {
   try {
      var reply = batch.req.responseText;

      if (reply == null || reply == "") {
        this._handleMetaDataWarning(null, "No data received from server");
      }
      else {
        var contentType = batch.req.getResponseHeader("Content-Type");
        if (!contentType.match(/^text\/plain/) && !contentType.match(/^text\/javascript/)) {
          if (this._textHtmlHandler && contentType.match(/^text\/html/)) {
            this._textHtmlHandler();
          }
          else {
            this._handleMetaDataWarning(null, "Invalid content type from server: '" + contentType + "'");
          }
        }
        else {
          // Skip checking the xhr.status because the above will do for most errors
          // and because it causes Mozilla to error

          if (reply.search("DWREngine._handle") == -1) {
            this._handleMetaDataWarning(null, "Invalid reply from server");
          }
          else {
            eval(reply);
          }
        }
      }

      // We're done. Clear up
      this._clearUp(batch);
    }
    catch (ex) {
      if (ex == null) ex = "Unknown error occured";
      this._handleMetaDataWarning(null, ex);
    }
    finally {
      // If there is anything on the queue waiting to go out, then send it.
      // We don't need to check for ordered mode, here because when ordered mode
      // gets turned off, we still process *waiting* batches in an ordered way.
      if (this._batchQueue.length != 0) {
        var sendbatch = this._batchQueue.shift();
        this._sendData(sendbatch);
        this._batches[this._batches.length] = sendbatch;
      }
    }
  }
};

/**
 * @private Called by reply scripts generated as a result of remote requests
 * @param id The identifier of the call that we are handling a response for
 * @param reply The data to pass to the callback function
 */
function __DWREngine__handleResponse(id, reply) {
  // Clear this callback out of the list - we don't need it any more
  var handlers = this._handlersMap[id];
  this._handlersMap[id] = null;

  if (handlers) {
    // Error handlers inside here indicate an error that is nothing to do
    // with DWR so we handle them differently.
    try {
      if (handlers.callback) handlers.callback(reply);
    }
    catch (ex) {
      this._handleMetaDataError(handlers, ex);
    }
  }

  // Finalize the call for IFrame transport 
  if (this._method == this.IFrame) {
    var responseBatch = this._batches[this._batches.length-1];	
    // Only finalize after the last call has been handled
    if (responseBatch.map["c"+(responseBatch.map.callCount-1)+"-id"] == id) {
      this._clearUp(responseBatch);
    }
  }
};

/** @private This method is called by Javascript that is emitted by server */
function __DWREngine__handleServerError(id, error) {
  // Clear this callback out of the list - we don't need it any more
  var handlers = this._handlersMap[id];
  this._handlersMap[id] = null;

  if (error.message) this._handleMetaDataError(handlers, error.message, error);
  else this._handleMetaDataError(handlers, error);
};

/** @private This is a hack to make the context be this window */
function __DWREngine__eval(script) {
  return eval(script);
}

/** @private Called as a result of a request timeout */
function __DWREngine__abortRequest(batch) {
  if (batch && !batch.completed) {
    clearInterval(batch.interval);
    this._clearUp(batch);
    if (batch.req) batch.req.abort();
    // Call all the timeout errorHandlers
    var handlers;
    for (var i = 0; i < batch.ids.length; i++) {
      handlers = this._handlersMap[batch.ids[i]];
      this._handleMetaDataError(handlers, "Timeout");
    }
  }
};

/** @private A call has finished by whatever means and we need to shut it all down. */
function __DWREngine__clearUp(batch) {
  if (batch.completed) {
    this._handleError("Double complete");
    return;
  }

  // IFrame tidyup
  if (batch.div) batch.div.parentNode.removeChild(batch.div);
  if (batch.iframe) batch.iframe.parentNode.removeChild(batch.iframe);
  if (batch.form) batch.form.parentNode.removeChild(batch.form);

  // XHR tidyup: avoid IE handles increase
  if (batch.req) delete batch.req;

  for (var i = 0; i < batch.postHooks.length; i++) {
    batch.postHooks[i]();
  }
  batch.postHooks = null;

  // TODO: There must be a better way???
  for (var i = 0; i < this._batches.length; i++) {
    if (this._batches[i] == batch) {
      this._batches.splice(i, 1);
      break;
    }
  }

  batch.completed = true;
};

/** @private Generic error handling routing to save having null checks everywhere */
function __DWREngine__handleError(reason, ex) {
  if (this._errorHandler) this._errorHandler(reason, ex);
};

/** @private Generic warning handling routing to save having null checks everywhere */
function __DWREngine__handleWarning(reason, ex) {
  if (this._warningHandler) this._warningHandler(reason, ex);
};

/** @private Generic error handling routing to save having null checks everywhere */
function __DWREngine__handleMetaDataError(handlers, reason, ex) {
  if (handlers && typeof handlers.errorHandler == "function") handlers.errorHandler(reason, ex);
  else this._handleError(reason, ex);
};

/** @private Generic error handling routing to save having null checks everywhere */
function __DWREngine__handleMetaDataWarning(handlers, reason, ex) {
  if (handlers && typeof handlers.warningHandler == "function") handlers.warningHandler(reason, ex);
  else this._handleWarning(reason, ex);
};

/**
 * @private Marshall a data item
 * @param batch A map of variables to how they have been marshalled
 * @param referto An array of already marshalled variables to prevent recurrsion
 * @param data The data to be marshalled
 * @param name The name of the data being marshalled
 */
function __DWREngine__serializeAll(batch, referto, data, name) {
  if (data == null) {
    batch.map[name] = "null:null";
    return;
  }

  switch (typeof data) {
  case "boolean":
    batch.map[name] = "boolean:" + data;
    break;
  case "number":
    batch.map[name] = "number:" + data;
    break;
  case "string":
    batch.map[name] = "string:" + encodeURIComponent(data);
    break;
  case "object":
    if (data instanceof String) batch.map[name] = "String:" + encodeURIComponent(data);
    else if (data instanceof Boolean) batch.map[name] = "Boolean:" + data;
    else if (data instanceof Number) batch.map[name] = "Number:" + data;
    else if (data instanceof Date) batch.map[name] = "Date:" + data.getTime();
    else if (data instanceof Array) batch.map[name] = this._serializeArray(batch, referto, data, name);
    else batch.map[name] = this._serializeObject(batch, referto, data, name);
    break;
  case "function":
    // We just ignore functions.
    break;
  default:
    this._handleWarning("Unexpected type: " + typeof data + ", attempting default converter.");
    batch.map[name] = "default:" + data;
    break;
  }
};

/** @private Have we already converted this object? */
function __DWREngine__lookup(referto, data, name) {
  var lookup;
  // Can't use a map: http://getahead.ltd.uk/ajax/javascript-gotchas
  for (var i = 0; i < referto.length; i++) {
    if (referto[i].data == data) {
      lookup = referto[i];
      break;
    }
  }
  if (lookup) return "reference:" + lookup.name;
  referto.push({ data:data, name:name });
  return null;
};

/** @private Marshall an object */
function __DWREngine__serializeObject(batch, referto, data, name) {
  var ref = this._lookup(referto, data, name);
  if (ref) return ref;

  // This check for an HTML is not complete, but is there a better way?
  // Maybe we should add: data.hasChildNodes typeof "function" == true
  if (data.nodeName && data.nodeType) {
    return this._serializeXml(batch, referto, data, name);
  }

  // treat objects as an associative arrays
  var reply = "Object:{";
  var element;
  for (element in data) {
    batch.paramCount++;
    var childName = "c" + this._batch.map.callCount + "-e" + batch.paramCount;
    this._serializeAll(batch, referto, data[element], childName);

    reply += encodeURIComponent(element) + ":reference:" + childName + ", ";
  }

  if (reply.substring(reply.length - 2) == ", ") {
    reply = reply.substring(0, reply.length - 2);
  }
  reply += "}";

  return reply;
};

/** @private Marshall an object */
function __DWREngine__serializeXml(batch, referto, data, name) {
  var ref = this._lookup(referto, data, name);
  if (ref) return ref;

  var output;
  if (window.XMLSerializer) output = new XMLSerializer().serializeToString(data);
  else output = data.toXml;

  return "XML:" + encodeURIComponent(output);
};

/** @private Marshall an array */
function __DWREngine__serializeArray(batch, referto, data, name) {
  var ref = this._lookup(referto, data, name);
  if (ref) return ref;

  var reply = "Array:[";
  for (var i = 0; i < data.length; i++) {
    if (i != 0) reply += ",";
    batch.paramCount++;
    var childName = "c" + this._batch.map.callCount + "-e" + batch.paramCount;
    this._serializeAll(batch, referto, data[i], childName);
    reply += "reference:";
    reply += childName;
  }
  reply += "]";

  return reply;
};

/** @private Convert an XML string into a DOM object. */
function __DWREngine__unserializeDocument(xml) {
  var dom;
  if (window.DOMParser) {
    var parser = new DOMParser();
    dom = parser.parseFromString(xml, "text/xml");
    if (!dom.documentElement || dom.documentElement.tagName == "parsererror") {
      var message = dom.documentElement.firstChild.data;
      message += "\n" + dom.documentElement.firstChild.nextSibling.firstChild.data;
      throw message;
    }
    return dom;
  }
  else if (window.ActiveXObject) {
    dom = this._newActiveXObject(this._DOMDocument);
    dom.loadXML(xml); // What happens on parse fail with IE?
    return dom;
  }
  else {
    var div = document.createElement("div");
    div.innerHTML = xml;
    return div;
  }
};

/**
 * @private Helper to find an ActiveX object that works.
 * @param axarray An array of strings to attempt to create ActiveX objects from
 */
function __DWREngine__newActiveXObject(axarray) {
  var returnValue;  
  for (var i = 0; i < axarray.length; i++) {
    try {
      returnValue = new ActiveXObject(axarray[i]);
      break;
    }
    catch (ex) { /* ignore */ }
  }
  return returnValue;
};

/** @private To make up for the lack of encodeURIComponent() on IE5.0 */
if (typeof window.encodeURIComponent === 'undefined') {
	js.dwr.core.DWREngine._okURIchars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-";
	js.dwr.core.DWREngine._hexchars = "0123456789ABCDEF";
	
  js.dwr.core.DWREngine._utf8 = function(wide) {
    wide = "" + wide; // Make sure it is a string
    var c;
    var s;
    var enc = "";
    var i = 0;
    while (i < wide.length) {
      c = wide.charCodeAt(i++);
      // handle UTF-16 surrogates
      if (c >= 0xDC00 && c < 0xE000) continue;
      if (c >= 0xD800 && c < 0xDC00) {
        if (i >= wide.length) continue;
        s = wide.charCodeAt(i++);
        if (s < 0xDC00 || c >= 0xDE00) continue;
        c = ((c - 0xD800) << 10) + (s - 0xDC00) + 0x10000;
      }
      // output value
      if (c < 0x80) {
        enc += String.fromCharCode(c);
      }
      else if (c < 0x800) {
        enc += String.fromCharCode(0xC0 + (c >> 6), 0x80 + (c & 0x3F));
      }
      else if (c < 0x10000) {
        enc += String.fromCharCode(0xE0 + (c >> 12), 0x80 + (c >> 6 & 0x3F), 0x80 + (c & 0x3F));
      }
      else {
        enc += String.fromCharCode(0xF0 + (c >> 18), 0x80 + (c >> 12 & 0x3F), 0x80 + (c >> 6 & 0x3F), 0x80 + (c & 0x3F));
      }
    }
    return enc;
  }

  js.dwr.core.DWREngine._toHex = function(n) {
    return this._hexchars.charAt(n >> 4) + this._hexchars.charAt(n & 0xF);
  }

  window.encodeURIComponent = function(s)  {
    s = this._utf8(s);
    var c;
    var enc = "";
    for (var i= 0; i<s.length; i++) {
      if (this._okURIchars.indexOf(s.charAt(i)) == -1) {
        enc += "%" + js.dwr.core.DWREngine._toHex(s.charCodeAt(i));
      }
      else {
        enc += s.charAt(i);
      }
    }
    return enc;
  }
}

/** @private To make up for the lack of Array.splice() on IE5.0 */
if (typeof Array.prototype.splice === 'undefined') {
  Array.prototype.splice = function(ind, cnt)
  {
    if (arguments.length == 0) return ind;
    if (typeof ind != "number") ind = 0;
    if (ind < 0) ind = Math.max(0,this.length + ind);
    if (ind > this.length) {
      if (arguments.length > 2) ind = this.length;
      else return [];
    }
    if (arguments.length < 2) cnt = this.length-ind;

    cnt = (typeof cnt == "number") ? Math.max(0, cnt) : 0;
    removeArray = this.slice(ind, ind + cnt);
    endArray = this.slice(ind + cnt);
    this.length = ind;

    for (var i = 2; i < arguments.length; i++) this[this.length] = arguments[i];
    for (i = 0; i < endArray.length; i++) this[this.length] = endArray[i];

    return removeArray;
  }
}

/** @private To make up for the lack of Array.shift() on IE5.0 */
if (typeof Array.prototype.shift === 'undefined') {
  Array.prototype.shift = function(str) {
    var val = this[0];
    for (var i = 1; i < this.length; ++i) this[i - 1] = this[i];
    this.length--;
    return val;
  }
}

/** @private To make up for the lack of Array.unshift() on IE5.0 */
if (typeof Array.prototype.unshift === 'undefined') {
  Array.prototype.unshift = function() {
    var i = unshift.arguments.length;
    for (var j = this.length - 1; j >= 0; --j) this[j + i] = this[j];
    for (j = 0; j < i; ++j) this[j] = unshift.arguments[j];
  }
}

/** @private To make up for the lack of Array.push() on IE5.0 */
if (typeof Array.prototype.push === 'undefined') {
  Array.prototype.push = function() {
    var sub = this.length;
    for (var i = 0; i < push.arguments.length; ++i) {
      this[sub] = push.arguments[i];
      sub++;
    }
  }
}

/** @private To make up for the lack of Array.pop() on IE5.0 */
if (typeof Array.prototype.pop === 'undefined') {
  Array.prototype.pop = function() {
    var lastElement = this[this.length - 1];
    this.length--;
    return lastElement;
  }
}

function __DWRUtil() {
	this.intervalId = null;
	this.useLoadingMessage = __DWRUtil_useLoadingMessage;
}
js.dwr.core.DWRUtil = new __DWRUtil();
function __DWRUtil_useLoadingMessage(message) {
  var instance = this;
  var loadingMessage;
  if (message) loadingMessage = message;
  else loadingMessage = "Loading";
  try {
  js.dwr.core.DWREngine.setPreHook(function() {
    var disabledZone = $('disabledZone');
    if (!disabledZone) {
      disabledZone = document.createElement('div');
      disabledZone.setAttribute('id', 'disabledZone');
      disabledZone.style.position = "absolute";
      disabledZone.style.zIndex = "1000";
      disabledZone.style.left = "0px";
      disabledZone.style.top = "0px";
      disabledZone.style.width = "100%";
      disabledZone.style.height = "100%";
      document.body.appendChild(disabledZone);
      var messageZone = document.createElement('div');
      messageZone.setAttribute('id', 'messageZone');
      messageZone.style.position = "absolute";
      messageZone.style.top = "0px";
      messageZone.style.right = "0px";
      messageZone.style.background = "#EEEECC";
      messageZone.style.color = "#123";
      messageZone.style.fontFamily = "Arial,Helvetica,sans-serif";
      messageZone.style.padding = "4px";
      disabledZone.appendChild(messageZone);
      var text = document.createTextNode(loadingMessage);
      messageZone.appendChild(text);
    }
    else {
      $('messageZone').innerHTML = loadingMessage;
      disabledZone.style.visibility = 'visible';
     // window.clearInterval(instance.intervalId);
    }
  });

  js.dwr.core.DWREngine.setPostHook(function() {
    $('disabledZone').style.visibility = 'hidden';
  });
  } catch (E) {
  	  alert(E.description);
  }
}
