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
        e.preventDefault();
        var column = table.column('.to-toggle');
        column.visible(!column.visible());
    })
});