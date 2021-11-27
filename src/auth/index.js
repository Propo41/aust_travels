// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";

// todo: use env file for the credentials
const firebaseConfig = {
  apiKey: "AIzaSyBEP0u2GZqxa0rO3ns0kq8T7NtzgrW995M",
  authDomain: "aust-travels.firebaseapp.com",
  databaseURL:
    "https://aust-travels-default-rtdb.asia-southeast1.firebasedatabase.app",
  projectId: "aust-travels",
  storageBucket: "aust-travels.appspot.com",
  messagingSenderId: "714054307139",
  appId: "1:714054307139:web:dcbe64e4f75dad249b73f6",
  measurementId: "G-DBYMDB47LP",
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
export default app;
