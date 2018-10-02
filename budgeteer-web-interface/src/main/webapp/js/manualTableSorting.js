$(document).ready(function(){
   $("th").click(function(event){
        var source = $(event.target);

        setRowIds();
   });
});

function dragStart(event)
{
    var source = event.target || event.srcElement;
    var row = $(source).parents("tr");
}

function dragLeave(event)
{
}

function drop()
{
}

function setRowIds()
{
       var allRows = $("table").find(".odd, .even");

       for(var i=0; i<allRows.length; i++)
       {
            $(allRows[i]).attr("id", i);
       }
}

function down(event)
{
    setRowIds();
    var source = event.target || event.srcElement;
    var currentRow = $(source).parents("tr");
    var currentRowId = $(currentRow).attr('id');


    var allRows = $("table").find(".odd, .even");

   for(var i=0; i<allRows.length; i++)
   {
        if($(allRows[i]).attr('id') == currentRowId))
        {

        }
   }
}

