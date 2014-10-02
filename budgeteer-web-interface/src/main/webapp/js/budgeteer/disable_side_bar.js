// disables the AdminLTE side bar by default
if ($(window).width() <= 992) {
    $('.row-offcanvas').toggleClass('active');
    $('.left-side').removeClass("collapse-left");
    $(".right-side").removeClass("strech");
    $('.row-offcanvas').toggleClass("relative");
} else {
    //Else, enable content streching
    $('.left-side').toggleClass("collapse-left");
    $(".right-side").toggleClass("strech");
}