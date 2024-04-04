    $(document).ready(function () {
        // Lấy giá trị URL hình ảnh từ thẻ img có id là "thumbnail2"
        var imageURL = document.getElementById("thumbnail2").getAttribute("src");

        // Kiểm tra xem trường input có id là "checkError" có tồn tại không
        var checkError = document.getElementById("checkError");
        if (checkError) {
            // Nếu tồn tại, thực hiện các hành động cần thiết
            alert(imageURL);
            $('#thumbnail2').removeClass('hidden');
            $('#thumbnail').addClass('hidden');
            document.getElementById("image").value(imageURL);
        }

        function ShowMessageErrr(title, message) {
            $('#Title').text(title);
            $('#Message').text(message);
            $('#AlertMessage').removeClass('hidden');
            // Hide the alert after 10 seconds
            setTimeout(function () {
                $('#AlertMessage').addClass('hidden');
            }, 10000);
        }

        $('#closeBtn').click(function () {
            $('#AlertMessage').addClass('hidden');
        });

        $('#image').on('change', function (e) {
            if (event.target.files.length > 0) {
                var src = URL.createObjectURL(event.target.files[0]);
                var thumbnail = document.getElementById("thumbnail");
                var thumbnail2 = document.getElementById("thumbnail2");
                var imageFileHidden=document.getElementById("imageFileHidden");
                imageFileHidden.value="";
                thumbnail.src = src;
                thumbnail.style.display = "block";
                if ($(thumbnail).hasClass('hidden')) {
                    $(thumbnail).removeClass('hidden');
                    $(thumbnail2).addClass('hidden');
                }
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