$(document).ready(function () {

    function ShowMessageErrr(title, message) {
        $('#Title').text(title);
        $('#Message').text(message);
        $('#AlertMessage').removeClass('hidden');
        // Hide the alert after 10 seconds
        setTimeout(function () {
            $('#AlertMessage').addClass('hidden');
        }, 10000);
    }

    // Close the alert when clicking on the "x" button
    $('#closeBtn').click(function () {
        $('#AlertMessage').addClass('hidden');
    });

    $('#image').on('change', function (e) {
        if (event.target.files.length > 0) {
            var src = URL.createObjectURL(event.target.files[0]);
            var thumbnail = document.getElementById("thumbnail");
            thumbnail.src = src;
            thumbnail.style.display = "block";
        }
    });



    $('#submitForm').on('submit', function (e) {
        let name = $('#name').val().trim();
        let sku = $('#sku').val().trim();
        let id = $('#id').val().trim();
        let check = false;

        $.ajax({
            url: "/products/check-sku-name-unique",
            method: "GET",
            data: {
                id:id,
                name:name,
                sku:sku
            },
            success: function(data) {
                alert((data))
                if (data !== "ok") {
                    ShowMessageErrr("Name or Sku!", "Exist Please choose another Name Or SKU.");
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