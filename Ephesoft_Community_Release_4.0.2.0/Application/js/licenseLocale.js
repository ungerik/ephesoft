function changeLocale(locale) {
	var topMessage = document.getElementById("topMessage");
	var bottomMessage = document.getElementById("bottomMessage");
	var ephesoftLink = document.getElementById("ephesoftLink");
	var r1 = document.getElementById("r1");
	var r2 = document.getElementById("r2");
	var r3 = document.getElementById("r3");
	
		if ("fr" == locale) {
			changeToFrench(topMessage, bottomMessage, ephesoftLink, r1, r2, r3);
			toggleCSS(locale);
		} else if ("tr" == locale) {
			changeToTurkish(topMessage, bottomMessage, ephesoftLink, r1, r2, r3);
			toggleCSS(locale);
		} else {
			changeToEnglish(topMessage, bottomMessage, ephesoftLink, r1, r2, r3);
			toggleCSS(locale);
		}
}

function changeToFrench(topMessage, bottomMessage, ephesoftLink, r1, r2, r3) {
	topMessage.innerHTML = "Votre licence Ephesoft pas pu etre verifiee. Les causes possibles peuvent etre:";
	bottomMessage.innerHTML = "S'il vous plait contacter votre administrateur systeme pour obtenir de l'aide.";
	ephesoftLink.innerHTML = "Propulse par Ephesoft";
	r1.innerHTML = "Licence Ephesoft n'est pas installe sur votre systeme.";
	r2.innerHTML = "La licence installee est invalide ou expire.";
	r3.innerHTML = "Serveur de licence ne fonctionne pas comme prevu ou est inaccessible.";
}

function changeToTurkish(topMessage, bottomMessage, ephesoftLink, r1, r2, r3) {
	topMessage.innerHTML = "Sizin Ephesoft lisans dogrulanamadi. Olasi nedenler olabilir:";
	bottomMessage.innerHTML = "Daha fazla yardim icin sistem yoneticinize basvurun.";
	ephesoftLink.innerHTML = "Ephesoft Powered by";
	r1.innerHTML = "Ephesoft lisans sisteminizde yuklu degil.";
	r2.innerHTML = "Kurulu lisans gecersiz veya suresi dolmus oldugunu.";
	r3.innerHTML = "License server is not working as expected or is unreachable.";
}

function changeToEnglish(topMessage, bottomMessage, ephesoftLink, r1, r2, r3) {
	topMessage.innerHTML = "Your Ephesoft license could not be verified. Possible causes may be: ";
	bottomMessage.innerHTML = "Please contact your system administrator for further assistance. ";
	ephesoftLink.innerHTML = "Powered by Ephesoft";
	r1.innerHTML = "Ephesoft license is not installed on your system.";
	r2.innerHTML = "The installed license is invalid or expired.";
	r3.innerHTML = "License server is not working as expected or is unreachable.";
}

function toggleCSS(locale) {
	var englishLink = document.getElementById("englishLink");
	var turkishLink = document.getElementById("turkishLink");
	var frenchLink = document.getElementById("frenchLink");
	if ("fr" == locale) {
			englishLink.className = "blueLink";
			turkishLink.className = "blueLink";
			frenchLink.className = "usedLink";
		} else if ("tr" == locale) {
			englishLink.className = "blueLink";
			turkishLink.className = "usedLink";
			frenchLink.className = "blueLink";
		} else {
			englishLink.className = "usedLink";
			turkishLink.className = "blueLink";
			frenchLink.className = "blueLink";
		}
}