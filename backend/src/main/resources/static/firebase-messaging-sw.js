importScripts("https://www.gstatic.com/firebasejs/10.12.4/firebase-app-compat.js");
importScripts("https://www.gstatic.com/firebasejs/10.12.4/firebase-messaging-compat.js");

firebase.initializeApp({
    apiKey: "AIzaSyDI3-fgGDZZyc9LsH_VIKnZF28MuyR7IU8",
    authDomain: "allforone-df977.firebaseapp.com",
    projectId: "allforone-df977",
    storageBucket: "allforone-df977.firebasestorage.app",
    messagingSenderId: "451259708327",
    appId: "1:451259708327:web:3d9c418abb34d5dc903c82",
    measurementId: "G-GM2V27JSC3"
});

const messaging = firebase.messaging();

messaging.onBackgroundMessage((payload) =>
    log("onMessage: " + JSON.stringify(payload))
);