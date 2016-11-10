function doOverlay(x0, x1, y0, y1, zoomFactor) {
	posx = x1;
	posy = y1;
	initx = x0;
	inity = y0;
	d = document.createElement('div');
	d.setAttribute('name', 'overlaydiv');
	d.className = 'square';
	d.style.left = initx + 'px';
	d.style.top = inity + 'px';
	var width = (posx - initx) * zoomFactor;
	var height = (posy - inity) * zoomFactor;
	d.style.width = Math.abs(width) + 'px';
	d.style.height = Math.abs(height) + 'px';
	document.getElementsByTagName('body')[0].appendChild(d);
}
function doOverlayById(overlayDivId, x0, x1, y0, y1, zoomFactor) {
	posx = x1;
	posy = y1;
	initx = x0;
	inity = y0;
	d = document.createElement('div');
	d.setAttribute('name', 'overlaydiv');
	d.setAttribute('id', overlayDivId);
	d.className = 'square';
	d.style.left = initx + 'px';
	d.style.top = inity + 'px';
	var width = (posx - initx) * zoomFactor;
	var height = (posy - inity) * zoomFactor;
	d.style.width = Math.abs(width) + 'px';
	d.style.height = Math.abs(height) + 'px';
	document.getElementsByTagName('body')[0].appendChild(d);
}

function doOverlayForKey(x0, x1, y0, y1, zoomFactor) {

	posx = x1;
	posy = y1;
	initx = x0;
	inity = y0;
	d = document.createElement('div');
	d.setAttribute('name', 'overlaydivkey');
	d.className = 'secondSquare';
	d.style.left = initx + 'px';
	d.style.top = inity + 'px';
	var width = (posx - initx) * zoomFactor;
	var height = (posy - inity) * zoomFactor;
	d.style.width = Math.abs(width) + 'px';
	d.style.height = Math.abs(height) + 'px';
	document.getElementsByTagName('body')[0].appendChild(d);
}

function doOverlayForValue(x0, x1, y0, y1, zoomFactor) {

	posx = x1;
	posy = y1;
	initx = x0;
	inity = y0;
	d = document.createElement('div');
	d.setAttribute('name', 'overlaydivvalue');
	d.className = 'thirdSquare';
	d.style.left = initx + 'px';
	d.style.top = inity + 'px';
	var width = (posx - initx) * zoomFactor;
	var height = (posy - inity) * zoomFactor;
	d.style.width = Math.abs(width) + 'px';
	d.style.height = Math.abs(height) + 'px';
	document.getElementsByTagName('body')[0].appendChild(d);
}

function doOverlayForRow(x0, x1, y0, y1, zoomFactor) {
	posx = x1;
	posy = y1;
	initx = x0;
	inity = y0;
	d = document.createElement('div');
	d.setAttribute('name', 'overlaydiv');
	d.setAttribute('onselectstart', "return false");
	d.setAttribute('onmousedown', "removeTableOverlay()");
	d.setAttribute('oncontextmenu', "return false");
	d.className = 'squareRow';
	d.style.left = initx + 'px';
	d.style.top = inity + 'px';
	var width = (posx - initx) * zoomFactor;
	var height = (posy - inity) * zoomFactor;
	d.style.width = Math.abs(width) + 'px';
	d.style.height = Math.abs(height) + 'px';
	document.getElementsByTagName('body')[0].appendChild(d);
}

function removeTableOverlay(){
	removeOverlay();
	removeOverlay();
}

function removeOverlayById(overlayDivId) {
	var di = document.getElementById(overlayDivId);
	if (di) {
		di.parentNode.removeChild(di);
	}
}

function removeOverlay() {
	/*
	 * var di = document.getElementsByName('overlaydiv'); i = 0; if (di) { if
	 * (di.length > 0) { while (i < di.length) {
	 * document.getElementsByTagName('body')[0].removeChild(di[i]); i++; } } }
	 */

	var inputs = document.getElementsByTagName('div');
	for ( var i = 0; i < inputs.length; i++) {
		if (inputs.item(i).getAttribute('name') == 'overlaydiv') {
			document.getElementsByTagName('body')[0].removeChild(inputs[i]);
		}

	}

	var inputs = document.getElementsByTagName('div');
	for ( var i = 0; i < inputs.length; i++) {
		if (inputs.item(i).getAttribute('name') == 'overlaydivkey') {
			document.getElementsByTagName('body')[0].removeChild(inputs[i]);
		}

	}

	var inputs = document.getElementsByTagName('div');
	for ( var i = 0; i < inputs.length; i++) {
		if (inputs.item(i).getAttribute('name') == 'overlaydivvalue') {
			document.getElementsByTagName('body')[0].removeChild(inputs[i]);
		}

	}

	/*
	 * var di = document.getElementsByName('overlaydivkey'); i = 0; if (di) { if
	 * (di.length > 0) { while (i < di.length) {
	 * document.getElementsByTagName('body')[0].removeChild(di[i]); i++; } } }
	 * var di = document.getElementsByName('overlaydivvalue'); i = 0; if (di) {
	 * if (di.length > 0) { while (i < di.length) {
	 * document.getElementsByTagName('body')[0].removeChild(di[i]); i++; } } }
	 */
}

