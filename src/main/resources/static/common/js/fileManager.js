// 범용 js 만들기

var commonLib = commonLib || {}; // 있으면 기존 것, 없으면 새로 생성

/**
1. 파일 업로드
*/


commonLib.fileManager = {
/**
* 1. 파일 업로드 처리
* @param files : 업로드 파일 정보 목록
* @param location : 파일 그룹(gid) 안에서 위치 구분 값 (ex) 메인이미지, 목록이미지, 상세페이지 등..
* @param imageOnly : 이미지만 업로드 가능하게 통제
* @param singleFile : true - 단일 파일 업로드
*/
    upload(files, location, imageOnly, singleFile) {
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

            // 파일 그룹(gid) 안에서 위치 구분 값
            if(location) {
                formData.append("location", location);
            }

            // 단일 파일 업로드
            if(singleFile) {
                formData.append("singleFile", singleFile)
            }

            // 이미지만 업로드 가능일 때 처리하기 S
            if(imageOnly) {
               for(const file of files) {
               // 이미지 파일이 아닌 경우
               if (file.type.indexOf("image/") == -1) {
               throw new Error("이미지 형식의 파일만 업로드 가능합니다.");
               }
               }
               formData.append("imageOnly", imageOnly);
              }
              // 이미지만 업로드 가능일 때 처리하기 E


            for(const file of files) {
                formData.append("file", file);
            }

            const { ajaxLoad } = commonLib;
            ajaxLoad("POST", "/api/file", formData, "json")
                .then((res) => { // 요청 성공 시 넘어가는 데이터
                    if(res && res.success){ // 파일 업로드 성공 시
                        // == function 은 정의되었다는 뜻.
                        //  함수가 정의되어 있으면 실행
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

// 파일 업로드 버튼 클릭 -> 파일 탐색기 열기
    for (const el of uploadFiles) {
        el.addEventListener("click", function (){

            // 파일 태그 동적으로 :  클릭 시 파일탐색기 생성되도록
            const fileEl = document.createElement("input");
            fileEl.type="file";
            fileEl.multiple = true; // 여러 파일 선택 가능


            // 업로드 파일이 이미지인지 확인 후 설정
            const imageOnly = this.dataset.imageOnly == 'true';
            fileEl.imageOnly = imageOnly;
            fileEl.location = this.dataset.location;

            const singleFile = this.dataset.singleFile == 'true';
            fileEl.singleFile = singleFile;
            if (singleFile) fileEl.multiple = false;

            // 파일 선택시 이벤트 처리
            fileEl.addEventListener("change", function(e) {
              const imageOnly = fileEl.imageOnly || false;
              const location = fileEl.location;
              const singleFile = fileEl.singleFile;

              commonLib.fileManager.upload(e.target.files, location, imageOnly, singleFile);
            });
            fileEl.click();
        });
    }



});