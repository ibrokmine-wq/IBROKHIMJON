function updateClock() {
	const now = new Date();
	let hours = now.getHours().toString().padStart(2, '0');
	let minutes = now.getMinutes().toString().padStart(2, '0');
	document.getElementById('clock').textContent = `${hours}:${minutes}`;
}

async function testSpeed() {
	const imageUrl = 'https://upload.wikimedia.org/wikipedia/commons/3/3f/Fronalpstock_big.jpg';
	const startTime = new Date().getTime();
	const response = await fetch(imageUrl + '?rand=' + Math.random());
	const blob = await response.blob();
	const endTime = new Date().getTime();

	const duration = (endTime - startTime) / 1000;
	const bitsLoaded = blob.size * 8;
	const kbps = bitsLoaded / duration / 1024;
	const result = Math.round(kbps);

	document.getElementById('speed-box').textContent = `${result} kb`;
}

setInterval(testSpeed, 1000);
testSpeed();

setInterval(updateClock, 3000);
updateClock();

function unlockScreen() {
	document.getElementById('lockScreen').classList.add('hidden');
	document.getElementById('mainContent').classList.add('active');
}
const blok = document.querySelector('.blok');
const all = document.querySelector('.och');
const kuzov = document.querySelector('.kuzov');
const camera = document.querySelector('.camera1');
console.log(camera);

all.addEventListener('click', function () {
	blok.classList.toggle('active');
	kuzov.classList.toggle('active');
});

document.getElementById('searchInput').addEventListener('keydown', function (event) {
	if (event.key === 'Enter') {
		const query = this.value;
		const url = 'https://www.google.com/search?q=' + encodeURIComponent(query);
		window.open(url, '_blank');
	}
});

function updateWiFiStatus() {
	const box = document.getElementById('wifi-box');
	const connection = navigator.connection || navigator.mozConnection || navigator.webkitConnection;

	if (connection && connection.type) {
		if (connection.type === '') {
			box.textContent = '📶';
		} else {
			box.textContent = '';
		}
	} else if (connection && connection.effectiveType) {
		if (connection.effectiveType === '3g') {
			box.textContent = '📶';
		} else {
			box.textContent = '❌ ';
		}
	} else {
		box.textContent = '';
	}
}

setInterval(updateWiFiStatus, 1000);
updateWiFiStatus();

const batteryBox = document.getElementById('battery-box');

async function updateBattery() {
	if (navigator.getBattery) {
		const battery = await navigator.getBattery();
		const level = Math.round(battery.level * 100);
		const charging = battery.charging;
		const icon = charging ? '🔌' : '🔋';
		batteryBox.textContent = `${icon} ${level}%`;
	} else {
		batteryBox.textContent = 'Zaryad: Noma’lum';
	}
}

setInterval(updateBattery, 1000);
updateBattery();

function startRecognition() {
	if (!('webkitSpeechRecognition' in window)) {
		alert("Brauzeringiz ovozli qidiruvni qo'llab-quvvatlamaydi.");
		return;
	}

	recognition = new webkitSpeechRecognition();
	recognition.lang = 'en-US';
	recognition.interimResults = false;
	recognition.maxAlternatives = 1;

	recognition.onresult = function (event) {
		const transcript = event.results[0][0].transcript;
		document.getElementById('searchInput').value = transcript;
		searchGoogle(transcript);
	};

	recognition.onerror = function (event) {
		console.error('Xatolik:', event.error);
	};

	recognition.start();
	document.getElementById('micBtn').classList.add('recording');
}

function stopRecognition() {
	if (recognition) {
		recognition.stop();
		document.getElementById('micBtn').classList.remove('recording');
	}
}

const micBtn = document.getElementById('micBtn');
micBtn.addEventListener('mousedown', startRecognition);
micBtn.addEventListener('mouseup', stopRecognition);
micBtn.addEventListener('mouseleave', stopRecognition);

function hover() {}
