import type { AxiosInstance, AxiosResponse } from "axios";
import axios from "axios";
import type {
  FirstProblemType,
  PageableRequest,
  ProblemDelectionType,
  ProblemRegistrationType,
  ProblemType,
  ProblemUpdateType,
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
      `/api/v1/problems/${id}`
    );
    return response.data;
  } catch (error) {
    throw error;
  }
};

// 문제 수정
export const updateProblemById = async (
  id: number,
  updateData: ProblemUpdateType
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
  problemData: ProblemRegistrationType
): Promise<void> => {
  try {
    await instance.post("/api/v1/problems", problemData);
  } catch (error) {
    throw error;
  }
};

// 문제 목록 조회
export const getProblemsList = async (
  pageable: PageableRequest
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
export const getFirstProblem = async (): Promise<FirstProblemType> => {
  try {
    const response = await instance.get("/api/v1/problems/poll");
    return response.data;
  } catch (error) {
    throw error;
  }
};

// Member Controller API

// 회원가입

// 로그인

// BookMark Controller API

// 북마크 등록

// 북마크 리스트 조회

// 북마크 삭제
