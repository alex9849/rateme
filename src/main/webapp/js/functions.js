let mymap;
let redIcon;
let blueIcon;
let selectedMarker;

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

		setPubHeadline(poi);

		let infoArea = document.querySelector("#infoarea");
		infoArea.innerHTML = '';
		infoArea.appendChild(generateTagTable(poi.poiTags));

	}
}

function setPubHeadline(poi) {
	let infolink = document.createElement("a");
	infolink.innerText = "Infos";
	let linkAtt = document.createAttribute("href");
	linkAtt.value = "javascript:showTags()";
	infolink.setAttributeNode(linkAtt);
	document.querySelector("#pubheadline").innerHTML = getName(poi) + " (" + infolink.outerHTML + ")";
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

function showTags() {
	document.querySelector("#infoareawbutton").style.display = "block";
}

function hideInfoArea() {
	document.querySelector("#infoareawbutton").style.display = "none";
}

function getName(poi) {
	for(let tag of poi.poiTags) {
		if(tag.tag === "name") {
			return tag.value;
		}
	}
	return 'Unbenannt';
}


function showRegistration() {
	document.querySelector("#registration").style.display = "block";
    //document.getElementById("registration").style.display = "block";
}

function hideRegistration() {
	document.querySelector("#registration").style.display = "none";
    //document.getElementById("registration").style.display = "none";
}

