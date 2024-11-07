
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
        getQNameByQuid(qid);
    });

///quiz-list/{qid}/{examNo}/{subjectId}
    $('#inquiry').on('click', function(event) {
        let qid = $('#qid').val();
        let examId = $('#examId').val();
        let subjectId = $('#subjectId').val();
        let search = $('#search').val();
        if (search && search.length < 2) {
            alert("검색어는 2자 이상 입력해 주세요.");
            return;
        }
        let url = '/register/quiz-list/' + qid + '/' + subjectId + '?examNo=' + examId + '&name=' + encodeURIComponent(search);

        // 세 개 모두 선택되어야 하는 조건 확인 //examId && examId !== "0" &&
        if (qid && qid !== "0" && subjectId && subjectId !== "0") {
            ajaxRequest({
                url: url,
                successCallback: function (response) {
                    if (response.code != 200) {
                        alert(response.message);
                        return;
                    }
                    $('#questionContainer').empty();
                    let answerCheck = $('#answerCheck').is(':checked');

                    $.each(response.data, function(index, item) {
                        // 각 데이터를 테이블의 새로운 행으로 추가
                        $('#questionContainer').append(`
                            <tr class="question-row" data-id="${item.id}">
                                <td class="text-end d-none d-md-table-cell">${item.id}</td>
                                <td class="text-left d-none d-md-table-cell">${item.subjectName}</td>
                                <td class="text-end d-none d-md-table-cell">${item.no}</td>
                                <td>${item.name}<br>
                                    ${item.questionImageFlag ? `<img src="${item.imageUrl}" width="300" />` : ''}
                                </td>
                                <td class="text-center">${answerCheck ? item.correct : ''}</td>
                                <td>
                                    <ul>
                                        ${item.choices.map(choice => `
                                                <li>
                                                    ${choice.name}<br>
                                                    ${item.choiceImageFlag ? `<img src="${choice.imageUrl}" width="100" />` : ''}
                                                </li>
                                            `).join('')}
                                    </ul>
                                </td>
                            </tr>
                        `);
                    });
                }
            });
        } else {
           alert("검색을 제외한 모든 조건을 선택해 주세요.");
           return;
        }
    });

// 이벤트 위임 방식으로 클릭 이벤트를 설정
    $('#questionContainer').on('click', '.question-row', function() {
        const id = $(this).data('id');
        window.open(`/register/quiz/${id}`, 'QuizPopup', 'width=600,height=400,resizable=yes,scrollbars=yes');
    });
});
