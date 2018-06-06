// wait until the DOM is ready
window.onload = (function () {

    // Get the input field
    var input = document.getElementById("proCh");
    var count = 0;

    // get the selected item from the dropdown list
    if(input.options[input.selectedIndex].value != "") {
        count = 1;
    }

    // add listener for keydown-event
    document.addEventListener("keydown", function(evt) {
        // get the event
        evt = evt || window.event;
        var isEscape = false;
        // get the key
        if ("key" in evt) {
            isEscape = (evt.key == "Escape" || evt.key == "Esc");
        } else {
            isEscape = (evt.keyCode == 27);
        }
        // if esc is pressed, click backlink or log out
        if (isEscape) {
            evt.preventDefault();
            if (document.getElementById("backlink1") != null)
                document.getElementById("backlink1").click();
            else
                document.getElementById("logout").click();
        }
    });

    // add listener for keypress-event
    input.addEventListener("keypress", function(event) {
      // Number 13 is the "Enter" key on the keyboard
      if (event.keyCode === 13 && count != 1) {
        count = 1;
        console.log("count is" + count);
        // click the dropdown list item
        document.getElementById("proCh").click();
      }
      else if(event.keyCode === 13 && count == 1) {
        event.preventDefault();
        count = 0;
        // click the submit-button
        document.getElementById("goBtn").click();
      }

    });

    // add listener for the click-event
    input.addEventListener("click", function(event) {
      if (count == 0) {
        count += 1;
      }
    });

});
