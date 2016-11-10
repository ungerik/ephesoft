/* This file contains javascript required for Ephesoft Cloud Home page */

/*
	This function is executed on click of administrator/operator tab for opening 
	and closing of admin/operator links. It ensures that only one tab is open
	at one time. It also changes the arrow button shown on right hand side of 
	respectives tabs.
 */
function jchange(divName) {

	// Array to hold images for opening and closing of tab
	var fold_image = new Array();
	fold_image[0] = "downfold";
	fold_image[1] = "Upfolding";

	// Id of image tag of arrow button
	var foldingImage = (divName == "admin_links") ? 'nfolding' : 'ufolding';

	// Id of other div element other than to be shown on clicked tab
	var otherBlock = (divName == "admin_links") ? 'user_links' : 'admin_links';

	// Id of other img element other than to be shown on clicked tab
	var otherImage = (divName == "admin_links") ? 'ufolding' : 'nfolding';

	// If clicked tab is closed
	if (document.getElementById(divName).style.display == 'none') {
		document.getElementById(divName).style.display = 'block';

		// If other tab is open
		if (document.getElementById(otherBlock).style.display == 'block') {

			// Close the other opened tabs
			document.getElementById(otherBlock).style.display = 'none';
			document.getElementById(otherImage).className = 'Upfolding';
		}

		// Change the arrow image
		if (document.getElementById(foldingImage)) {
			document.getElementById(foldingImage).className = 'downfold';
		}
	} else {

		// Close the clicked tab
		//document.getElementById(divName).style.display = 'none';
		//if (document.getElementById(foldingImage)) {
		//	document.getElementById(foldingImage).src = fold_image[1];
		//}
	}
}

//$(document).ready(function()
//{
//    $(".backup_picture").error(function(){
//        $(this).attr('src', 'images/1.jpg');
//    });
//});

