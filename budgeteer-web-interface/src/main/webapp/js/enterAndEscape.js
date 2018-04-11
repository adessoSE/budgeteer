// add listener for keydown event
document.addEventListener("keydown", function(evt) {
    // get the event
    evt = evt || window.event;
    var isEnter = false;
    var isEscape = false;
    // check the key
    if ("key" in evt) {
        isEnter = (evt.key == "Enter");
        isEscape = (evt.key == "Escape" || evt.key == "Esc");
    } else {
        isEnter = (evt.keyCode == 13);
        isEscape = (evt.keyCode == 27);
    }
    // if enter is pressed, click submit
    if (isEnter) {
        evt.preventDefault();
        document.getElementById("submitButton").click();
    }
    // if esc is pressed, click cancel
    else if (isEscape) {
        evt.preventDefault();
        document.getElementById("cancel").click();
    }
});
