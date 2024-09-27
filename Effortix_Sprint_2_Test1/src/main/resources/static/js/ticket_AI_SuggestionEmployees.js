/*<script>
    document.getElementById('askAISuggestions').addEventListener('click', function() {
        // Get the ticket description
        var description = document.getElementById('tDescription').value;
        
        // Make an AJAX request to the backend to get AI-suggested employees
        fetch('/tickets/getAiSuggestedEmployees', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ description: description })
        })
        .then(response => response.json())
        .then(data => {
            // Clear the dropdown
            var aiSuggestedDropdown = document.getElementById('aiSuggestedEmployees');
            aiSuggestedDropdown.innerHTML = '<option value="" disabled selected>Select an employee</option>';
            
            // Populate the dropdown with AI-suggested employees
            data.forEach(function(employee) {
                var option = document.createElement('option');
                option.value = employee.eId;
                option.text = employee.eName;
                aiSuggestedDropdown.appendChild(option);
            });
        })
        .catch(error => console.error('Error:', error));
    });
</script>
*/