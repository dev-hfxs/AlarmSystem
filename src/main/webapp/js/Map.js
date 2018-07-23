
function Map(){
	var struct = function(key, value) {
	this.key = key;
	this.value = value;
}
var put = function(key, value){
	var isNew = true;
	for (var i = 0; i < this.arr.length; i++) {
		if ( this.arr[i].key === key ) {
    		this.arr[i].value = value;
			isNew = false;
    		break;
		}
	}
	if(isNew){
		this.arr[this.arr.length] = new struct(key, value);
	}
}
var get = function(key) {
	for (var i = 0; i < this.arr.length; i++) {
		if ( this.arr[i].key === key ) {
			return this.arr[i].value;
		}
	}
	return null;
}
var getItem = function(index) {
	if (index < this.arr.length) {
		return this.arr[index];
	}
	return null;
}
var remove = function(key) {
	var v;
	for (var i = 0; i < this.arr.length; i++) {
		v = this.arr.pop();
		if ( v.key === key ) {
			continue;
   		}
		this.arr.unshift(v);
	}
}
var size = function() {
	return this.arr.length;
}

var empty = function() {
	this.arr=[];
}

var isEmpty = function() {
	return this.arr.length <= 0;
}
this.arr = new Array();
this.get = get;
this.put = put;
this.getItem = getItem;
this.remove = remove;
this.size = size;
this.empty = empty;
this.isEmpty = isEmpty;
}