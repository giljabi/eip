let qid;

//현재 콤보에서 선택하므로 qid는 필요없다
function selectSubject() {
    const subjectSelect = $('#subjectSelect').val();
    const url = '/questions/random/' + qid + '/' + subjectSelect;
    window.location.href = url;
}

function makeSubjectList(qid) {
    ajaxRequest({
        url: '/questions/random/' + qid,
        credentials: 'include',
        async: false,
        successCallback: function (response) {
            //reponse가 null이면 /로 보낸다
            if(response.length == 0) {
                alert('접근 절차가 잘못되었습니다. 첫화면부터 시작해주세요');
                window.location.href = '/';
            } else {
                $.each(response, function(index, item) {
                    $('#subjectSelect').append(new Option(item.name, item.id));
                });
            }
            //console.log(response);
        }
    });
}


$(document).ready(function () {
    $('#retryButton').hide();
    $('#submitButton').show();

    // /questions/random/1/0
    const pathArray = window.location.pathname.split('/');
    qid = pathArray[pathArray.length - 2];              // 1
    makeSubjectList(qid);
    // select 박스의 값 설정
    const subjectId = pathArray[pathArray.length - 1];  // 0
    $('#subjectSelect').val(subjectId);

    $('#viewResultsButton').click(function () {
        const url = '/questions/results/' + qid;
        const newWindow = window.open(url, '_blank', 'noopener,noreferrer');

/*        // 팝업 차단 방지를 위해 창이 제대로 열리지 않은 경우 처리
        if (!newWindow || newWindow.closed || typeof newWindow.closed == 'undefined') {
            alert('팝업이 차단되었습니다. 팝업 차단 설정을 해제해 주세요.');
        }*/
    });

    $('#retryButton').click(function () {
        $('#retryOverlay').fadeIn();

        setTimeout(function () {
            $('#retryOverlay').fadeOut();
            const subjectSelect = $('#subjectSelect').val();
            const url = '/questions/random/' + qid + '/' + subjectSelect;
            window.location.href = url;
        }, 1000);

    });

    $('#submitButton').click(function () {
        $('#adOverlay').fadeIn();

        setTimeout(function () {
            $('#adOverlay').fadeOut();
            submitAnswers();
        }, 1000);
    });

    function submitAnswers() {
        const subjectSelect = $('#subjectSelect').val();
        let allSelected = true;

        // 각 질문별로 하나의 선택지가 선택되었는지 확인
        $('input[name="question-id"]').each(function () {
            let questionId = $(this).val();
            let selectedChoice = $(`input[name='question_${questionId}']:checked`).val();

            if (!selectedChoice) {
                allSelected = false; // 하나라도 선택되지 않으면 false로 설정
            }
        });

        if (!allSelected) {
            alert('모든 질문에 답을 선택해야 합니다.');
            return; // 선택되지 않은 경우 제출 중단
        }

        let userAnswer = [];

        $('input[name="question-id"]').each(function () {
            let questionId = $(this).val();
            let selectedChoice = $(`input[name='question_${questionId}']:checked`).val();

            userAnswer.push({
                id: questionId,
                answer: selectedChoice,
                qid: qid
            });
        });

        let reqData = {};
        reqData.subject = subjectSelect;
        reqData.answers = userAnswer;
        //console.log(reqData); // 결과를 콘솔에 출력 (테스트용)

        // AJAX를 이용해 서버에 제출
        ajaxRequest({
            url: '/questions/submit-answers',
            method: 'POST',
            data: JSON.stringify(reqData),
            contentType: 'application/json',
            successCallback: function(response) {
                //reponse가 null이면 /로 보낸다
                if(response.length == 0) {
                    alert('접근 절차가 잘못되었습니다. 첫화면부터 시작해주세요');
                    window.location.href = '/';
                } else {
                    applyResults(response);
                }
                //console.log(response);
            }
        });
    }

    function applyResults(results) {
        results.forEach(result => {
            const rowId = `#choice-${result.questionId}-${result.correctAnswer}`;
            const rowElement = $(rowId);
            const correctPercentage = result.correctPercentage;

            const percentageBadge = $(`#percentage-${result.questionId}-${result.correctAnswer}`);
            percentageBadge.text(`(${correctPercentage}%)`);

            if (result.correct) {
                rowElement.addClass('correct');
            } else {
                rowElement.addClass('incorrect');
            }

            // 라디오 버튼 비활성화
            $(`input[name='question_${result.questionId}']`).prop('disabled', true);
        });

        $('#submitButton').hide();
        $('#retryButton').show();
    }

});






