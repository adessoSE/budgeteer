var shown = false;
$(document).ready(function(){
    var table;
    if($.fn.dataTable.isDataTable( '.table' )) {
        table = $(".table").DataTable();
    }
    else {
        table = $(".table").DataTable();
    }
    var column = table.column('.to-toggle');
    column.visible(false);

    $('.toggle-tax-info').on('click', function(e){
        var text;
        e.preventDefault();
        var column = table.column('.to-toggle');
        column.visible(!column.visible());
        if(!shown){
            text = "Hide ";
        }
        else{
            text = "Show ";
        }
        shown = !shown;
        $('.toggle-tax-info h3 a').text(text + "Tax Information");
        $('.toggle-tax-info .small-box-footer').text(text + "gross sum, tax amount and tax rate of the invoices");
    });
});