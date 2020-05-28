let mymap;
let redIcon;
let blueIcon;
let selectedMarker;
let currentUser;

window.onload = function() {
	mymap = L.map('mapid').setView([ 49.250723, 7.377122 ], 13);

	L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?'
	                +'access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpe'
	                +'jY4NXVycTA2emYycXBndHRqcmZ3N3gifQ'
	                +'.rJcFIG214AriISLbB6B5aw',
	   {  maxZoom     : 18,
	      attribution : 'Map data &copy; '
	                     + '<a href="https://www.openstreetmap.org/">OpenStreetMap</a>'
	                     + ' contributors, '
	                     + '<a href="https://creativecommons.org/licenses/by-sa/2.0/">'
	                     + 'CC-BY-SA</a>, '
	                     + 'Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',	
	      id         : 'mapbox.streets' 
	   }).addTo(mymap);
	redIcon = new L.Icon({
		iconUrl: './icon/marker-icon-red.png',
		shadowUrl: './icon/marker-shadow.png',
		iconSize: [25, 41],
		iconAnchor: [12, 41],
		popupAnchor: [1, -34],
		shadowSize: [41, 41]
	});
	blueIcon = new L.Icon({
		iconUrl: './icon/marker-icon-blue.png',
		shadowUrl: './icon/marker-shadow.png',
		iconSize: [25, 41],
		iconAnchor: [12, 41],
		popupAnchor: [1, -34],
		shadowSize: [41, 41]
	});

	showPoisOnMap();
};

function showPoisOnMap() {
	fetch("rateme/poi")
		.then(response => response.json())
		.then(data => data.forEach(poi => {
			let callback = poiSelectionCallback(poi);
			L.marker([poi.positionX, poi.positionY], {icon: blueIcon})
				.addTo(mymap)
				.on('click', callback);
		})).catch(err => console.log(err));
}

function loginUser() {
	let data = {
		username: document.querySelector("#loginUserName").value,
		password: document.querySelector("#loginPassword").value
	};
	let cfg = {
		method: 'POST',
		headers: { 'Content-type': 'application/json' },
		body: JSON.stringify(data)
	};
	fetch("rateme/user", cfg)
		.then(response => response.json())
		.then(data => {
			currentUser = data;
			updateRatingSubmitDiv();
			updateHeader();
		})
		.catch(err => {
			document.querySelector("#loginErrorArea").innerHTML = "Login failed!";
		})
}

function logoutUser() {
	let cfg = {
		method: 'DELETE',
		headers: { 'Content-type': 'application/json' }
	};
	fetch("rateme/user", cfg)
		.then(data => {
			currentUser = null;
			updateRatingSubmitDiv();
			updateHeader();
		})
		.catch(err => {
			document.querySelector("#logoutErrorArea").innerHTML = "Logout failed!";
		})
}

function poiSelectionCallback(poi) {
	return function (event) {
		if(selectedMarker != null) {
			selectedMarker.setIcon(blueIcon);
		}
		selectedMarker = event.target;
		selectedMarker.setIcon(redIcon);

		console.log("Event");
		console.log(event);
		console.log("Poi");
		console.log(poi);
		poi.poiTags.forEach(item => {
			console.log(item.tag + " " + item.value);
		});

		updatePubHeadline(poi);

		let infoArea = document.querySelector("#infoarea");
		infoArea.innerHTML = '';
		infoArea.appendChild(generateTagTable(poi.poiTags));
		updateRatingSubmitDiv();
	}
}

/* #########################
### Generators / Getters ###
######################### */

function getName(poi) {
	for(let tag of poi.poiTags) {
		if(tag.tag === "name") {
			return tag.value;
		}
	}
	return 'Unbenannt';
}

function generateTagTable(tags) {
	let tagtable = document.createElement("TABE");
	for(let tag of tags) {
		let tagTableRow = document.createElement("TR");
		let tagTableColKey = document.createElement("TD");
		let tagTableColValue = document.createElement("TD");
		tagTableColKey.innerText = tag.tag;
		tagTableColValue.innerText = tag.value;
		tagTableRow.appendChild(tagTableColKey);
		tagTableRow.appendChild(tagTableColValue);
		tagtable.appendChild(tagTableRow);
	}
	return tagtable;
}

/* #########################
######### Updaters #########
######################### */

function updatePubHeadline(poi) {
	let infolink = document.createElement("a");
	infolink.innerText = "Infos";
	let linkAtt = document.createAttribute("href");
	linkAtt.value = "javascript:switchInfoArea()";
	infolink.setAttributeNode(linkAtt);
	document.querySelector("#pubheadline").innerHTML = getName(poi) + " (" + infolink.outerHTML + ")";
}

function updateHeader() {
	if(currentUser != null) {
		document.querySelector("#loginAndRegister").style.display = "none";
		document.querySelector("#currentUserInfo").style.display = "block";
		document.querySelector("#currentUserInfoHello").innerHTML = "Hallo " + currentUser.username + "!";
		document.querySelector("#loginErrorArea").innerHTML = "";
	} else {
		document.querySelector("#loginAndRegister").style.display = "block";
		document.querySelector("#currentUserInfo").style.display = "none";
		document.querySelector("#logoutErrorArea").innerHTML = "";
	}
}

function updateRatingSubmitDiv() {
	if(currentUser != null && selectedMarker != null) {
		document.querySelector("#bewertungsAbgabenDiv").style.display = "block";
	} else {
		document.querySelector("#bewertungsAbgabenDiv").style.display = "none";
	}
}

function updatePasswordCanvas() {
	let registerPassword = document.querySelector("#registerPassword").value;
	let length = registerPassword.length;
	let regexSecialSign = /[!ยง$&?]/;
	let regexNumbers = /[1234567890]/;
	let regexMinorChars = /[abcdefghijklmnopqrstuvwxyz]/;
	let regexMajorChars = /[ABCDEFGHIJKLMNOPQRSTUVWXYZ]/;

	let security = 0;
	if(length >= 5) {
		security = 1;
		if(regexMinorChars.test(registerPassword) && regexMajorChars.test(registerPassword)) {
			security = 2;
			if(regexSecialSign.test(registerPassword) && regexNumbers.test(registerPassword)) {
				security = 3;
				if(length > 7) {
					security = 4;
				}
			}
		}
	}

	var c = document.querySelector("#pwdCanvas");
	var ctx = c.getContext("2d");
	ctx.fillStyle = "red";
	ctx.fillRect(0, 0, 265, 10);
	var grd = ctx.createLinearGradient(0, 0, security * 100, 0);
	grd.addColorStop(0, "green");
	grd.addColorStop(1, "red");
	ctx.fillStyle = grd;
	ctx.fillRect(0, 0, 265, 10);
}

function switchInfoArea() {
	if(document.querySelector("#infoareawbutton").style.display === "block") {
		hideInfoArea();
	} else {
		showInfoArea();
	}
}

function showInfoArea() {
	document.querySelector("#infoareawbutton").style.display = "block";
}

function hideInfoArea() {
	document.querySelector("#infoareawbutton").style.display = "none";
}

function switchRegistration() {
	if(document.querySelector("#registration").style.display === "block") {
		hideRegistration();
	} else {
		showRegistration();
	}
}

function showRegistration() {
	document.querySelector("#registration").style.display = "block";
    //document.getElementById("registration").style.display = "block";
}

function hideRegistration() {
	document.querySelector("#registration").style.display = "none";
    //document.getElementById("registration").style.display = "none";
}

