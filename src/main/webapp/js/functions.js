let mymap;
let redIcon;
let blueIcon;
let selectedMarker;
let currentUser = null;
let ownRatings = [];
let pois = [];

window.onload = function() {
	setupButtons();
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
	updateOwnRatings();
};

function setupButtons() {
	document.querySelector("#loginForm").addEventListener("submit", e => {e.preventDefault(); loginUser(e.target.elements.username.value, e.target.elements.password.value, true);});
	document.querySelector("#showRegisterLink").addEventListener("click", e => switchRegistration());
	document.querySelector("#registerAbort").addEventListener("click", e => hideRegistration());
	document.querySelector("#logoutButton").addEventListener("click", e => logoutUser());
	document.querySelector("#registerPassword").addEventListener("keyup", e => updatePasswordCanvas(e.target.value));
	document.querySelector("#registrationForm").addEventListener("submit", e => {e.preventDefault(); submitRegister(e);});
}

function showPoisOnMap() {
	fetch("rateme/poi")
		.then(response => response.json())
		.then(data => {
			pois = data;
			pois.forEach(poi => {
				let callback = poiSelectionCallback(poi);
				L.marker([poi.positionX, poi.positionY], {icon: blueIcon})
					.addTo(mymap)
					.on('click', callback);
			})
		}).catch(err => console.log(err));
}

function loginUser(username, password, displayError) {
	let data = {
		username: username,
		password: password
	};
	let cfg = {
		method: 'POST',
		headers: { 'Content-type': 'application/json' },
		body: JSON.stringify(data)
	};
	fetch("rateme/user", cfg)
		.then(response => {
			if(response.status !== 200) {
				if(displayError) {
					document.querySelector("#loginErrorArea").innerHTML = "Login failed!";
				}
				return;
			}
			return response.json();
		}).then(json => {
		currentUser = json;
		updateRatingSubmitDiv();
		updateHeader();
		loadOwnRatings();
	});
}

function loadOwnRatings() {
	if(currentUser === null) {
		ownRatings = {};
		return;
	}
	let config = {
		method: 'GET',
		headers: { 'Content-type': 'application/json' }
	};
	fetch("/rateme/rating/own")
		.then(response => response.json())
		.then(json => {
			ownRatings = json;
			updateOwnRatings();
		})
}

function logoutUser() {
	let cfg = {
		method: 'DELETE',
		headers: { 'Content-type': 'application/json' }
	};
	fetch("rateme/user", cfg)
		.then(response => {
			if(response.status !== 200) {
				document.querySelector("#logoutErrorArea").innerHTML = "Logout failed!";
				return;
			}
			currentUser = null;
			updateRatingSubmitDiv();
			updateHeader();
			updateOwnRatings();
		});
}

function submitRegister(e) {
	let username = e.target.elements.username.value;
	let password = e.target.elements.password.value;
	let data = {
		firstname: e.target.elements.firstname.value,
		lastname: e.target.elements.lastname.value,
		street: e.target.elements.street.value,
		streetNr: e.target.elements.streetNr.value,
		zip: e.target.elements.zip.value,
		city: e.target.elements.city.value,
		email: e.target.elements.email.value,
		username: username,
		password: password
	};
	let cfg = {
		method: 'PUT',
		headers: { 'Content-type': 'application/json' },
		body: JSON.stringify(data)
	};
	fetch("rateme/user", cfg)
		.then(response => {
			//IF error
			if(!response.ok) {
				response.json().then(json => document.querySelector("#registrationErrorArea").innerHTML = json.message);
				return;
			}
			loginUser(username, password, false);
			hideRegistration();
		});
}

function poiSelectionCallback(poi) {
	return function (event) {
		if(selectedMarker != null) {
			selectedMarker.setIcon(blueIcon);
		}
		selectedMarker = event.target;
		selectedMarker.setIcon(redIcon);

		updatePubHeadline(poi);
		updatePoiRatings(poi);
		hideInfoArea();
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

function generateStars(number) {
	let div = document.createElement("div");
	for(let i = 1; i <= 5; i++) {
		if(i <= number) {
			div.innerHTML += "<img src=\"./icon/star.png\" />";
		} else {
			div.innerHTML += "<img src=\"./icon/star-outline.png\" />";
		}
	}
	return div;
}

/* #########################
######### Updaters #########
######################### */

function updateOwnRatings() {
	let ownRatingsArea = document.querySelector("#ownRatingsArea");
	let table = document.createElement("TABLE");
	table.className += "ownRatingsTable";
	let empty = true;
	let i = 0;
	for(let rating of ownRatings) {
		i++;
		empty = false;
		let row = document.createElement("TR");
		let col1 = document.createElement("TD");
		col1.innerText = getName(pois.find(poi => poi.osmId === rating.osmId))
		let col2 = document.createElement("TD");
		let createDate = new Date(Date.parse(rating.createDt.replace('Z', '')));
		col2.innerText = createDate.toLocaleDateString("de-DE");
		let col3 = document.createElement("TD");
		col3.innerText = rating.text;
		let col4 = document.createElement("TD");
		col4.appendChild(generateStars(rating.grade));
		let col5 = document.createElement("TD");

		row.appendChild(col1);
		row.appendChild(col2);
		row.appendChild(col3);
		row.appendChild(col4);
		row.appendChild(col5);
		if(i % 2 === 1) {
			row.style.backgroundColor = "#F3E2A9";
		} else {
			row.style.backgroundColor = "#F9EECF";
		}
		table.appendChild(row);
	}
	ownRatingsArea.innerHTML = "";
	if(currentUser == null) {
		ownRatingsArea.innerHTML = "Du bist nicht eingeloggt!";
		return;
	}
	if(empty) {
		ownRatingsArea.innerHTML = "Du hast noch nichts bewertet!";
	} else {
		ownRatingsArea.appendChild(table);
	}
}

function updatePoiRatings(poi) {
	let bewertungsDiv = document.querySelector("#bewertungsDiv");
	let config = {
		method: 'GET',
		headers: { 'Content-type': 'application/json' }
	};
	fetch("/rateme/rating/poi/" + poi.osmId, config)
		.then(response => response.json())
		.then(json => {
			bewertungsDiv.innerHTML = "";
			let empty = true;
			for(rating of json) {
				empty = false;
				let stars = generateStars(rating.grade);
				bewertungsDiv.appendChild(stars);
				let divText = document.createElement("div");
				let createDate = new Date(Date.parse(rating.createDt.replace('Z', '')));
				divText.innerText = rating.username + " schreibt am " + createDate.toLocaleDateString("de-DE") + ":";
				bewertungsDiv.appendChild(divText);
				let text = document.createElement("div");
				text.innerText = rating.text;
				bewertungsDiv.appendChild(text);
				let br = document.createElement("br");
				bewertungsDiv.appendChild(br);
			}
			if(empty) {
				bewertungsDiv.innerHTML = "Noch keine Bewertungen!"
			}
		})

}

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

function updatePasswordCanvas(registerPassword) {
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

