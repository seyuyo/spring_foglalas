$(document).ready(function () {

    const popInsContainer = $('#pop-insurance-container');
    const popInsTable = $('#pop-insurance-table');

    const tableContainer = $('#table-container');
    const biztTable = $('#bizt-table');

    const biztVallButton = $('#bizt-vallalatok-button');
    const popInstButton = $('#pop-insurance-button');

    popInsContainer.hide();
    popInsTable.hide();
    tableContainer.hide();
    biztTable.hide();

    biztVallButton.click(function () {
        popInsContainer.hide();
        popInsTable.hide();
        tableContainer.show();
        biztTable.show();

    });

    popInstButton.click(function () {
        tableContainer.hide();
        biztTable.hide();
        popInsContainer.show();
        popInsTable.show();
    });


});
