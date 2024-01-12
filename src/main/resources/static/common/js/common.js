var commonLib = commonLib || {};

/**
ajax 요청, 응답 편의 함수
@param method : 요청 방식 - GET, POST, PUT, PATCH, DELETE ...
@param url : 요청 URL
@param params : 요청 데이터(POST, PUT, PATCH ...)
@param responseType : json - javascript 객체로 변환, 아니면 text로 변환
*/

commonLib.ajaxLoad = function(method, url, params, responseType) {
    method = method || "GET"; // 없으면 GET으로
    params = params || null; // 없으면 null(undefined)

    const token = document.querySelector("meta[name='_csrf']").content;
    const tokenHeader = document.querySelector("meta[name='_csrf_header']").content;

    return new Promise((resolve, reject) => { // ??
        const xhr = new XMLHttpRequest();

        xhr.open(method, url);
        xhr.setRequestHeader(tokenHeader, token);
        xhr.send(params); // 요청 body에 실릴 데이터 키=값&키=값 ... FormData 객체(주로 POST, PUT, PATCH 일 때)

        xhr.onreadystatechange = function() { // ?
            if (xhr.status == 200 && xhr.readyState == XMLHttpRequest.DONE) { // 성공
                // responseType이 json인지 체크
                const resData = (responseType && responseType.toLowerCase() == 'json')?
                    JSON.parse(xhr.responseText) : xhr.responseText;
                resolve(resData); // 성공 시 응답 에이터
            }
        };

        xhr.onabort = function(err) { // 실패 시?
            reject(err); // 중단 시
        };
        xhr.onerror = function(err) { // ?
            reject(err); // 요청 또는 응답 시 오류 발생
        };

    });

};

/**
이지윅 에디터 로드
*/
commonLib.loadEditor = function(id, height) {
    if (!id) {
        return;
    }
    height = height || 450;

    // ClassicEditor  ??
    return ClassicEditor.create(document.getElementById(id), {
        height
    });
}
