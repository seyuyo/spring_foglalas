var signUpButton = document.getElementById("signUpButton");
var signUpContainer = document.querySelector(".sign-up-container");
var signInButton = document.getElementById("signInButton");
var signInContainer = document.querySelector(".sign-in-container");
var overlay = document.querySelector(".overlay");

signUpButton.addEventListener("click", function() {
    // Az overlay elem jobbra mozgatása
    overlay.classList.remove("overlay-left");
    overlay.classList.add("overlay-right");

    // A bejelentkezési űrlap elrejtése
    signInContainer.style.display = "none";

    // A regisztrációs űrlap megjelenítése
    signUpContainer.style.display = "block";
});

signInButton.addEventListener("click", function() {
    // Az overlay elem balra mozgatása
    overlay.classList.remove("overlay-right");
    overlay.classList.add("overlay-left");

    // A regisztrációs űrlap elrejtése
    signUpContainer.style.display = "none";

    // A bejelentkezési űrlap megjelenítése
    signInContainer.style.display = "block";
});
