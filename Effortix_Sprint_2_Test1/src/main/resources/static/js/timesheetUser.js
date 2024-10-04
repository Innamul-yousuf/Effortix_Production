$(document).ready(function() {
    $('#timesheetEntriesContainer').hide(); // Hide initially

    $('#timesheetForm').on('submit', function(event) {
        event.preventDefault(); // Prevent form submission

        const employeeId = $('#employeeId').val();
        const fromDate = $('#fromDate').val();
        const toDate = $('#toDate').val();

        $.ajax({
            url: `/api/timesheet/employee/${employeeId}/dates`,
            type: 'GET',
            data: {
                fromDate: fromDate,
                toDate: toDate
            },
            success: function(data) {
                $('#timesheetEntriesTable').empty();

                if (data.length > 0) {
                    data.forEach(function(entry) {
                        $('#timesheetEntriesTable').append(
                            '<tr>' +
                                '<td>' + entry.date + '</td>' +
                                '<td>' + entry.etActivity + '</td>' +
                            '</tr>'
                        );
                    });
                    $('#timesheetEntriesContainer').show();
                } else {
                    $('#timesheetEntriesContainer').hide();
                    alert('No timesheet entries found for the selected dates.');
                }
            },
            error: function() {
                alert('Error fetching timesheet entries.');
            }
        });
    });

    $('#downloadExcel').click(function() {
        const employeeId = $('#employeeId').val();
        const fromDate = $('#fromDate').val();
        const toDate = $('#toDate').val();

        if (!fromDate || !toDate) {
            alert('Please select a date range first.');
            return;
        }

        $.ajax({
            url: `/api/timesheet/employee/${employeeId}/dates`,
            type: 'GET',
            data: {
                fromDate: fromDate,
                toDate: toDate
            },
            success: function(data) {
                exportToExcel(data);
            },
            error: function() {
                alert('Error fetching data for export.');
            }
        });
    });

    function exportToExcel(data) {
        const worksheet = XLSX.utils.json_to_sheet(data.map(entry => ({
            Date: entry.date,
            Activity: entry.etActivity
        })));

        const workbook = XLSX.utils.book_new();
        XLSX.utils.book_append_sheet(workbook, worksheet, "Timesheet");

        XLSX.writeFile(workbook, `Timesheet_${new Date().toISOString().slice(0, 10)}.xlsx`);
    }
});
