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



        $(document).ready(function() {
            // Handle form submission
            $("#timesheetForm").submit(function(event) {
                event.preventDefault();
                fetchTimesheetEntries();
            });

            // Function to fetch and display timesheet entries
            function fetchTimesheetEntries() {
                const employeeId = $("#employeeId").val();
                const fromDate = $("#fromDate").val();
                const toDate = $("#toDate").val();

                $.ajax({
                    url: `/api/timesheet/employee/${employeeId}/dates`,
                    method: 'GET',
                    data: {
                        fromDate: fromDate,
                        toDate: toDate
                    },
                    success: function(data) {
                        populateTable(data);
                    },
                    error: function(err) {
                        alert('Error fetching data');
                        console.log(err);
                    }
                });
            }

            // Function to populate the timesheet entries in the table
            function populateTable(entries) {
                const tableBody = $("#timesheetEntriesTable");
                tableBody.empty();
                entries.forEach(entry => {
                    const row = `<tr>
                                    <td>${entry.date}</td>
                                    <td>${entry.etActivity}</td>
                                 </tr>`;
                    tableBody.append(row);
                });
            }

            // Handle the "Download Excel" button click
            $("#downloadExcel").click(function() {
                const employeeId = $("#employeeId").val();
                const fromDate = $("#fromDate").val();
                const toDate = $("#toDate").val();

                if (!employeeId || !fromDate || !toDate) {
                    alert("Please select an employee and date range first.");
                    return;
                }

                // Fetch the data and convert it to Excel
                $.ajax({
                    url: `/api/timesheet/employee/${employeeId}/dates`,
                    method: 'GET',
                    data: {
                        fromDate: fromDate,
                        toDate: toDate
                    },
                    success: function(data) {
                        exportToExcel(data);
                    },
                    error: function(err) {
                        alert('Error fetching data for export');
                        console.log(err);
                    }
                });
            });

            // Function to export data to Excel
            function exportToExcel(data) {
                const worksheet = XLSX.utils.json_to_sheet(data.map(entry => ({
                    Date: entry.date,
                    etActivity: entry.etActivity
                })));

                const workbook = XLSX.utils.book_new();
                XLSX.utils.book_append_sheet(workbook, worksheet, "Timesheet");

                XLSX.writeFile(workbook, `Timesheet_${new Date().toISOString().slice(0, 10)}.xlsx`);
            }
        });
    