// 범용 js 만들기

var commonLib = commonLib || {}; // 있으면 기존 것, 없으면 새로 생성

/**
1. 파일 업로드
*/


commonLib.fileManager = {
// 1. 파일 업로드 처리
    upload(files) {
        try {
            if(!files || files.length == 0) {
                throw new Error("업로드할 파일을 선택하세요.");
            }

            // gid
            const gidEl = document.querySelector("[name='gid']");
            if (!gidEl || !gidEl.value.trim()) {
                throw new Error("gid가 누락되었습니다.");
            }

            const gid = gidEl.value.trim();
            // 자바스크립트로 새로운 폼 만들기
            // https://developer.mozilla.org/ko/docs/Web/API/FormData/append

            const formData = new FormData(); // 기본 Content-Type : multipart/form-data

            formData.append("gid", gid);
            for(const file of files) {
                formData.append("file", file);
            }

            const { ajaxLoad } = commonLib;
            ajaxLoad("POST", "/api/file", formData, "json")
                .then((res) => { // 요청 성공 시 넘어가는 데이터
                    if(res && res.success){ // 파일 업로드 성공 시
                        if(typeof parent.callbackFileUpload == 'function') { // callback : 범용 기능
                            parent.callbackFileUpload(res.data);
                        }
                    } else { // 파일 업로드 실패
                        if(res) alert(res.message);

                    }
                })
                .catch(err => console.error(err));




        } catch (err) {
            alert(err.message);
            console.error(err);
        }
    }


};

//이벤트 처리
window.addEventListener("DOMContentLoaded", function() {
    const uploadFiles = document.getElementsByClassName("upload_files");
    // 파일 태그 동적으로 :  클릭 시 파일탐색기 생성되도록
    const fileEl = document.createElement("input");
    fileEl.type="file";
    fileEl.multiple = true; // 여러 파일 선택 가능

// 파일 업로드 버튼 클릭 -> 파일 탐색기 열기
    for (const el of uploadFiles) {
        el.addEventListener("click", function (){
            fileEl.click();

        });
    }

    // 파일 선택 시 이벤트 처리
    fileEl.addEventListener("change", function(e){
        // console.dir(e.target.files); // 콘솔 files에 업로드한 파일에 대한 정보 출력
        commonLib.fileManager.upload(e.target.files);
    });

});