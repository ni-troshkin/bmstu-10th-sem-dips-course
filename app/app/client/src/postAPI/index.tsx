import { Category } from "types/Categories";
import axios from "axios";
import { Reservation } from "types/Reservation";
import { UserRatingResponse } from "types/UserRatingResponse";
import { ServiceAvg } from "types/ServiceAvg";
import { RequestAvg } from "types/RequestAvg";
import { StatMessage } from "types/StatMessage";
import { LibraryResponse } from "types/LibraryResponse";
import { LibraryBookResponse } from "types/LibraryBookResponse";
import { TokenResponse } from "types/TokenResponse";
import { AxiosResponseHeaders } from "axios";

export const backUrl = "http://localhost:8082/api/v1";

const axiosBackend = () => {
    let instance = axios.create({
        baseURL: backUrl,
        headers: {Authorization: `Bearer ${localStorage.getItem('token')}`}
    });

    // instance.interceptors.request.use(function (config) {
    //     const token = localStorage.getItem("token");
    //     // const token = 'eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJvdHdpR29BQTJNTV9LRnlmZHpqdndhS1lrclRyNEhLaklXcmtRZGpsNjNRIn0.eyJleHAiOjE3MjQ2MzA5MDcsImlhdCI6MTcyNDYyOTQwNywiYXV0aF90aW1lIjoxNzI0NjI5Mzg4LCJqdGkiOiJjOWRiNmFiMi04YjA5LTRiMzQtOWJjYS1lMGJjYzg4NDhmZjUiLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODEvcmVhbG1zL0xpYnJhcnlJZGVudGl0eVByb3ZpZGVyIiwiYXVkIjpbInJlYWxtLW1hbmFnZW1lbnQiLCJhY2NvdW50Il0sInN1YiI6IjYzMTYyNmQ0LWFjZWItNDNmZi1hODg0LTFiNTk1MjUxNWU4YyIsInR5cCI6IkJlYXJlciIsImF6cCI6ImxpYnJhcnktY2xpZW50Iiwic2lkIjoiYjljYzFiMTktODUzZC00ZjliLWEzMzYtZTYzMGNlYmJjNzRlIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIqIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJkZWZhdWx0LXJvbGVzLWxpYnJhcnlpZGVudGl0eXByb3ZpZGVyIiwiUk9MRV9VU0VSIiwib2ZmbGluZV9hY2Nlc3MiLCJST0xFX0FETUlOIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJyZWFsbS1tYW5hZ2VtZW50Ijp7InJvbGVzIjpbInZpZXctcmVhbG0iLCJ2aWV3LWlkZW50aXR5LXByb3ZpZGVycyIsIm1hbmFnZS1pZGVudGl0eS1wcm92aWRlcnMiLCJpbXBlcnNvbmF0aW9uIiwicmVhbG0tYWRtaW4iLCJjcmVhdGUtY2xpZW50IiwibWFuYWdlLXVzZXJzIiwicXVlcnktcmVhbG1zIiwidmlldy1hdXRob3JpemF0aW9uIiwicXVlcnktY2xpZW50cyIsInF1ZXJ5LXVzZXJzIiwibWFuYWdlLWV2ZW50cyIsIm1hbmFnZS1yZWFsbSIsInZpZXctZXZlbnRzIiwidmlldy11c2VycyIsInZpZXctY2xpZW50cyIsIm1hbmFnZS1hdXRob3JpemF0aW9uIiwibWFuYWdlLWNsaWVudHMiLCJxdWVyeS1ncm91cHMiXX0sImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoib3BlbmlkIGVtYWlsIHByb2ZpbGUiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsIm5hbWUiOiJOaWtvbGF5IFRyb3Noa2luIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiYWRtaW4iLCJnaXZlbl9uYW1lIjoiTmlrb2xheSIsImZhbWlseV9uYW1lIjoiVHJvc2hraW4iLCJlbWFpbCI6ImFkbWluQGV4YW1wbGUuY29tIn0.XTINJlsWDK80Qo-CfiQnkwxkkLPO6Pi2O-tOL70cHElpX6wm6Fm4SJ5K38Fs7bAoZ-9ks_zd6soSIwzPos2mypLYV8m5H4SMJUJk9aMzDUhwBywzknEQKoh5ThEUiS3fWGzSWFtGHL4s-aWVVyStN-JtSgptmzKg4brMvRsId8s_qbiz1ndqSvlebiyJN3bILSh3ws8_D61fIKyNFmgzQqgctud0ZEzK_K-7eplcmUnP7c4Jb9tAmwfwbZ9daNoPxy1-AnfXERw5LIvvap9UWRamYA-8msxz7hqaFuSfi_LhqLf1I08P904HtwzNN9IezSkG6fo5wjhZZx6hBRWvKg'
    //     if (config.headers && token) {
    //         config.headers.Authorization = 'Bearer ' + token;
    //     }

    //     return config;
    // })

    return instance;
}

export default axiosBackend();


export type AllReservationsResp = {
    status: number,
    content: Reservation[]
}

export type AllAvgServiceTimeResp = {
    status: number,
    content: ServiceAvg[]
}

export type GetLibrariesByCityResp = {
    status: number,
    content: LibraryResponse
}

export type GetBooksInLibraryResp = {
    status: number,
    content: LibraryBookResponse
}

export type AllAvgRequestTimeResp = {
    status: number,
    content: RequestAvg[]
}

export type AllStatsResp = {
    status: number,
    content: StatMessage[]
}

export type UserRatingResp = {
    status: number,
    content: UserRatingResponse
}

export type DefaultResp = {
    status: number,
    content: string | null,
}

export type DefaultHeadersResp = {
    status: number,
    content: string,
    headers: AxiosResponseHeaders
}

export type TokenResponseResp = {
    status: number,
    content: TokenResponse
}