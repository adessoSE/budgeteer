
document.addEventListener('keydown',
function(evt) {
    evt = evt || window.event;
     var isEnter = false;
     var isEscape = false;
     if ("key" in evt) {
         isEnter = (evt.key == "Enter");
         isEscape = (evt.key == "Escape" || evt.key == "Esc");
     } else {
         isEnter = (evt.keyCode == 13);
         isEscape = (evt.keyCode == 27);
     }
     if(isEnter) {
         evt.preventDefault();
         document.getElementById("submitButton").click();
     }
     else if(isEscape) {
         evt.preventDefault();
         document.getElementById("cancel").click();
     }
 });