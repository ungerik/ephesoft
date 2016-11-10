(function () {

  /* Session time out (in minutes) */
  var sessionTimeout = 30;

  /* Amount of time before session time out when user is warned (in minutes) */
  var warningTime = 1;
  var warningDelay = sessionTimeout - warningTime;
	$(window).load(function () {
	var dialogHtml = "<div id='overlay'><div class='modal gwt-DialogBox centreAlignDiv'><div class='Caption'>Session Expiration</div>" +
						"<div class='modal_text'>Your session is about to expire.</div> <div class='modal_buttons'>" +
						"<button class='gwt-Button' type='submit' onclick='renewSession();'>Continue</button>" +
						"<button class='gwt-Button' type='submit' onclick='hideWarningPopup();'>Cancel</button></div></div></div>";
	$('body').append(dialogHtml);

	initializeLocalStorage();
	startTimeoutTimer(warningDelay * 60000);
	triggerStorageEvent("sessionRenewTrigger");
  });
  
  function initializeLocalStorage() {
	// Set session timeout in local storage
	var currentDate = new Date();
	var now = currentDate.getTime();
	window.timeout = now + sessionTimeout * 60 * 1000;
	localStorage["timeout"] = window.timeout;

	localStorage["popupTrigger"] = 0;
	localStorage["sessionRenewTrigger"] = 0;
	localStorage["sessionExpiryTrigger"] = 0;
	localStorage['lastStorageEventKey'] = 0;
  }
  
  
  /* Starts a timer to repeatedly check for session expiry after specified duration */
  function startTimeoutTimer(warningDelay) {
    setTimeout(function () {
      refreshTimeoutValue();

      var timeLeft = (window.timeout - Date.parse(new Date()));

      if (timeLeft <= warningTime * 60000) {
        if (localStorage["popupTrigger"] == 0) {
          warnUser();
          checkForSessionExpiration();
        }

      } else {
        startTimeoutTimer(timeLeft - warningTime * 60000);
      }
    }, warningDelay);
  }

  window.showWarningPopup = function () {
    document.getElementById("overlay").style.display = "block";
  };
  
  window.hideWarningPopup = function () {
    document.getElementById("overlay").style.display = "none";
  };

  /* Refreshes the session by making an ajax request to the server */
  window.renewSession = function () {
    $.ajax({
	  url: "/dcma/sessiontimeout"
	}).done(function(sessionTimeoutValue) {
	  // Hide session timeout pop-up
	  hideWarningPopup();
	  clearInterval(window.refreshIntervalId);
	  initializeLocalStorage();
	  window.timeout = sessionTimeoutValue;
	  startTimeoutTimer(warningDelay * 60000);
	  triggerStorageEvent("sessionRenewTrigger");
	});
  };

  /* Handles events that are raised from other tabs/windows */
  function storageEventHandler(evt) {
    var key = null;

    if (window.addEventListener) {
      key = evt.key;
    } else if (window.attachEvent) {
      key = localStorage['lastStorageEventKey'];
    }
	
    switch (key) {
      case 'timeout':
        window.timeout = localStorage["timeout"];
        break;
      case 'popupTrigger':
        if (localStorage["popupTrigger"] > 0 && localStorage["popupTrigger"] < 50) {
          warnUser();
          checkForSessionExpiration();
        }
        break;
      case 'sessionRenewTrigger':
        if (localStorage["sessionRenewTrigger"] > 0  && localStorage["sessionRenewTrigger"] < 50) {
		  hideWarningPopup();
		  clearInterval(window.refreshIntervalId);
		  startTimeoutTimer(warningDelay * 60000);
        }
        break;
      case 'sessionExpiryTrigger':
        if (localStorage["sessionExpiryTrigger"] > 0  && localStorage["sessionExpiryTrigger"] < 50) {
		  document.location.href = 'home.html';
        }
        break;
    }
  }


  // Add the event listener
  if (window.addEventListener) {
    /* Listen for the storage event (triggered when the
     storage is modified - setItem, removeItem, clear) */
    window.addEventListener("storage", storageEventHandler, true);
  } else if (window.attachEvent) {
    /* Listen for the storage event on the DOM object
    (Hack since attachEvent is an IE 8 property) */
    document.attachEvent("onstorage", storageEventHandler);
  }

  var triggerStorageEvent = function (key) {
		// IE8 fix for missing key when event is fired
		if (window.attachEvent) {
		  localStorage['lastStorageEventKey'] = key;
		}
		localStorage[key] = Number(localStorage[key]) + 1; 
  };

  var warnUser = function () {
    window.showWarningPopup();
    triggerStorageEvent("popupTrigger");
  };

  /* Checks if session has expired and redirects user to home page after triggering session timeout */
  var checkForSessionExpiration = function () {
    window.refreshIntervalId = setInterval(function () {
      refreshTimeoutValue();
      var curr = Date.parse(new Date());
      if (curr >= window.timeout) {
		triggerStorageEvent("sessionExpiryTrigger");
		document.location.href = 'home.html'; 
      }
    }, 5000);
  };

  /* Refreshes timeout global variable with highest known session timeout value */
  function refreshTimeoutValue() {
    var cookieTimeout = getCookie('sessionExpiry');

    if (cookieTimeout && (cookieTimeout > localStorage["timeout"])) {
      window.timeout = cookieTimeout;
    } else {
      window.timeout = localStorage["timeout"];
    }
  }

  /* Cookie utility functions */
  if (typeof String.prototype.trimLeft !== "function") {
    String.prototype.trimLeft = function () {
      return this.replace(/^\s+/, "");
    };
  }
  if (typeof String.prototype.trimRight !== "function") {
    String.prototype.trimRight = function () {
      return this.replace(/\s+$/, "");
    };
  }
  if (typeof Array.prototype.map !== "function") {
    Array.prototype.map = function (callback, thisArg) {
      for (var i = 0, n = this.length, a = []; i < n; i++) {
        if (i in this) a[i] = callback.call(thisArg, this[i]);
      }
      return a;
    };
  }
  function getCookies() {
    var c = document.cookie, v = 0, cookies = {};
    if (document.cookie.match(/^\s*\$Version=(?:"1"|1);\s*(.*)/)) {
      c = RegExp.$1;
      v = 1;
    }
    if (v === 0) {
      c.split(/[,;]/).map(function (cookie) {
        var parts = cookie.split(/=/, 2),
          name = decodeURIComponent(parts[0].trimLeft()),
          value = parts.length > 1 ? decodeURIComponent(parts[1].trimRight()) : null;
        cookies[name] = value;
      });
    } else {
      c.match(/(?:^|\s+)([!#$%&'*+\-.0-9A-Z^`a-z|~]+)=([!#$%&'*+\-.0-9A-Z^`a-z|~]*|"(?:[\x20-\x7E\x80\xFF]|\\[\x00-\x7F])*")(?=\s*[,;]|$)/g).map(function ($0, $1) {
        var name = $0,
          value = $1.charAt(0) === '"'
            ? $1.substr(1, -1).replace(/\\(.)/g, "$1")
            : $1;
        cookies[name] = value;
      });
    }
    return cookies;
  }

  function getCookie(name) {
    return getCookies()[name];
  }

})();