package com.nomnom.onnomnom.global.enums;

public enum ErrorCode {
    REQUEST_RESULT_FAILURE("E901", "요청 결과 처리 실패"),

    STORE_REGISTRATION_ERROR("E902", "가계등록 값 에러"),

    S3_SERVICE_FAILURE("E903", "S3 에러"),
    
    EMAIL_VERIFICATION_MISMATCH("E904", "이메일 인증 코드 불일치"),
    
    DUPLICATE_MEMBER_ID("E905", "아이디 중복"),
    
    INVALID_MEMBER_ID("E906", "아이디 유효성 검증 실패"),
    
    INVALID_PASSWORD_FORMAT("E907", "비밀번호 유효성 검증 실패"),
    
    PROFILE_PICTURE_SAVE_FAILURE("E908", "프로필사진 저장 실패"),
    
    PROFILE_PICTURE_VALIDATION_FAIL("E909", "프로필 사진 유효성 검증 실패"),
    
    INVALID_EMAIL_FORMAT("E910", "이메일 유효성 검증 실패"),
    
    INVALID_NICKNAME_FORMAT("E911", "닉네임 유효성 검증 실패"),
    
    DUPLICATE_NICKNAME("E912", "닉네임 중복"),
    
    NO_UPDATE_PERMISSION("E913", "수정 권한 없음"),
    
    NO_DELETE_PERMISSION("E914", "삭제 권한 없음"),
    
    BUSINESS_INFO_REGISTER_FAIL("E915", "운영정보등록 실패"),
    
    BREAK_TIME_RANGE_ERROR("E916", "브레이크타임 범위 오류"),
    
    BUSINESS_INFO_UPDATE_FAIL("E917", "운영정보수정 실패"),
    
    EMAIL_ACCOUNT_NAME_MISMATCH("E918", "이메일 계정 정보, 이름 불일치"),
    
    EMAIL_ACCOUNT_ID_MISMATCH("E919", "이메일 계정 정보, 아이디 불일치"),
    
    VERIFICATION_CODE_NOT_FOUND("E920", "인증코드가 존재하지 않음"),
    
    VERIFICATION_CODE_EXPIRED("E921", "인증 코드의 만료일이 지남"),
    
    INPUT_LENGTH_EXCEEDED("E922", "입력 가능 글자 수 초과"),
    
    RESTAURANT_NOT_FOUND("E923", "맛집 정보가 존재하지 않음"),
    
    NO_SEARCH_RESULTS("E924", "검색 결과가 존재하지 않음"),
    
    MEMBER_NOT_FOUND("E925", "회원정보가 존재하지 않음"),
    
    FAQ_REGISTRATION_FAIL("E926", "faq게시판 등록 실패"),
    
    INVALID_FILE_FORMAT("E927", "파일 형식 오류"),
    
    DB_INSERT_FAILURE("E928", "DB입력 실패"),
    
    DB_QUERY_FAILURE("E929", "DB조회 실패"),
    
    DB_UPDATE_FAILURE("E930", "DB업데이트 실패"),
    
    DB_DELETE_FAILURE("E931", "DB삭제 실패"),
    
    RESTAURANT_VALIDATION_FAIL("E932", "맛집 유효성 검증 실패"),
    
    REVIEW_VALIDATION_FAIL("E933", "리뷰 유효성 검증 실패"),
    
    FILE_SIZE_EXCEEDED("E934", "파일 크기 초과"),
    
    DUPLICATE_MEMBER_EMAIL("E935", "이메일 중복 검사 실패"),
    
    INVALID_REFRESH_TOKEN("E936", "유효하지 않은 리프레쉬 토큰입니다."),
    
    ID_PASSWORD_MISMATCH("E937", "아이디나 비번을 잘못 입력하였습니다."),
    
    BREAK_STARTTIME_ERROR("E938", "브레이크 시작 시간을 잘못 입력하셨습니다."),
	
    BREAK_ENDTIME_ERROR("E939", "브레이크 종료 시간을 잘못 입력하셨습니다.");
	

    private final String code;
    private final String defaultMessage;
    ErrorCode(String code, String defaultMessage) { 
        this.code = code; 
        this.defaultMessage = defaultMessage;
    }
    public String getCode() { return code; }
    public String getDefaultMessage() { return defaultMessage; }
}
