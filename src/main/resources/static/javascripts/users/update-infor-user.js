// Function to show email exists alert with custom message
function ShowMessageErrr(title, message) {
    $('#Title').text(title);
    $('#Message').text(message);
    $('#AlerMessage').removeClass('hidden');
    // Hide the alert after 10 seconds
    setTimeout(function() {
        $('#AlerMessage').addClass('hidden');
    }, 10000);
}
// Close the alert when clicking on the "x" button
$('#closeBtn').click(function() {
    $('#AlerMessage').addClass('hidden');
});


$(document).ready(function() {
    $('#registerForm').submit(function(event) {
        let isEmailUnique = true; // Biến để kiểm tra tính duy nhất của email
        let username = $('#username').val();
        $.ajax({
            url: "/users/check-username-unique",
            method: "GET",
            data: { username: username },
            success: function(data) {
                if (data !== "ok") {
                    ShowMessageErrr("Email already exists!", "Please choose another email.");
                    isEmailUnique = false;
                }
            },
            async: false
        });

        if (!isEmailUnique) {
            event.preventDefault();
        }
    });
});