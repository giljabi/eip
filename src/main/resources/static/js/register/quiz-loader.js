let selectedFile = null;

$(document).ready(function () {
    $('#writeButton').click(function () {
        const formData = new FormData();
        if (selectedFile)
            formData.append('formData', selectedFile);
        else {
            alert('파일을 선택해주세요.');
            return;
        }

        $('#adOverlay').fadeIn();
        ajaxRequest({
            url: '/register/quizloader/save',
            data: formData,
            isFormData: true,
            async: true,
            method: 'POST',
            successCallback: function(response) {
                if(response.code == 200) {
                    $('#message').text('파일이 정상적으로 등록되었습니다.');
                }
                $('#adOverlay').fadeOut();
            }
        });
    });

    $('#fileOpenButton').click(function () {
        $('#fileInput').click();
    });

    $('#fileOpenButton').click(function () {
        $('#fileInput').on('change', function(event) {
            selectedFile = event.target.files[0];
            const formData = new FormData();
            if (selectedFile)
                formData.append('formData', selectedFile);
            else {
                alert('파일을 선택해주세요.');
                return;
            }

            $('#adOverlay').fadeIn();
            ajaxRequest({
                url: '/register/quizloader',
                data: formData,
                isFormData: true,
                method: 'POST',
                async: true,
                successCallback: function (response) {
                    if (response.code == 200) {
                        const tbody = $('#questionsTableBody');
                        tbody.empty(); // 기존 테이블 내용 초기화

                        response.data.forEach(function(result) {
                            const qifText = result.qif ? 'true' : '';
                            const cifText = result.cif ? 'true' : '';
                            const choicesList = result.choices.map(choice => `<li>${choice}</li>`).join('');
                            const rowHtml = `
                                <tr>
                                    <td>${result.question}</td>
                                    <td>${qifText}</td>
                                    <td><ul>${choicesList}</ul></td>
                                    <td>${cifText}</td>
                                    <td>${result.answer}</td>
                                </tr>
                            `;

                            tbody.append(rowHtml);
                        });

                        $('#adOverlay').fadeOut();
                        return;
                    }
                }
            });
        });
    });

});



