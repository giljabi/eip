let checkChoiceImageFlag = false;
//let formData = new FormData(); // FormData 객체 생성

function checkChoiceImage(isValid) {
    //이미지는 전체가 없던가 있던가 둘 중 하나이어야 함
    let allFilesSelected = true;
    let anyFileSelected = false;

    // 각 파일의 유무를 확인하여 전체가 선택되었는지 또는 선택되지 않았는지 확인
    for (let i = 1; i <= 4; i++) {
        //formData.append(`choiceFile${i}`, null);    // 초기값 설정

        let fileInput = $(`#choiceFile${i}`)[0].files[0];

        // 파일이 선택되지 않은 경우
        if (!fileInput) {
            allFilesSelected = false;
        } else {
            anyFileSelected = true;
        }

        // 파일이 있는 경우 1MB 이하인지 확인
        if (fileInput) {
            //formData.append(`choiceFile${i}`, fileInput);

            let fileSize = fileInput.size / 1024 / 1024; // MB로 변환
            if (fileSize > 1) {
                alert('이미지 파일은 1MB 이하로 업로드해주세요.');
                isValid = false;
                break;
            }
        }
    }

/*    // 유효성 검사: 모든 파일이 있거나 없지 않으면 에러 처리
    if (anyFileSelected && !allFilesSelected) {
        alert('모든 이미지 파일을 선택하거나, 모두 선택하지 않아야 합니다.');
        isValid = false;
    }*/
    checkChoiceImageFlag = allFilesSelected;
    return isValid;
}

$(document).ready(function() {
    $('#choiceImageFlag').on('change', function() {
        let isDisabled = $(this).is(':checked'); // 체크 여부 확인

        // choiceName1 ~ choiceName4 필드 disable 상태 설정
        $('#choiceName1, #choiceName2, #choiceName3, #choiceName4').prop('disabled', isDisabled);
        $('#choiceFile1, #choiceFile2, #choiceFile3, #choiceFile4').prop('disabled', !isDisabled);
    });
    // 페이지 로드 시 초기 상태는 이미지가 없는 것으로 가정
    $('#choiceImageFlag').trigger('change');


    $('#qid').on('change', function() {
        let qid = $(this).val();
        getSubjectByQuid(qid);
    });


    $('#submitButton').on('click', function(event) {
        event.preventDefault(); // 기본 폼 제출 방지

        let formData = new FormData($('#questionForm')[0]);

        // 입력값 검증
        let isValid = true;

        // Date 선택 확인
        if ($('#examId').val() === "0") {
            alert('Date를 선택해주세요.');
            return;
        }

        // Certificate 선택 확인
        if ($('#qid').val() === "0") {
            alert('Certificate를 선택해주세요.');
            return;
        }

        // Subject 선택 확인
        if ($('#subjectId').val() === "0") {
            alert('Subject를 선택해주세요.');
            return;
        }

        // Question Number 확인
        if ($('#no').val() === "") {
            alert('Question Number를 입력해주세요.');
            return;
        }

        // Question 확인
        if ($('#name').val().trim() === "") {
            alert('Question을 입력해주세요.');
            return;
        }

        // Correct 선택 확인
        if ($('#selectCorrect').val() === "") {
            alert('Correct를 선택해주세요.');
            return;
        }

        // $('#questionImageFlag').is(':checked')는 체크박스가 체크되어 있으면 true, 체크되어 있지 않으면 false를 반환
        // questionImageFlag false이면 파일이 없어야 하고 파일이 있으면 questionImageFlag를 true로 설정한다.
        if ($('#questionImageFile').val() !== "") {
            let file = $('#questionImageFile')[0].files[0];
            let fileSize = file.size / 1024 / 1024; // MB로 변환
            if (fileSize > 1) {
                alert('이미지 파일은 1MB 이하로 업로드해주세요.');
                return;
            }
        }

        // 선택지 내용 확인, 이미지가 있는 경우
        if(!$('#choiceImageFlag').is(':checked')) {
            // 선택지 내용 확인, 파일 4개가 선택되어 있는지 확인
            for (let i = 1; i <= 4; i++) {
                if ($('#choiceName' + i).val().trim() === "") {
                    alert(i + '번 선택지 내용을 입력해주세요.');
                    isValid = false;
                    break;
                }
            }
        } else {
            for (let i = 1; i <= 4; i++) {
                if($('#question\\.imageUrl' + i).text().trim() !== "")
                    continue;
                if ($('#choiceFile' + i).val().trim() === "") {
                    alert(i + '번 선택지 그림을 입력해주세요.');
                    isValid = false;
                    break;
                }
            }
        }
        isValid = checkChoiceImage(isValid);

        if(!isValid) {
            alert('데이터 검증 실패');
            return;
        } else {
            // JSON 객체 생성
            let jsonData = {
                id: $('#id').val(),
                useFlag: $('#useFlag').is(':checked'),
                //questionImageFlag: $('#questionImageFlag').is(':checked'),
                choiceImageFlag: $('#choiceImageFlag').is(':checked'),
                examId: $('#examId').val(),
                qid: $('#qid').val(),
                subjectId: $('#subjectId').val(),
                no: $('#no').val(),
                name: $('#name').val().trim(),
                correct: $('#correct').val(),
                choices: [
                    $('#choiceName1').val().trim(),
                    $('#choiceName2').val().trim(),
                    $('#choiceName3').val().trim(),
                    $('#choiceName4').val().trim()
                ]
            };
            formData.append('jsonData', new Blob([JSON.stringify(jsonData)], { type: 'application/json' }));
            ajaxRequest({
                url: gContextPath + '/register/quiz',
                method: 'POST',
                data: formData,
                isFormData: true,
                successCallback: function(response) {
                    if(response.code == 200) {
                        alert('문제가 등록되었습니다.');
                        location.href = gContextPath + '/register/quiz'; // 다음문제를 연속으로 입력하기 위해 입력창으로 이동
                    } else {
                        alert(response.message);
                        return;
                    }
                }
            });
        }
    });

    $('#cancelButton').on('click', function(event) {
        this.window.close();
    });
});


