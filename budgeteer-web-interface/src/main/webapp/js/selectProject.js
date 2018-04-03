window.onload = (function () {

    // Get the input field
    var input = document.getElementById("proCh");
    var count = 0;

    if(input.options[input.selectedIndex].value != "") {
        count = 1;
    }

    document.addEventListener("keydown", function(evt) {
        evt = evt || window.event;
        var isEscape = false;
        if ("key" in evt) {
            isEscape = (evt.key == "Escape" || evt.key == "Esc");
        } else {
            isEscape = (evt.keyCode == 27);
        }
        if (isEscape) {
            evt.preventDefault();
            document.getElementById("logout").click();
        }
    });

    // Execute a function when the user presses enter on the keyboard
    input.addEventListener("keypress", function(event) {
      // Number 13 is the "Enter" key on the keyboard
      if (event.keyCode === 13 && count != 1) {
        count = 1;
        console.log("count is" + count);
        // Trigger the button element with a click
        document.getElementById("proCh").click();
      }
      else if(event.keyCode === 13 && count == 1) {
        event.preventDefault();
        count = 0;
        document.getElementById("goBtn").click();
      }

    });

    input.addEventListener("click", function(event) {
      if (count == 0) {
        count += 1;
      }


    });

});