var shown = false;

$(document).ready(function(){
    $('.toggle-tax-info-boxes').on('click', function(e){
        var text;
        if(!shown){
            $(".to-toggle-box").css({"display":"block"});
            $('.toggle-tax-info-boxes h3 a').text("");
            text = "Hide "
        }
        else{
            $(".to-toggle-box").css({"display":"none"});
            text = "Show "
        }
        shown = !shown;
        $('.toggle-tax-info-boxes h3 a').text(text + "Tax Information");
        $('.toggle-tax-info-boxes .small-box-footer').text(text + "gross sum, tax amount and tax rate of the invoice");
    });
});