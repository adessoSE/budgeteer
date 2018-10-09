var table;
var draggedRow;

$(document).ready(function(){
    table = $(".table").DataTable();
});

// Get all row indices of the table
function getAllIndices(){
    var indices = [];
    table.rows().eq(0).each( function ( index ) {
        indices.push(index);
    } );
    return indices;
}

// Get the row index of the element which triggered the event
function getRowIndex(event){
    var source = event.target || event.srcElement;
    var thisRow = $(source).closest('tr');
    return table.row(thisRow).index();
}

// Event handler for the down button: Swap row with next row
function down(event){
    var page = table.page();
    // Order by the first column, else row swapping doesn't work
    table.order([0, 'desc']);
    var thisIndex = getRowIndex(event);
    var allIndices = getAllIndices();
    var arrayIndex = allIndices.indexOf(thisIndex);
    var nextIndex = allIndices[arrayIndex+1];
    swapRows(thisIndex, nextIndex);
    table.page(page);
}

// Event handler for the up button: Swap row with previous row
function up(event){
    var page = table.page();
    // Order by the first column, else row swapping doesn't work
    table.order([0, 'desc']);
    var thisIndex = getRowIndex(event);
    var allIndices = getAllIndices();
    var arrayIndex = allIndices.indexOf(thisIndex);
    var previousIndex = allIndices[arrayIndex-1];
    swapRows(thisIndex, previousIndex);
    table.page(page);
}

// Swap the rows of the two indices
function swapRows(firstIndex, secondIndex) {
    var firstRowData = table.row(firstIndex).data();
    var secondRowData = table.row(secondIndex).data();

    if(typeof secondRowData !== 'undefined'){
        table.row(firstIndex).data(secondRowData).draw();
        table.row(secondIndex).data(firstRowData).draw();
    }
}

// Event handler for dragging: save the dragged row
function dragStart(event){
    var source = event.target || event.srcElement;
    draggedRow = $(source).parents("tr");
}

// Event handler for dropping: Swap rows
function drop(event){
    event.stopPropagation();
    event.preventDefault();
    var currentIndex = getRowIndex(event);
    var oldIndex = table.row(draggedRow).index();
    swapRows(currentIndex, oldIndex);
}

function allowDrop(ev) {
    ev.preventDefault();
}

// Get the sorting and send it via AJAX
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

