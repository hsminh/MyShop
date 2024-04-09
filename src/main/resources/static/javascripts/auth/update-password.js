function ShowMessageError(title, message) {
    $('#Title').text(title);
    $('#Message').text(message);
    $('#AlerMessage').removeClass('hidden');
    // Hide the alert after 10 seconds
    setTimeout(function() {
        $('#AlerMessage').addClass('hidden');
    }, 10000);
}

$('#submitButton').submit(function (event) {
    event.preventDefault();
    let password = $('#password').val().trim();
    let confirmPassword = $('#confirmPassword').val().trim();

    if (password !== confirmPassword) {
        ShowMessageError("Error!", "Passwords do not match.");
        return;
    }
    if (password.length < 6) {
        ShowMessageError("Error!", "Password must be at least 6 characters");
        return;
    }
    this.submit();
});