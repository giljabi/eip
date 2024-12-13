
const EIPclientUUID = 'EIPclientUUID';
/**
 *
 * @param url
 * @param method
 * @param data
 * @param isFormData
 * @param async
 * @param successCallback
 * @param errorCallback
 */
function ajaxRequest({url, method = 'GET',
                         data = null,
                         isFormData = false,
                         async = false,
                         successCallback,
                         errorCallback }) {
    const fullUrl = `${window.location.protocol}//${window.location.host}${url}`;
    $.ajax({
        url: fullUrl,
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
function getSubjectByQuid(qid) {
    ajaxRequest({
        url: '/register/quiz/subject/' + qid,
        successCallback: function(response) {
            if(response.code != 200) {
                alert(response.message);
                return;
            }
            $('#subjectId').empty();    //시험과목은 선택필수로 지정
            $.each(response.data, function(index, item) {
                $('#subjectId').append(new Option(item.name, item.id));
            });
        },
        errorCallback: function(error) {
            alert('데이터가 없습니다.');
        }
    });
}

/**
 * 시험 데이터가 있는 날짜 조회
 * @param qid
 */
function getExamDay(qid) {
    ajaxRequest({
        url: '/register/quiz/examnoday/' + qid,
        successCallback: function(response) {
            if(response.code != 200) {
                alert(response.message);
                return;
            }
            $('#examId').empty();
            $('#examId').append(new Option('선택', 0));
            $.each(response.data, function(index, item) {
                $('#examId').append(new Option(item.examday, item.id));
            });
        },
        errorCallback: function(error) {
            alert('데이터가 없습니다.');
        }
    });
}


// UUID 생성 함수
function generateUUID() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        const r = (Math.random() * 16) | 0;
        const v = c === 'x' ? r : (r & 0x3) | 0x8;
        return v.toString(16);
    });
}

// UUID 저장
function getUUID() {
    let uuid = localStorage.getItem(EIPclientUUID); // 기존 UUID 조회
    if (!uuid) {
        localStorage.setItem(EIPclientUUID, generateUUID()); // UUID 저장
    }
    return uuid;
}




