function loginEvent(e) {
    var div = setupmenu()
    document.body.appendChild(div)
}

function setupmenu() {
    var all = document.createElement("div")
    all.classList.add("login-frame") 
    
    var login = document.createElement("div")
    login.classList.add("login-form")

    var ppt = document.createElement("div")
    ppt.classList.add("login-ppt")

    all.appendChild(login)
    all.appendChild(ppt)
    return all;
}