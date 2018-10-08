var table;
var draggedRow;

$(document).ready(function(){
    table = $(".table").DataTable();
});

function getAllIndices(){
    var indices = [];
    table.rows().eq(0).each( function ( index ) {
        indices.push(index);
    } );
    return indices;
}

function getRowIndex(event){
    var source = event.target || event.srcElement;
    var thisRow = $(source).closest('tr');
    var thisIndex = table.row(thisRow).index();
    return table.row(thisRow).index();
}

function getCurrentIndex(event){
    var source = event.target || event.srcElement;
    var thisRow = $(source).closest('tr');
    return table.row(thisRow).index();
}

function down(event){
    var page = table.page();
    table.order([0, 'desc']);
    var thisIndex = getCurrentIndex(event);
    var allIndices = getAllIndices();
    var arrayIndex = allIndices.indexOf(thisIndex);
    var nextIndex = allIndices[arrayIndex+1];
    swapRows(thisIndex, nextIndex);
    table.page(page);
}

function up(event){
    var page = table.page();
    table.order([0, 'desc']);
    var thisIndex = getCurrentIndex(event);
    var allIndices = getAllIndices();
    var arrayIndex = allIndices.indexOf(thisIndex);
    var previousIndex = allIndices[arrayIndex-1];
    swapRows(thisIndex, previousIndex);
    table.page(page);
}

function swapRows(firstIndex, secondIndex) {
    var firstRowData = table.row(firstIndex).data();
    var secondRowData = table.row(secondIndex).data();

    if(typeof secondRowData !== 'undefined'){
        table.row(firstIndex).data(secondRowData).draw();
        table.row(secondIndex).data(firstRowData).draw();
    }
}

function dragStart(event){
    var source = event.target || event.srcElement;
    draggedRow = $(source).parents("tr");
}

function drop(event){
    event.stopPropagation();
    event.preventDefault();
    var currentIndex = getCurrentIndex(event);
    var oldIndex = table.row(draggedRow).index();
    swapRows(currentIndex, oldIndex);
}

function allowDrop(ev) {
    ev.preventDefault();
}


function saveSorting(){
    var rows = table.rows();
    var contractIDs = [];

    rows.iterator('row', function(context, index){
         var node = $(this.row(index).node());
         var contractID = $(node).find("input").get(0);
         contractIDs.push($(contractID).val());
    });

    postAJAX(contractIDs);
}

function postAJAX(tableData){
    try{
        var commandToSend = 'tableData ='+tableData;
        var wcall = Wicket.Ajax.post({
            u: callbackUrl + '&'+commandToSend
        });
    }
    catch(e){
    }
}

