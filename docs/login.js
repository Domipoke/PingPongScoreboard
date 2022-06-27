function loginEvent(e) {
    var div = setupmenu()
    document.body.appendChild(div)
}

function setupmenu() {
    var all = document.createElement("div")
    all.classList.add("login-frame") 
    
    var login = document.createElement("div")
    login.classList.add("login-form")
    var lgel = {
        email: document.createElement("input"),
        pwd: document.createElement("input"),
        btn: document.createElement("button"),
        lb: {
            email: document.createElement("span"),
            pwd: document.createElement("span")
        }
    }
    
    lgel.email.classList.add("login-form-email")
    lgel.lb.email.classList.add("login-form-email-label")
    lgel.lb.email.innerHTML="Email"
    lgel.pwd.classList.add("login-form-pwd")
    lgel.lb.pwd.classList.add("login-form-pwd-label")
    lgel.lb.pwd.innerHTML="Password"
    lgel.btn.classList.add("login-form-btn")
    lgel.btn.innerHTML="Login"

    lgel.btn.addEventListener("click", ()=>{
        var e = document.getElementsByClassName("login-form-email")[0]
        var p = document.getElementsByClassName("login-form-pwd")[0]
        var ue = e.value
        var up = p.value
        if (isValid(ue)&&isValid(up)) {
            
        }
    })
    login.appendChild(lgel.email)
    login.appendChild(lgel.pwd)
    login.appendChild(lgel.btn)
    login.appendChild(lgel.lb.email)
    login.appendChild(lgel.lb.pwd)
    var ppt = document.createElement("div")
    ppt.classList.add("login-ppt")
    var pptel = {
        table: document.createElement("div"),
        lines: {
            H: document.createElement("div"),
            V: document.createElement("div")
        },
        net: document.createElement("div")
    }
    pptel.table.classList.add("login-ppt-table")
    pptel.lines.H.classList.add("login-ppt-lines-H")
    pptel.lines.V.classList.add("login-ppt-lines-V")
    pptel.net.classList.add("login-ppt-net")
    ppt.appendChild(pptel.table)
    ppt.appendChild(pptel.lines.H)
    ppt.appendChild(pptel.lines.V)
    ppt.appendChild(pptel.net)
    
    all.appendChild(login)
    all.appendChild(ppt)
    return all;
}

function isValid(s) {
    if (s.length>0&&s!=null&&s!=undefined) {
        return true
    } else return false
}