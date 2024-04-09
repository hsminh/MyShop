// Function to move focus to the next input
function moveToNextInput(currentInput, nextInput) {
    if (currentInput.value.length === currentInput.maxLength) {
        nextInput.focus();
    }
}
// Event listener to move focus to the next input
document.getElementById('digit1').addEventListener('input', function () {
    moveToNextInput(this, document.getElementById('digit2'));
});
document.getElementById('digit2').addEventListener('input', function () {
    moveToNextInput(this, document.getElementById('digit3'));
});
document.getElementById('digit3').addEventListener('input', function () {
    moveToNextInput(this, document.getElementById('digit4'));
});
document.getElementById('digit4').addEventListener('input', function () {
    moveToNextInput(this, document.getElementById('digit5'));
});
document.getElementById('digit5').addEventListener('input', function () {
    moveToNextInput(this, document.getElementById('digit6'));
});
function ShowMessageError(title, message) {
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
$('#submit').click(function (event) {
    event.preventDefault();
    let email = $('#email').val();
    let digit1 = parseInt($('#digit1').val());
    let digit2 = parseInt($('#digit2').val());
    let digit3 = parseInt($('#digit3').val());
    let digit4 = parseInt($('#digit4').val());
    let digit5 = parseInt($('#digit5').val());
    let digit6 = parseInt($('#digit6').val());
    let token = '' + digit1 + digit2 + digit3 + digit4 + digit5 + digit6;
    $.ajax({
        url: "/auth/verify-verification-code",
        method: "GET",
        data: {
            verificationCode:token,
            email : email
        },
        success: function (data) {
            if (data !== "duplicated") {
                window.location.href = "/auth/update-password?token=" + encodeURIComponent(data)+"&email="+email;
            } else {

                ShowMessageError("Verification Code!", "Incorrect Verification Code Or Time Out!");
            }
        },
        error: function () {
            ShowMessageError("Error!", "Something went wrong.");
        }
    });
});

