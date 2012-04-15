$export("js.reflect.Type")

function __Type() {
    this.NUMBER = 1;
    this.STRING = 2;
    this.BOOLEAN = 3;
    this.OBJECT = 4;
    this.NULL = 5;
    this.UNDEFINED = 6;
    this.ARRAY = 7;
    this.getType = __Type_getType;
	this.isArray = __Type_isArray;
	this.isUndefined = __Type_isUndefined;	
	this.isNumber = __Type_isNumber;
	this.isString = __Type_isString;
	this.isBoolean = __Type_isBoolean;
	this.isObject = __Type_isObject;
}
function __Type_getType(value) {
	var type = this.OBJECT;
	if (this.isNumber(value)) {
	   return this.NUMBER;
	} else if (this.isString(value)) {
	   return this.STRING;
	} else if (this.isBoolean(value)) {
	   return this.BOOLEAN;
	} else if (this.isObject(value)) {
	   return this.OBJECT;
	} else if (value==null) {
	   return this.NULL;
	} else if (this.isUndefined(value)) {
	   return this.UNDEFINED;
	} else if (this.isArray(value)) {
	   return this.ARRAY;
	}
}
function __Type_isArray(value) {
    var isarray=false;
	if (typeof value == 'object')
	{
		var criterion = value.constructor.toString().match(/array/i);
		isarray = (criterion!=null);
	}
	return isarray;
}
function __Type_isUndefined(value){
	return 	(typeof value == 'undefined');
}
function __Type_isNumber(value) {
	return 	(typeof value == 'number')||(this.isObject(value) && value instanceof Number);
}
function __Type_isString(value) {
	return 	(typeof value == 'string')||(this.isObject(value) && value instanceof String);
}
function __Type_isBoolean(value) {
	return (typeof value == 'boolean')||(this.isObject(value) && value instanceof Boolean);
}
function __Type_isObject(value) {
	return (typeof value == 'object');
}
	
js.reflect.Type = new __Type();	