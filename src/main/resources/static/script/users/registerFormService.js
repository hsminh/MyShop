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
        let checkInforInForm = true; // Biến để kiểm tra tính duy nhất của email
        let username = $('#username').val();

        let firstName=$('#firstName').val();
        let lastName=$('#lastName').val();
        if(firstName.length===0||lastName.length===0)
        {
            checkInforInForm=false;
            ShowMessageErrr("Notification!", "Please fill in all required information");
        }
        if(checkInforInForm)
        {
            $.ajax({
                url: "/users/check-username-unique",
                method: "GET",
                data: { username: username },
                success: function(data) {
                    if (data !== "ok") {
                        ShowMessageErrr("Email already exists!", "Please choose another email.");
                        checkInforInForm = false;
                    }
                },
                async: false
            });


        }
        if (!checkInforInForm) {
            event.preventDefault();
        }
    });



    $('#registerForm').submit(function(event) {
        let id=$('#id').val();
        if(id==='0'||id==='')
        {
            let password = $('#password').val();
            let repassword = $('#repassword').val();
            if (password !== repassword) {
                ShowMessageErrr("Password not Match!", "Please make sure your passwords match.");
                event.preventDefault();
            }
        }
    });
});