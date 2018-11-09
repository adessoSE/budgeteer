var table;
var draggedRow;
var handleBtn;
var target = false;

$(document).ready(function(){
    table = $(".table").DataTable();
    // Remove arrows in the header cell of the sorting buttons
    $("#btnsHeadCell").removeClass('sorting_asc');

    $("tr").click(function(){
        // Remove arrows in the header cell of the sorting buttons
        $("#btnsHeadCell").removeClass('sorting');
        saveSorting();
    });
});

function mouseDownRow(event){
    target = event.target;
    handleBtn = $(event).closest('.dragBtn');
}

function drag(event){
    if($(target).hasClass('dragBtn') || $(target).hasClass('fa-hand-o-up'))    {
        event.dataTransfer.setData('text/plain', 'handleBtn');
        // Order by the first column, else row swapping doesn't work
        table.order([0, 'asc']);
        draggedRow = $(event.target);
    }
    else{
        event.preventDefault();
    }
}

// Event handler for dropping: Swap rows
function drop(event){
    event.stopPropagation();
    event.preventDefault();
    var currentIndex = getRowIndex(event);
    var oldIndex = table.row(draggedRow).index();
    swapRows(currentIndex, oldIndex);
    saveSorting();
}

function allowDrop(ev) {
    ev.preventDefault();
}

function hover(event, show){
    var source = event.target || event.srcElement;

    if(show){
        $(source).tooltip('show');
    }
    else{
        $(source).tooltip('hide');
    }
}

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
    table.order([0, 'asc']);
    var thisIndex = getRowIndex(event);
    var allIndices = getAllIndices();
    var arrayIndex = allIndices.indexOf(thisIndex);
    var nextIndex = allIndices[arrayIndex+1];
    swapRows(thisIndex, nextIndex);
    table.page(page).draw(false );
    saveSorting();
}

// Event handler for the up button: Swap row with previous row
function up(event){
    var page = table.page();
    // Order by the first column, else row swapping doesn't work
    table.order([0, 'asc']);
    var thisIndex = getRowIndex(event);
    var allIndices = getAllIndices();
    var arrayIndex = allIndices.indexOf(thisIndex);
    var previousIndex = allIndices[arrayIndex-1];
    swapRows(thisIndex, previousIndex);
    table.page(page).draw( false );
    saveSorting();
}

// Swap the rows of the two indices
function swapRows(firstIndex, secondIndex) {
    var firstRowData = table.row(firstIndex).data();
    var secondRowData = table.row(secondIndex).data();

    if(typeof secondRowData !== 'undefined'){
        table.row(firstIndex).data(secondRowData).draw(false);
        table.row(secondIndex).data(firstRowData).draw(false);
    }
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

    // Remove arrows in the header cell of the sorting buttons
    $("#btnsHeadCell").removeClass('sorting_asc sorting');
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

