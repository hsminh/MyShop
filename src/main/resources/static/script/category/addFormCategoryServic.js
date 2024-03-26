$(document).ready(function () {

    function ShowMessageErrr(title, message) {
        $('#Title').text(title);
        $('#Message').text(message);
        $('#AlertMessage').removeClass('hidden');
        // Hide the alert after 10 seconds
        setTimeout(function() {
            $('#AlertMessage').addClass('hidden');
        }, 10000);
    }
    // Close the alert when clicking on the "x" button
    $('#closeBtn').click(function() {
        $('#AlertMessage').addClass('hidden');
    });

    $('#submitForm').on('submit', function (e) {
        let des = $('#des').val().trim();
        let name = $('#name').val().trim();
        let slug = $('#slug').val().trim();
        let id = $('#id').val().trim();
        let check = false;

        if (des.length < 1 || des.length > 255) {
            ShowMessageErrr("Description:", "Description must be between 1 and 255 characters.");
            check = true;
        } else if (name.length === 0) {
            ShowMessageErrr("Name:", "Name is required and cannot be empty.");
            check = true;
        } else if (slug.length === 0) {
            ShowMessageErrr("Slug:", "Slug is required and cannot be empty.");
            check = true;
        }
        $.ajax({
            url: "/category/check-slug-name-uni",
            method: "GET",
            data: {
                id:id,
                name:name,
                slug:slug
            },
            success: function(data) {
                if (data !== "ok") {
                    ShowMessageErrr("Name or Slug!", "Exist Please choose another Name Or Slug.");
                    check = true;
                }
            },
            async: false
        });

        if (check) {
            e.preventDefault();
        }
    });
});