function getViewPortHeight() {
	viewportheight = window.innerHeight;
	return viewportheight;
}

function getViewPortHeightForIE() {
	viewportheight = document.documentElement.clientHeight;
	return viewportheight;
}

function getViewPortWidthForIE() {
	viewportwidth = document.documentElement.clientWidth;
	return viewportwidth;
}

function loginSubmit() {
	document.getElementById("loginForm").submit();
}

function getViewPortWidth() {
	return window.innerWidth || document.documentElement.clientWidth
			|| document.body.clientWidth;
}
window.onbeforeunload = function() {
	onCloseWindow();
	return;
}

var onmessage = function(e) {
	var document = window.top.document;
	var element;
	if (e.data == "Save") {
		element = document.getElementById("okButtonElement");
	} else if (e.data == "Cancel") {
		element = document.getElementById("closeButtonElement");
	}
	if (element != null) {
		element.click();
	}
}

if (typeof window.addEventListener != 'undefined') {
	window.addEventListener('message', onmessage, false);
} else if (typeof window.attachEvent != 'undefined') {
	window.attachEvent('onmessage', onmessage);
}

function performScannerAction(action) {
	window.document.webScannerApplet.init(action);
}

function setScannerProperties(keys, values, delimiter) {
	window.document.webScannerApplet.setScannerProperties(keys, values,
			delimiter);
}

function setImageDeleted() {
	window.document.webScannerApplet.onImageDelete();
}

/**
 * Sets the scanner stop status to <code>false</code> when start button is
 * clicked.
 */
function setScannerStopStatusOnStart() {
	window.document.webScannerApplet.setScannerStopStatusOnStart();
}

/*
 * To apply the selected theme.
 */
function loadJs(url) {
	var oldLink = document.getElementsByTagName("script").item(0);
	var newLink = document.createElement("script");
	newLink.setAttribute("type", "text/javascript");
	newLink.setAttribute("language", "javascript");
	newLink.setAttribute("src", url);
	document.getElementsByTagName("head").item(0)
			.replaceChild(newLink, oldLink);
}

/*
 * Sets the jquery resizable functionality to the UI elements whose IDs are
 * passed as parameter.
 */
function setResizableProperty(resizableId, height, width) {
	var resizeId = '#' + resizableId;
	$(resizeId).resizable({minHeight : height,minWidth : width});
}

/*
Set the value under the tag with tagName to the value
*/
function setTagValue(tagName,value) {

	// For IE browser, title element doesn't work as expected
	if (tagName === 'title') {
		document.title=value;
	} else {
		var tag= document.getElementsByTagName(tagName)[0];
		tag.text=value;
	}
}
/*
Removes all the child nodes of under the node
*/
function removeAllChild(node) {
while (node.hasChildNodes()) {
    node.removeChild(node.firstChild);
}
}

/*
Sets the value under the tag with id to the value
*/
function setIDValue(id,value) {
	var tag= document.getElementById(id);
	tag.innerHTML=value;
}

/*
 * Adds the jquery zoom on mouseover functionality on the container ID passed.
 */
function addZoomProperty(containerId) {
	var parentId = '#' + containerId;
	$(parentId).zoom({magnify: '1'});
}

/*
 * Removes the jquery zoom on mouseover functionality on the container ID passed.
 */
function removeZoomProperty(containerId) {
	var parentId = '#' + containerId;
	$(parentId).trigger('zoom.destroy');
}

/* Add jquery resizable functionality on the ids passed. */
function resizeElements(resizableId, childId, width, height) {
	var resizeId = '#' + resizableId;
	var childId = '#' + childId;
	$(childId).resizable({alsoResize:resizeId , minHeight : height, minWidth : width});
}

function validateRegex(){
	try {
	    return new RegExp("(?i)abc");
	}
	catch(e) {
	    return false;
	}
}

function documentvalidate(batchInstanceIdentifier,documentName,batchClassIdentifier) {
	return true;
	}


function getDLFValue(fieldName) {
	if(null != fieldName) {
	var dlfElement = document.getElementById(fieldName)

	if(null != dlfElement) {
	return dlfElement.getElementsByTagName("input")[0].value;
	}
	}
	return null;
}