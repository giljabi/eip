function ajaxRequest({url, method = 'GET',
                         data = null,
                         isFormData = false,
                         async = false,
                         successCallback,
                         errorCallback }) {
    $.ajax({
        url: url,
        type: method,
        data: data,
        async: async,
        processData: !isFormData, // FormData일 경우 false, 일반 객체일 경우 true
        contentType: isFormData ? false : 'application/json', // FormData일 경우 false, JSON인 경우 기본값
        enctype: isFormData ? 'multipart/form-data' : undefined,
        success: successCallback,
        error: function(error) {
            alert('오류가 발생했습니다.');
            if (errorCallback) errorCallback(error);
        }
    });
}

/**
 * 종목에 포함된 과목을 조회
 * @param qid
 */
function getQNameByQuid(qid) {
    ajaxRequest({
        url: '/register/quiz/qname/' + qid,
        successCallback: function(response) {
            if(response.code != 200) {
                alert(response.message);
                return;
            }
            $('#subjectId').empty();
            $.each(response.data, function(index, item) {
                $('#subjectId').append(new Option(item.name, item.id));
            });
        },
        errorCallback: function(error) {
            alert('데이터가 없습니다.');
        }
    });
}