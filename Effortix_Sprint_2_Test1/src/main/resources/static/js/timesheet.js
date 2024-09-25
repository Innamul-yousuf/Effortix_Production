$(document).ready(function() {
    $('#timesheetEntriesContainer').hide(); // Hide initially

    $('#timesheetForm').on('submit', function(event) {
        event.preventDefault(); // Prevent form submission

        $.ajax({
            url: '/api/timesheet/employee/' + $('#employeeId').val() + '/dates',
            type: 'GET',
            data: {
                fromDate: $('#fromDate').val(),
                toDate: $('#toDate').val()
            },
            success: function(data) {
                // Clear existing entries
                $('#timesheetEntriesTable').empty();

                // Check if data is not empty
                if (data.length > 0) {
                    // Populate the table with the new entries
                    data.forEach(function(entry) {
                        $('#timesheetEntriesTable').append(
                            '<tr>' +
                                '<td>' + entry.date + '</td>' +
                                '<td>' + entry.etActivity + '</td>' +
                            '</tr>'
                        );
                    });
                    // Show the entries container
                    $('#timesheetEntriesContainer').show();
                } else {
                    // If no entries, hide the container or show a message
                    $('#timesheetEntriesContainer').hide();
                    alert('No timesheet entries found for the selected dates.');
                }
            },
            error: function() {
                alert('Error fetching timesheet entries.');
            }
        });
    });
});
