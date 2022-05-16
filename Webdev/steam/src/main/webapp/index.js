"use strict";

function login(){
    window.location.href="series.html";

    //alert("Eigegebener Username: "+ document.getElementById("id_username").innerHML + "\n Eigegebenes Passwort:" + document.getElementById("id_password").innerHTML);
}

let btn_login = document.getElementById("id_login");
btn_login.onclick = login;