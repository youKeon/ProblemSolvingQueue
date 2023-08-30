// Problem Controller API

// 문제 단건 조회
export type ProblemType = {
  title: string;
  url: string;
  level: string;
  category: string;
  solved: boolean;
};

// 문제 수정
export type ProblemUpdateType = {
  url: string;
  category: string;
  isSolved: boolean;
  level: number;
};

// 문제 삭제(isDeleted = true)
export type ProblemDelectionType = {
  isDeleted: boolean;
};

// 문제 되돌리기(isDeleted = false)
export type ProblemRecoveryType = {
  isDeleted: boolean;
};

// 문제 등록
export type ProblemRegistrationType = {
  url: string;
  title: string;
  category: string;
  level: number;
};

// 문제 목록 조회(ProblemType과 함께 사용)
export type PageableRequestType = {
  page: number;
  size: number;
  sort: string[];
};

// 가장 먼저 등록한 문제 조회
export type ProblemOldestType = {
  title: string;
  url: string;
  level: number;
  category: string;
  solved: boolean;
};

// Member Controller API

// 회원가입
export type SignupRequestType = {
  email: string;
  password: string;
};

// 로그인
export type SigninRequestType = {
  email: string;
  password: string;
};

// BookMark Controller API

// 북마크 등록
export type BookMarkRequestType = {
  problemId: number;
};

// 북마크 리스트 조회
export type BookmarkDetailType = {
  url: string;
  level: number;
  category: string;
  solved: boolean;
};

// 북마크 삭제
