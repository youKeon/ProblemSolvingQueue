import type { AxiosInstance, AxiosResponse } from "axios";
import axios from "axios";
import type {
  ProblemOldestType,
  PageableRequestType,
  ProblemDelectionType,
  ProblemRegistrationType,
  ProblemType,
  ProblemUpdateType,
  SignupRequestType,
  SigninRequestType,
  BookMarkRequestType,
  BookmarkDetailType,
} from "./types";

const instance: AxiosInstance = axios.create({
  baseURL: "http://localhost:8080",
  timeout: 5000,
  headers: {
    "Content-Type": "application/json",
  },
});

// Problem Controller API

// 문제 단건 조회
export const getProblemById = async (id: number) => {
  try {
    const response: AxiosResponse<ProblemType> = await instance.get(
      `/api/v1/problems/${id}`,
    );
    return response.data;
  } catch (error) {
    throw error;
  }
};

// 문제 수정
export const updateProblemById = async (
  id: number,
  updateData: ProblemUpdateType,
): Promise<void> => {
  try {
    await instance.put(`/api/v1/problems/${id}`, updateData);
  } catch (error) {
    throw error;
  }
};

// 문제 삭제(isDeleted = true)
export const deleteProblemById = async (id: number): Promise<void> => {
  try {
    const deletionData: ProblemDelectionType = {
      isDeleted: true,
    };
    await instance.put(`/api/v1/problems/${id}`, deletionData);
  } catch (error) {
    throw error;
  }
};

// 문제 되돌리기(isDeleted = false)
export const recoveryProblemById = async (id: number): Promise<void> => {
  try {
    const recoveryData: ProblemDelectionType = {
      isDeleted: false,
    };
    await instance.put(`/api/v1/problems/${id}`, recoveryData);
  } catch (error) {
    throw error;
  }
};

// 문제 등록
export const registerProblem = async (
  problemData: ProblemRegistrationType,
): Promise<void> => {
  try {
    await instance.post("/api/v1/problems", problemData);
  } catch (error) {
    throw error;
  }
};

// 문제 목록 조회
export const getProblemsList = async (
  pageable: PageableRequestType,
): Promise<ProblemType[]> => {
  try {
    const response = await instance.get("/api/v1/problems/problems", {
      params: pageable,
    });
    return response.data;
  } catch (error) {
    throw error;
  }
};

// 가장 먼저 등록한 문제 조회
export const getFirstProblem = async (): Promise<ProblemOldestType> => {
  try {
    const response = await instance.get("/api/v1/problems/poll");
    return response.data;
  } catch (error) {
    throw error;
  }
};

// Member Controller API

// 회원가입
export const signup = async (signupData: SignupRequestType): Promise<void> => {
  try {
    await instance.post("/api/v1/members/signup", signupData);
  } catch (error) {
    throw error;
  }
};

// 로그인
export const signin = async (signinData: SigninRequestType): Promise<any> => {
  try {
    const response = await instance.post("/api/v1/members/signin", signinData);
    return response.data;
  } catch (error) {
    throw error;
  }
};

// BookMark Controller API

// 북마크 등록
export const addBookmark = async (
  bookmarkData: BookMarkRequestType,
): Promise<void> => {
  try {
    await instance.post("/api/v1/bookmark", bookmarkData);
  } catch (error) {
    throw error;
  }
};

// 북마크 리스트 조회
export const getBookmarkList = async (
  userId: number,
): Promise<BookmarkDetailType> => {
  try {
    const response = await instance.get(`/api/v1/bookmark/${userId}`);
    return response.data;
  } catch (error) {
    throw error;
  }
};

// 북마크 삭제
export const deleteBookmark = async (bookmarkId: number): Promise<void> => {
  try {
    await instance.delete(`/api/v1/bookmark/${bookmarkId}`);
  } catch (error) {
    throw error;
  }
};
