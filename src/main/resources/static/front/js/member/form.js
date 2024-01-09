// 프로필 : 회원 가입, 회원 정보 수정 시에도 필요


/**
* 파일 업로 후 후속 처리 함수
*
* @params files : 업로드 한 파일 정보 목록
*/
function callbackFileUpload(files) {
    if (!files || files.length == 0) {
        return;
    }

    const file = files[0];

    let html = document.getElementById("image1_tpl").innerHTML;

    const imageUrl = file.thumbsUrl.length > 0 ? file.thumbsUrl.pop() : file.fileUrl;
    const seq = file.seq;

    html = html.replace(/\[seq\]/g, seq)
                .replace(/\[imageUrl\]/g, imageUrl);

    const domParser = new DOMParser();
    const dom = domParser.parseFromString(html, "text/html");

    const imageTplEl = dom.querySelector(".image1_tpl_box");


    const profileImage = document.getElementById("profile_image");
    profileImage.innerHTML = "";

    profileImage.appendChild(imageTplEl);
}

/**
* 파일 삭제 후 후속처리 함수
* @param seq : 파일 등록 번호
*/
function callbackFileUpload(seq) {
    const fileEl = document.getElementById(`file_${seq}`); // 템플릿 리터럴 : 변수를 직접 출력(seq)
    fileEl.parentElement.removeChild(fileEl);


    }