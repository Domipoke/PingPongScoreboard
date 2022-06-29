function isValid(s) {
    if (s.length>0&&s!=null&&s!=undefined) {
        return true
    } else return false
}

class Menu extends HTMLElement {
    constructor (props) {
        super(props)
        this.appendChild(this.createMenu())
    }
    loadUser(user) {
        var userbtn = document.createElement("div")
        userbtn.classList.add("menu-item-span")
        userbtn.classList.add("right")
        userbtn.innerHTML=user.email
        userbtn.addEventListener("click", this.userpagediv)
        document.getElementById("login").replaceWith(userbtn)
        document.getElementsByClassName("login-frame")[0].remove()
    }
    

    setupmenu() {
        var d = document.createElement('div')
        d.classList.add("login-frame-div")
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
        lgel.email.type="email"
        lgel.lb.email.classList.add("login-form-email-label")
        lgel.lb.email.innerHTML="Email"
        lgel.pwd.classList.add("login-form-pwd")
        lgel.pwd.type="password"
        lgel.lb.pwd.classList.add("login-form-pwd-label")
        lgel.lb.pwd.innerHTML="Password"
        lgel.btn.classList.add("login-form-btn")
        lgel.btn.innerHTML="Login"
    
        lgel.btn.addEventListener("click", this.loginEventListener)
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

        var style = document.createElement("link")
        style.setAttribute("type","text/css")
        style.setAttribute("rel","stylesheet")
        style.setAttribute("href","./css/login.css")

        d.appendChild(style)
        d.appendChild(all)
        return d;
    }
    loginEvent() {
        var div = this.setupmenu()
        if (document.getElementsByClassName(div.classList.toString()).length==0) document.body.appendChild(div)
    
    }
    
    
    loginEventListener(e) {
        var e = document.getElementsByClassName("login-form-email")[0]
        var p = document.getElementsByClassName("login-form-pwd")[0]
        var ue = "domenico.montuori1312006@gmail.com"//e.value
        var up = "domipoke3D"//p.value
        if (isValid(ue) && isValid(up) ) {
            //https://firebase.google.com/docs/auth/web/firebaseui
            var fa;
            if (firebase.apps.length===0) fa=firebase.initializeApp({apiKey: "AIzaSyCnyVeuMNfIEPrB4GFnU3gepf6FMRctrWw",databaseURL: "https://gp4e-6a225.firebaseio.com",projectId: "gp4e-6a225",storageBucket: "gp4e-6a225.appspot.com",});
            else fa = firebase.apps[0]
            const app = fa;
            const auth = app.auth();
            if (auth!=null) {
                auth.signInWithEmailAndPassword(ue, up)
                .then((userCredential) => {
                    // Signed in
                    var user = userCredential.user;
                    this.loadUser(user)
                })
                .catch((error) => {
                    var errorCode = error.code;
                    var errorMessage = error.message;
                    auth.createUserWithEmailAndPassword(ue, up)
                    .then((userCredential) => {
                        // Signed in 
                        var user = userCredential.user;
                        this.loadUser(user)
                    })
                    .catch((error) => {
                        var errorCode = error.code;
                        var errorMessage = error.message;
                        // ..
                    });
                });
            } else {
                this.loadUser(auth.current0User)
            }
        }
    }
    
    userpagediv(e) {
        
    }

    createMenu() {
        var menu = document.createElement("div")
        menu.id="menu"
        var style = document.createElement("link")
        style.setAttribute("type","text/css")
        style.setAttribute("rel","stylesheet")
        style.setAttribute("href","./css/menu.css")

        var logo = document.createElement("img")
        logo.src="./img/logo.svg"
        logo.classList.add("menu-item-image")
        logo.classList.add("rounded")

        var amatch = document.createElement("a")
        amatch.href="./match.html"
        amatch.classList.add("menu-item-span")
        amatch.innerHTML="Match"
        var auser = document.createElement("a")
        auser.href="./user.html"
        auser.classList.add("menu-item-span")
        auser.innerHTML="User"

        var blogin = document.createElement("button")
        blogin.innerHTML="Login"
        blogin.id="login"
        blogin.classList.add("menu-item-span")
        blogin.classList.add("right")
        blogin.addEventListener("click",()=>{
            var app;
            if (firebase.apps.length===0) {
                app=firebase.initializeApp({
                    apiKey: "AIzaSyCnyVeuMNfIEPrB4GFnU3gepf6FMRctrWw",
                    //authDomain: "PROJECT_ID.firebaseapp.com",
                    // The value of `databaseURL` depends on the location of the database
                    databaseURL: "https://gp4e-6a225.firebaseio.com",
                    projectId: "gp4e-6a225",
                    storageBucket: "gp4e-6a225.appspot.com",
                    //messagingSenderId: "SENDER_ID",
                    //appId: "APP_ID",
                    // For Firebase JavaScript SDK v7.20.0 and later, `measurementId` is an optional field
                    //measurementId: "G-MEASUREMENT_ID",
                })
            } else {
                app = firebase.apps[0]
            }
            var user = app.auth().currentUser;
            console.log(user)
            if (user) this.loadUser(user) 
            else this.loginEvent()
        })




        menu.appendChild(style)
        menu.appendChild(logo)
        menu.appendChild(amatch)
        menu.appendChild(auser)
        menu.appendChild(blogin)
        return menu
    }
} 

customElements.define("custom-menu",Menu)