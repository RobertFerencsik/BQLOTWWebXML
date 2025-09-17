function showMenu() {
    document.getElementById("menuItems").style.display = "block";
}

function hideMenu() {
    document.getElementById("menuItems").style.display = "none";
}


    var videom = document.getElementById('videom');

function  indit_leallit(){

        var videom = document.getElementById('videom');

    if (videom.paused){
    videom.play();
    }else{
    videom.pause();
    }
}
